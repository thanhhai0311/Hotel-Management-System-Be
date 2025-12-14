package com.javaweb.converter;

import com.javaweb.model.dto.BookingRoomDTO.BookingResponseDTO;
import com.javaweb.model.entity.BookingRoomEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingRoomConverter {

    public BookingResponseDTO toResponseDTO(BookingRoomEntity entity) {
        BookingResponseDTO dto = new BookingResponseDTO();

        // 1. Map thông tin cơ bản
        dto.setId(entity.getId());
        dto.setContractCheckInTime(entity.getContractCheckInTime());
        dto.setContractCheckOutTime(entity.getContractCheckOutTime());
        dto.setActualCheckInTime(entity.getActualCheckInTime());
        dto.setActualCheckOutTime(entity.getActualCheckOutTime());

        // 2. Map thông tin Phòng
        if (entity.getRoom() != null) {
            dto.setRoomId(entity.getRoom().getId());
            dto.setRoomNumber(entity.getRoom().getRoomNumber());
            if (entity.getRoom().getRoomType() != null) {
                dto.setRoomType(entity.getRoom().getRoomType().getName());
                // Lưu ý ép kiểu về Double nếu trong DB lưu Float/BigDecimal
                dto.setRoomPrice(Double.valueOf(entity.getRoom().getRoomType().getPrice()));
            }
        }

        // 3. Map thông tin Khách hàng
        if (entity.getCustomer() != null) {
            dto.setCustomerId(entity.getCustomer().getId());
            dto.setCustomerName(entity.getCustomer().getName());
            dto.setCustomerPhone(entity.getCustomer().getPhone());
        }

        // 4. Map thông tin Nhân viên
        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getId());
            dto.setEmployeeName(entity.getEmployee().getName());
        }

        // 5. Map thông tin Khuyến mãi
        if (entity.getRoomPromotion() != null) {
            dto.setPromotionName(entity.getRoomPromotion().getPromotion().getName());
            dto.setDiscount(entity.getRoomPromotion().getPromotion().getDiscount());
        }

        // 6. Map thông tin Hóa đơn (Bill)
        if (entity.getBill() != null) {
            dto.setBillId(entity.getBill().getId());
            // Giả sử getTotalAfterTax trả về số, ép kiểu về Double cho an toàn
            dto.setTotalMoney(Float.valueOf(entity.getBill().getTotalAfterTax()));

            if (entity.getBill().getPaymentStatus() != null) {
                dto.setPaymentStatus(entity.getBill().getPaymentStatus().getName());
            } else {
                dto.setPaymentStatus("Chưa thanh toán");
            }
        }

        // 7. Map danh sách Dịch vụ (Booking Services)
//        List<BookingResponseDTO.BookingServiceInfoDTO> serviceList = new ArrayList<>();
//        if (entity.getBookingServices() != null && !entity.getBookingServices().isEmpty()) {
//            serviceList = entity.getBookingServices().stream()
//                    .map(this::toServiceDTO) // Gọi hàm phụ bên dưới
//                    .collect(Collectors.toList());
//        }
//        dto.setServices(serviceList);
//
        return dto;
    }

    // Hàm phụ để map từng dịch vụ nhỏ (giúp code gọn hơn)
//    private BookingResponseDTO.BookingServiceInfoDTO toServiceDTO(BookingServiceEntity bs) {
//        String svName = (bs.getService() != null) ? bs.getService().getName() : "Unknown Service";
//        Double svPrice = (bs.getPrice() != null) ? Double.valueOf(bs.getPrice()) : 0.0;
//        int qty = (bs.getQuantity() != null) ? bs.getQuantity() : 0;
//
//        return new BookingResponseDTO.BookingServiceInfoDTO(svName, qty, svPrice);
//    }

}