package com.javaweb.service.impl;

import com.javaweb.model.dto.BookingServiceDTO.BookingServiceBulkRequest;
import com.javaweb.model.dto.BookingServiceDTO.BookingServiceDTO;
import com.javaweb.model.entity.*;
import com.javaweb.repository.*;
import com.javaweb.service.BookingServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceServiceImpl implements BookingServiceService {
    @Autowired
    private BookingServiceRepository bookingServiceRepository;
    @Autowired
    private BookingRoomRepository bookingRoomRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BillRepository billRepository;

    @Override
    @Transactional
    public List<BookingServiceDTO> addServicesToRoom(BookingServiceBulkRequest request) {
        BookingRoomEntity bookingRoom = bookingRoomRepository.findById(request.getBookingRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy phòng!"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth.getName();

        boolean isStaffOrAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_STAFF"));

        if (!isStaffOrAdmin) {
            UserEntity currentUser = userRepository.findByAccount_Email(currentEmail).get();

            if (currentUser == null || !bookingRoom.getCustomer().getId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Truy cập bị từ chối! Bạn không thể đặt dịch vụ cho phòng không phải của mình.");
            }
        }

        if ((bookingRoom.getStatus() != null && bookingRoom.getStatus() == 0) || bookingRoom.getActualCheckOutTime() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phòng không khả dụng để gọi dịch vụ!");
        }

        List<BookingServiceEntity> entitiesToSave = new ArrayList<>();
        Float totalAmountToAdd = 0.0f;

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (BookingServiceBulkRequest.ServiceItem item : request.getItems()) {
                ServiceEntity service = serviceRepository.findById(item.getServiceId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dịch vụ ID " + item.getServiceId() + " không tồn tại!"));

                if (service.getIsAvaiable() != null && service.getIsAvaiable() == 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dịch vụ '" + service.getName() + "' đang tạm ngưng!");
                }

                BookingServiceEntity entity = new BookingServiceEntity();
                entity.setBookingRoom(bookingRoom);
                entity.setService(service);
                entity.setPrice(service.getPrice()); // Lưu giá gốc

                int qty = (item.getQuantity() != null && item.getQuantity() > 0) ? item.getQuantity() : 1;
                entity.setQuantity(qty);
                entity.setStatus(0); // Pending

                entitiesToSave.add(entity);
                totalAmountToAdd += (service.getPrice() * qty);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Danh sách dịch vụ trống!");
        }
        List<BookingServiceEntity> savedEntities = bookingServiceRepository.saveAll(entitiesToSave);
        updateBillMoney(bookingRoom, totalAmountToAdd);
        List<BookingServiceDTO> resultDTOs = new ArrayList<>();
        for (BookingServiceEntity en : savedEntities) {
            resultDTOs.add(convertToDTO(en));
        }
        return resultDTOs;
    }

    @Override
    public void updateServiceQuantity(Integer id, Integer newQuantity) {
        BookingServiceEntity entity = bookingServiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order không tồn tại!"));

        checkPermission(entity.getBookingRoom());
        int oldQuantity = entity.getQuantity();
        Float price = entity.getPrice();
        Float diffAmount = (newQuantity - oldQuantity) * price;

        if (newQuantity <= 0) {
            Float amountToSubtract = -(oldQuantity * price);
            updateBillMoney(entity.getBookingRoom(), amountToSubtract);
            bookingServiceRepository.delete(entity);
        } else {
            entity.setQuantity(newQuantity);
            bookingServiceRepository.save(entity);
            updateBillMoney(entity.getBookingRoom(), diffAmount);
        }
    }

    @Override
    public void deleteService(Integer id) {
        BookingServiceEntity entity = bookingServiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy order!"));
        checkPermission(entity.getBookingRoom());
        if (entity.getStatus() != 2) {
            Float amountToSubtract = -(entity.getPrice() * entity.getQuantity());
            updateBillMoney(entity.getBookingRoom(), amountToSubtract);
        }
        bookingServiceRepository.delete(entity);
    }

    @Override
    public void cancelService(Integer id) {
        BookingServiceEntity entity = bookingServiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy order!"));
        checkPermission(entity.getBookingRoom());
        if (entity.getStatus() == 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dịch vụ đã được phục vụ xong, không thể hủy!");
        }
        if (entity.getStatus() == 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dịch vụ này đã bị hủy trước đó rồi!");
        }
        entity.setStatus(2);
        // entity.setCompletedDate(LocalDateTime.now());
        bookingServiceRepository.save(entity);
        Float amountToSubtract = -(entity.getPrice() * entity.getQuantity());
        updateBillMoney(entity.getBookingRoom(), amountToSubtract);
    }

    @Override
    public List<BookingServiceDTO> getAllBookingServices(Integer page, Integer limit) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isStaffOrAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_STAFF"));
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = null;
        if (page != null && limit != null && page > 0 && limit > 0) {
            pageable = PageRequest.of(page - 1, limit, sort);
        }
        List<BookingServiceEntity> entities;
        if (isStaffOrAdmin) {
            if (pageable != null) {
                entities = bookingServiceRepository.findAll(pageable).getContent();
            } else {
                entities = bookingServiceRepository.findAll(sort);
            }
        } else {
            String currentEmail = auth.getName();
            UserEntity currentUser = userRepository.findByAccount_Email(currentEmail).get();
            if (currentUser == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bạn chưa đăng nhập hoặc tài khoản lỗi!");
            }
            if (pageable != null) {
                entities = bookingServiceRepository.findByBookingRoom_Customer_Id(currentUser.getId(), pageable).getContent();
            } else {
                entities = bookingServiceRepository.findByBookingRoom_Customer_Id(currentUser.getId(), sort);
            }
        }
        List<BookingServiceDTO> result = new ArrayList<>();
        for (BookingServiceEntity en : entities) {
            result.add(convertToDTO(en));
        }
        return result;
    }

    @Override
    public BookingServiceDTO getBookingServiceById(Integer id) {
        BookingServiceEntity entity = bookingServiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy dịch vụ với ID: " + id));
        checkPermission(entity.getBookingRoom());
        return convertToDTO(entity);
    }

    @Override
    public List<BookingServiceDTO> getServicesByBookingRoom(Integer bookingRoomId) {
        List<BookingServiceEntity> entities = bookingServiceRepository.findByBookingRoom_Id(bookingRoomId);
        List<BookingServiceDTO> dtos = new ArrayList<>();
        for (BookingServiceEntity en : entities) {
            dtos.add(convertToDTO(en));
        }
        return dtos;
    }

    private void updateBillMoney(BookingRoomEntity bookingRoom, Float amountChange) {
        BillEntity bill = bookingRoom.getBill();
        if (bill != null) {
            float currentTotal = 0.0f;
            try {
                if (bill.getTotalBeforeTax() != null) {
                    currentTotal = bill.getTotalBeforeTax();
                }
            } catch (NumberFormatException e) {
                currentTotal = 0.0f;
            }
            Float newTotal = currentTotal + amountChange;
            if (newTotal < 0) newTotal = 0.0f;
            bill.setTotalBeforeTax(newTotal);
            bill.setTotalAfterTax((bill.getTotalBeforeTax() * 0.1f));
            billRepository.save(bill);
        }
    }

    private void checkPermission(BookingRoomEntity bookingRoom) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = auth.getName();
        boolean isStaffOrAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_STAFF"));
        if (!isStaffOrAdmin) {
            UserEntity currentUser = userRepository.findByAccount_Email(currentEmail).get();
            if (currentUser == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn chưa đăng nhập hoặc tài khoản lỗi!");
            }
            if (!bookingRoom.getCustomer().getId().equals(currentUser.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Truy cập bị từ chối! Bạn không có quyền thao tác trên phòng này.");
            }
        }
    }

    private BookingServiceDTO convertToDTO(BookingServiceEntity entity) {
        BookingServiceDTO dto = new BookingServiceDTO();
        dto.setId(entity.getId());
        dto.setBookingRoomId(entity.getBookingRoom().getId());
        dto.setServiceId(entity.getService().getId());

        // Thông tin chi tiết
        dto.setServiceName(entity.getService().getName());
        dto.setUnit(entity.getService().getUnit());
        dto.setPrice(entity.getPrice());
        dto.setQuantity(entity.getQuantity());

        // Tính tổng tiền
        if (entity.getPrice() != null) {
            dto.setTotalPrice(entity.getPrice() * entity.getQuantity());
        }

        dto.setStatus(entity.getStatus());
        dto.setcreatedTime(entity.getCreatedTime());
        return dto;
    }
}
