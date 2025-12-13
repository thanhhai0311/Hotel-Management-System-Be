package com.javaweb.service.impl;

import com.javaweb.model.dto.BookingRoomDTO.BookingItemDTO;
import com.javaweb.model.dto.BookingRoomDTO.BookingRequestDTO;
import com.javaweb.model.entity.*;
import com.javaweb.repository.*;
import com.javaweb.service.BookingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    private static final LocalTime STANDARD_CHECKIN_TIME = LocalTime.of(14, 0); // 14:00 PM
    private static final LocalTime STANDARD_CHECKOUT_TIME = LocalTime.of(12, 0); // 12:00 PM

    @Override
    @Transactional
    public Map<String, Object> createBooking(BookingRequestDTO request) {
        UserEntity customer;
        if (request.getCustomerId() != null) {
            customer = userRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Khách hàng không tồn tại"));
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
                newUser.setAccount(null);
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

            if (lockedRoom.getRoomStatus() == null || lockedRoom.getRoomStatus().getId() != 1) {
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
}