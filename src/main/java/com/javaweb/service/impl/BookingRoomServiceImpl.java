package com.javaweb.service.impl;

import com.javaweb.converter.BookingRoomConverter;
import com.javaweb.model.dto.BookingRoomDTO.BookingItemDTO;
import com.javaweb.model.dto.BookingRoomDTO.BookingRequestDTO;
import com.javaweb.model.dto.BookingRoomDTO.BookingResponseDTO;
import com.javaweb.model.dto.CheckoutDTO.CheckoutInfoDTO;
import com.javaweb.model.entity.*;
import com.javaweb.repository.*;
import com.javaweb.service.BookingRoomService;
import com.javaweb.service.CustomerIdentificationService;
import com.javaweb.utils.VietQRUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingRoomServiceImpl implements BookingRoomService {
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private BookingRoomRepository bookingRoomRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentStatusRepository paymentStatusRepository;
    @Autowired
    private RoomPromotionRepository roomPromotionRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private BookingRoomConverter bookingRoomConverter;
    @Autowired
    private CustomerIdentificationService identificationService;
    @Autowired
    private RoomStatusRepository roomStatusRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BlackListRepository blackListRepository;
    @Autowired
    private HotelRepository hotelRepository;

    private static final LocalTime STANDARD_CHECKIN_TIME = LocalTime.of(14, 0); // 14:00 PM
    private static final LocalTime STANDARD_CHECKOUT_TIME = LocalTime.of(12, 0); // 12:00 PM
    private static final long GRACE_PERIOD_MINUTES = 30;
    private static final int MAX_CANCEL_LIMIT = 3;
    private static final long PENALTY_DAYS_BEFORE_CHECKIN = 3;


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public Map<String, Object> createBooking(BookingRequestDTO request) {
        UserEntity customer;
        if (request.getCustomerId() != null) {
            customer = userRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khách hàng không tồn tại"));
            Optional<BlackListEntity> blackListOpt = blackListRepository.findByCustomerId(request.getCustomerId());
            if (blackListOpt.isPresent()) {
                BlackListEntity blackList = blackListOpt.get();
                if (blackList.getCount() >= MAX_CANCEL_LIMIT) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tài khoản của bạn đang bị tạm khóa tính năng đặt phòng do hủy quá "
                            + MAX_CANCEL_LIMIT + " lần. Vui lòng liên hệ khách sạn để được hỗ trợ.");
                }
            }
        } else {
            if (request.getCustomerPhone() == null || request.getCustomerName() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thiếu tên hoặc SĐT khách hàng");
            }
            Optional<UserEntity> existingUser = userRepository.findOneByPhone(request.getCustomerPhone());
            if (existingUser.isPresent()) {
                customer = existingUser.get();
                customer.setName(request.getCustomerName());
                customer.setIdentification(request.getIdentification());
            } else {
                UserEntity newUser = new UserEntity();
                newUser.setName(request.getCustomerName());
                newUser.setPhone(request.getCustomerPhone());
                newUser.setIdentification(request.getIdentification());
                newUser.setAccount(null); // là khách hàng mới
                customer = userRepository.save(newUser);
            }
        }

        if (request.getBookings() == null || request.getBookings().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Danh sách đặt phòng trống");
        }

        // 2. Sắp xếp danh sách phòng để Lock theo thứ tự (tránh Deadlock)
        List<BookingItemDTO> sortedBookings = request.getBookings().stream()
                .sorted(Comparator.comparing(BookingItemDTO::getRoomId))
                .collect(Collectors.toList());

        // 3. VALIDATE & LOCK
        for (BookingItemDTO item : sortedBookings) {

            // --- A. PESSIMISTIC LOCK ---
            RoomEntity lockedRoom = roomRepository.findByIdWithLock(item.getRoomId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phòng ID " + item.getRoomId() + " không tồn tại"));

            if (lockedRoom.getRoomStatus() == null || lockedRoom.getRoomStatus().getId() == 4) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Phòng " + lockedRoom.getRoomNumber() + " hiện không khả dụng.");
            }

            // --- B. VALIDATE THỜI GIAN ---
            if (!item.getCheckOutDate().isAfter(item.getCheckInDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ngày check-out phải sau ngày check-in.");
            }

            // --- C. CHECK TRÙNG LỊCH (Dùng LocalDate trực tiếp) ---
            boolean isBooked = bookingRoomRepository.existsByRoomIdAndOverlappingTime(
                    item.getRoomId(),
                    item.getCheckInDate().atTime(STANDARD_CHECKIN_TIME),
                    item.getCheckOutDate().atTime(STANDARD_CHECKIN_TIME)
            );

            if (isBooked) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Phòng " + lockedRoom.getRoomNumber() + " đã kín lịch trong thời gian bạn chọn.");
            }

//            roomRepository.findById(item.getRoomId()).get().setRoomStatus(roomStatusRepository.findById(2).get());
        }

        // 4. Tạo Draft Bill
        PaymentStatusEntity draftStatus = paymentStatusRepository.findById(1)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi cấu hình trạng thái thanh toán"));

//        PaymentMethodEntity draftMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phương thức thanh toán"));

        BillEntity draftBill = new BillEntity();
        draftBill.setCustomer(customer);
        draftBill.setPaymentStatus(draftStatus);
        draftBill.setPaymentMethod(null);
        draftBill.setPaymentDate(new Date());
        draftBill.setCreatedAt(LocalDateTime.now());
        draftBill.setTotalBeforeTax(0f);
        draftBill.setTotalAfterTax(0f);

        draftBill = billRepository.save(draftBill);

        // 5. Tạo Booking Room
        float totalAmount = 0f;
        List<BookingRoomEntity> bookingEntities = new ArrayList<>();

        for (BookingItemDTO item : sortedBookings) {
            RoomEntity room = roomRepository.findById(item.getRoomId()).get();

            BookingRoomEntity booking = new BookingRoomEntity();
            booking.setBill(draftBill);
            booking.setRoom(room);
            booking.setCustomer(customer);

            LocalDateTime actualCheckIn = item.getCheckInDate().atTime(STANDARD_CHECKIN_TIME);
            LocalDateTime actualCheckOut = item.getCheckOutDate().atTime(STANDARD_CHECKOUT_TIME);

            // LƯU Ý: Gán trực tiếp LocalDate, không cần convert sang LocalDateTime nữa
            booking.setContractCheckInTime(actualCheckIn);
            booking.setContractCheckOutTime(actualCheckOut);

            // Logic: Lúc mới đặt, Actual Time thường để null hoặc bằng Contract Time tùy nghiệp vụ
            // Ở đây ta để null, khi nào khách đến check-in thật sự thì lễ tân update sau
            booking.setActualCheckInTime(null);
            booking.setActualCheckOutTime(null);

            // Xử lý khuyến mãi
            if (item.getRoomPromotionId() != null) {
                RoomPromotionEntity promo = roomPromotionRepository.findById(item.getRoomPromotionId()).orElse(null);
                booking.setRoomPromotion(promo);
            }

            // Tính tiền
            long nights = ChronoUnit.DAYS.between(item.getCheckInDate(), item.getCheckOutDate());
            if (nights < 1) nights = 1;

            RoomTypeEntity roomType = room.getRoomType();

            if (roomType == null || roomType.getPrice() == null) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi cấu hình giá phòng.");
            }

            float roomPrice = roomType.getPrice();
            float bookingPrice = roomPrice * nights;

            if (item.getRoomPromotionId() != null) {
                RoomPromotionEntity rp = roomPromotionRepository.findById(item.getRoomPromotionId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy khuyến mãi"));
                float discount = rp.getPromotion().getDiscount() / 100;
                bookingPrice = bookingPrice - bookingPrice * discount;
            }

            totalAmount += bookingPrice;
            bookingEntities.add(booking);
        }

        bookingRoomRepository.saveAll(bookingEntities);

        // 6. Cập nhật Bill
        draftBill.setTotalBeforeTax(totalAmount);
        float tax = totalAmount * 0.1f;
        draftBill.setTotalAfterTax(totalAmount + tax);
        BillEntity savedBill = billRepository.save(draftBill);
        Map<String, Object> billInfo = new LinkedHashMap<>(); // Dùng LinkedHashMap để giữ thứ tự key đẹp
        billInfo.put("id", savedBill.getId());

        // --- Xử lý UserEntity (Customer) ---
        // Kiểm tra null để tránh lỗi NullPointerException
        if (savedBill.getCustomer() != null) {
            billInfo.put("customerName", savedBill.getCustomer().getName());
            billInfo.put("customerId", savedBill.getCustomer().getId());
        }

        // --- Xử lý PaymentStatusEntity ---
        if (savedBill.getPaymentStatus() != null) {
            billInfo.put("paymentStatus", savedBill.getPaymentStatus().getName());
            billInfo.put("paymentStatusId", savedBill.getPaymentStatus().getId());
        }

        if (savedBill.getPaymentMethod() != null) {
            billInfo.put("paymentMethodId", savedBill.getPaymentMethod().getId());
            billInfo.put("paymentMethod", savedBill.getPaymentMethod().getName());
        }

        billInfo.put("paymentDate", savedBill.getPaymentDate());
        billInfo.put("totalBeforeTax", savedBill.getTotalBeforeTax());
        billInfo.put("totalAfterTax", savedBill.getTotalAfterTax());

        // B. Map danh sách Booking Rooms chi tiết
        List<Map<String, Object>> bookingListInfo = bookingEntities.stream().map(booking -> {
            Map<String, Object> detail = new LinkedHashMap<>();
            detail.put("id", booking.getId());

            // Thông tin phòng
            if (booking.getRoom() != null) {
                detail.put("roomNumber", booking.getRoom().getRoomNumber());
                detail.put("roomId", booking.getRoom().getId());

                // Thông tin loại phòng & giá
                if (booking.getRoom().getRoomType() != null) {
                    detail.put("roomType", booking.getRoom().getRoomType().getName());
                    detail.put("pricePerNight", booking.getRoom().getRoomType().getPrice());
                }
            }

            // Thông tin khuyến mãi (nếu có)
            if (booking.getRoomPromotion() != null) {
                detail.put("promotionId", booking.getRoomPromotion().getId());
                detail.put("discount", booking.getRoomPromotion().getPromotion().getDiscount()); // Giả sử có getDiscount
            } else {
                detail.put("promotionName", null);
            }

            // Thời gian hợp đồng (Trả về đầy đủ ngày giờ)
            detail.put("contractCheckInTime", booking.getContractCheckInTime());
            detail.put("contractCheckOutTime", booking.getContractCheckOutTime());

            return detail;
        }).collect(Collectors.toList());

        // C. Tổng hợp Response cuối cùng
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Tạo đơn đặt phòng thành công");
        Map<String, Object> dataMap = new LinkedHashMap<>();
        dataMap.put("bill", billInfo);
        dataMap.put("bookings", bookingListInfo);

        response.put("data", dataMap);

        return response;
    }

    @Override
    public List<BookingResponseDTO> getAllBookings(Integer page, Integer limit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        boolean isAdminOrStaff = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_STAFF"));
        List<BookingRoomEntity> entities;
        Sort sort = Sort.by("id").descending();
        Pageable pageable = (page != null && limit != null)
                ? PageRequest.of(page - 1, limit, sort)
                : null;
        if (isAdminOrStaff) {
            if (pageable != null) {
                entities = bookingRoomRepository.findAll(pageable).getContent();
            } else {
                entities = bookingRoomRepository.findAll(sort);
            }
        } else {
            UserEntity currentUser = userRepository.findByAccount_Email(currentEmail).get();
            if (currentUser == null) {
                return Collections.emptyList();
            }
            if (pageable != null) {
                entities = bookingRoomRepository.findByCustomer_Id(currentUser.getId(), pageable).getContent();
            } else {
                entities = bookingRoomRepository.findByCustomer_Id(currentUser.getId(), sort);
            }
        }

        return entities.stream()
                .map(bookingRoomConverter::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDTO getBookingById(Integer id) {
        BookingRoomEntity bookingEntity = bookingRoomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn đặt phòng với ID: " + id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();

        boolean isAdminOrStaff = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_STAFF"));

        if (!isAdminOrStaff) {
            UserEntity currentUser = userRepository.findByAccount_Email(currentEmail).get();

            if (currentUser == null || !bookingEntity.getCustomer().getId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem đơn đặt phòng này!");
            }
        }
        return bookingRoomConverter.toResponseDTO(bookingEntity);
    }

    @Override
    @Transactional
    public void cancelBooking(Integer bookingId) {
        BookingRoomEntity bookingRoom = bookingRoomRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng đặt này!"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName();
        LocalDateTime checkinTime = bookingRoom.getContractCheckInTime();
        boolean isAdminOrStaff = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_STAFF"));

        if (!isAdminOrStaff) {
            UserEntity currentUser = userRepository.findByAccount_Email(currentEmail).get();
            if (currentUser == null || !bookingRoom.getCustomer().getId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền hủy phòng này!");
            }
        }

        if (bookingRoom.getActualCheckInTime() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phòng này đã check-in, không thể hủy!");
        }

        if (bookingRoom.getStatus() != null && bookingRoom.getStatus() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phòng này đã bị hủy trước đó rồi!");
        }

        bookingRoom.setStatus(0);
        bookingRoomRepository.save(bookingRoom);

        BillEntity bill = bookingRoom.getBill();
        if (bill != null) {
            updateBillTotal(bill);

            boolean allCancelled = bill.getBookingRooms().stream()
                    .allMatch(br -> br.getStatus() == 0);
            if (allCancelled) {
                PaymentStatusEntity cancelStatus = (PaymentStatusEntity) paymentStatusRepository.findByName("Canceled")
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi cấu hình: Không tìm thấy trạng thái 'Đã hủy' trong DB"));
                LocalDateTime createdTime = bill.getCreatedAt();
                LocalDateTime cancelTime = LocalDateTime.now();

                long minutesDiff = Duration.between(createdTime, cancelTime).toMinutes();
                long daysUntilCheckIn = Duration.between(cancelTime, checkinTime).toDays();
                if (minutesDiff > GRACE_PERIOD_MINUTES && daysUntilCheckIn < PENALTY_DAYS_BEFORE_CHECKIN) {
                    updateBlackListCount(bill.getCustomer());
                }

                bill.setPaymentStatus(cancelStatus);
            }
            billRepository.save(bill);
        }
    }

    private void updateBlackListCount(UserEntity customer) {
        BlackListEntity blackList = blackListRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    BlackListEntity newBL = new BlackListEntity();
                    newBL.setCustomer(customer);
                    newBL.setHotel(hotelRepository.findById(1).get());
                    newBL.setCount(0);
                    return newBL;
                });

        blackList.setCount(blackList.getCount() + 1);
        blackListRepository.save(blackList);
    }

    // Hàm phụ tính lại tiền (Ví dụ đơn giản)
    private void updateBillTotal(BillEntity bill) {
        float newTotal = 0;
        for (BookingRoomEntity br : bill.getBookingRooms()) {
            if (br.getStatus() == 1) { // Chỉ cộng tiền những phòng còn Active
                // newTotal += br.getRoom().getRoomType().getPrice();
                // Cộng thêm tiền dịch vụ nếu có...
                float discount = 0f;
                if (br.getRoomPromotion() != null) {
                    discount = br.getRoomPromotion().getPromotion().getDiscount() / 100;
                }
                newTotal += br.getRoom().getRoomType().getPrice() - br.getRoom().getRoomType().getPrice() * discount;
            }
        }
        bill.setTotalBeforeTax(newTotal);
        bill.setTotalAfterTax(newTotal + newTotal * 0.1f);
    }

    @Override
    public void checkIn(Integer id, MultipartFile cccdImage) {
        BookingRoomEntity booking = bookingRoomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn đặt phòng!"));

        if (booking.getStatus() != null && booking.getStatus() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Đơn này đã bị hủy, không thể check-in!");
        }

        if (booking.getActualCheckInTime() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khách đã check-in rồi, không thể thực hiện lại!");
        }

        booking.setActualCheckInTime(LocalDateTime.now()); // Gán giờ hiện tại
        RoomEntity room = booking.getRoom();
        room.setRoomStatus(roomStatusRepository.findById(2).get()); // Occupied
        roomRepository.save(room);

        if (booking.getBill() != null && booking.getBill().getCustomer() != null && cccdImage != null) {
            try {
                Integer customerId = booking.getBill().getCustomer().getId();
                identificationService.processCheckInImage(customerId, cccdImage);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi upload ảnh: " + e.getMessage());
            }
        }

        bookingRoomRepository.save(booking);
    }

    @Override
    public void checkOut(Integer id) {
        BookingRoomEntity booking = bookingRoomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy đơn đặt phòng!"));

        if (booking.getActualCheckInTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khách chưa check-in, không thể check-out!");
        }

        if (booking.getActualCheckOutTime() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khách đã check-out rồi!");
        }

        booking.setActualCheckOutTime(LocalDateTime.now());
        booking.setStatus(2); // đã checkout

        RoomEntity room = booking.getRoom();
        room.setRoomStatus(roomStatusRepository.findById(1).get()); // Available
        roomRepository.save(room);
        bookingRoomRepository.save(booking);

        if (booking.getBill() != null && booking.getBill().getCustomer() != null) {
            Integer customerId = booking.getBill().getCustomer().getId();
            identificationService.processCheckOutExpiry(customerId);
        }
    }

    @Override
    public CheckoutInfoDTO getPaymentInfoByBill(Integer billId) {
        BillEntity bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hóa đơn!"));

        CheckoutInfoDTO info = new CheckoutInfoDTO();
        info.setBillId(bill.getId());
        info.setTotalAmount(bill.getTotalAfterTax());

        if (bill.getCustomer() != null) {
            info.setCustomerName(bill.getCustomer().getName());
        }

        List<Integer> roomNumbers = new ArrayList<>();
        if (bill.getBookingRooms() != null) {
            for (BookingRoomEntity br : bill.getBookingRooms()) {
                if (br.getRoom() != null) {
                    roomNumbers.add(br.getRoom().getRoomNumber());
                }
            }
        }
        info.setRoomNumbers(roomNumbers);

        // Tạo nội dung chuyển khoản
        String content = "THANHTOAN BILL " + bill.getId();
        info.setPaymentContent(content);

        // Tạo QR chuyển khoản
        String qrUrl = VietQRUtils.generateQRUrl(bill.getTotalAfterTax(), content);
        info.setQrUrl(qrUrl);

        return info;
    }

    @Override
    @Transactional
    public void confirmCheckoutByBill(Integer billId, Integer paymentMethodId) { // 1: QR Banking, 2: Cash
        BillEntity bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hóa đơn!"));

        // Payment status: 1-Pending, 2-Completed, 3-Cancel
        if (bill.getPaymentStatus() != null && bill.getPaymentStatus().getId() == 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hóa đơn này đã được thanh toán rồi!");
        }

        List<BookingRoomEntity> bookingRooms = bill.getBookingRooms();
        if (bookingRooms == null || bookingRooms.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hóa đơn không có phòng nào!");
        }

        for (BookingRoomEntity booking : bookingRooms) {
            if (booking.getStatus() == null || booking.getStatus() != 2) {
                // Set thời gian check out là hiện tại
                if (booking.getActualCheckOutTime() == null) {
                    booking.setActualCheckOutTime(LocalDateTime.now());
                }

                // Đổi trạng thái Booking -> Completed/Checkout (2)
                booking.setStatus(2);

                // Trả phòng (Room) về trạng thái Trống (Available = 1)
                RoomEntity room = booking.getRoom();
                room.setRoomStatus(roomStatusRepository.findById(1)
                        .orElseThrow(() -> new RuntimeException("Lỗi cấu hình Room Status")));
                roomRepository.save(room);
            }
        }

        bookingRoomRepository.saveAll(bookingRooms);

        PaymentStatusEntity paidStatus = paymentStatusRepository.findById(2) // 2 = PAID/COMPLETED
                .orElseThrow(() -> new RuntimeException("Lỗi cấu hình Payment Status"));
        bill.setPaymentStatus(paidStatus);

        PaymentMethodEntity method = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phương thức thanh toán sai"));
        bill.setPaymentMethod(method);

        billRepository.save(bill);

        // Đặt ngày xóa CCCD của khách
        if (bill.getCustomer() != null) {
            identificationService.processCheckOutExpiry(bill.getCustomer().getId());
        }
    }
}
