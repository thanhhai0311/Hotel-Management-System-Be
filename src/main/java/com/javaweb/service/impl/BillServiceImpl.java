package com.javaweb.service.impl;

import com.javaweb.model.dto.BillDTO.BillResponseDTO;
import com.javaweb.model.entity.BillEntity;
import com.javaweb.model.entity.BookingRoomEntity;
import com.javaweb.model.entity.BookingServiceEntity;
import com.javaweb.model.entity.UserEntity;
import com.javaweb.repository.BillRepository;
import com.javaweb.repository.PaymentStatusRepository;
import com.javaweb.repository.UserRepository;
import com.javaweb.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Override
    public List<BillResponseDTO> getAllBills(Integer page, Integer limit) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isStaffOrAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_STAFF"));

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = (page != null && limit != null && page > 0 && limit > 0)
                ? PageRequest.of(page - 1, limit, sort)
                : null;

        List<BillEntity> bills;

        if (isStaffOrAdmin) {
            if (pageable != null) bills = billRepository.findAll(pageable).getContent();
            else bills = billRepository.findAll(sort);
        } else {
            String email = auth.getName();
            UserEntity user = userRepository.findByAccount_Email(email).get();
            if (user == null) return new ArrayList<>();

            if (pageable != null) bills = billRepository.findByCustomer_Id(user.getId(), pageable).getContent();
            else bills = billRepository.findByCustomer_Id(user.getId(), sort);
        }

        if (bills == null) return new ArrayList<>();

        return bills.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public BillResponseDTO getBillById(Integer id) {
        BillEntity bill = billRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hóa đơn!"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isStaffOrAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_STAFF"));

        if (!isStaffOrAdmin) {
            String email = auth.getName();
            UserEntity user = userRepository.findByAccount_Email(email).get();

            if (user == null || bill.getCustomer() == null || !bill.getCustomer().getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xem hóa đơn này!");
            }
        }

        return convertToDTO(bill);
    }

    @Override
    public BillResponseDTO updateStatusBill(Integer id) {
        BillEntity bill = billRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy hóa đơn!"));
        if (bill.getPaymentStatus().getId() != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Khách hàng đã check-in hoặc hủy booking rồi");
        }
        bill.setPaymentStatus(paymentStatusRepository.findById(4)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thầy trạng thái hóa đơn!")));
        billRepository.save(bill);
        return convertToDTO(bill);
    }

    private BillResponseDTO convertToDTO(BillEntity bill) {
        if (bill == null) return null; // Check cấp cao nhất

        BillResponseDTO dto = new BillResponseDTO();
        dto.setId(bill.getId());
        dto.setPaymentDate(bill.getPaymentDate()); // Date có thể null, không sao

        dto.setTotalBeforeTax(bill.getTotalBeforeTax() != null ? bill.getTotalBeforeTax() : 0f);
        dto.setTotalAfterTax(bill.getTotalAfterTax() != null ? bill.getTotalAfterTax() : 0f);
        dto.setPhoneNumber(bill.getCustomer().getPhone());

        if (bill.getPaymentMethod() != null) {
            dto.setPaymentMethod(bill.getPaymentMethod().getName());
        } else {
            dto.setPaymentMethod(null);
        }

        if (bill.getPaymentStatus() != null) {
            dto.setPaymentStatus(bill.getPaymentStatus().getName());
        } else {
            dto.setPaymentStatus("Unknown");
        }

        if (bill.getCustomer() != null) {
            dto.setCustomerId(bill.getCustomer().getId());
            dto.setCustomerName(bill.getCustomer().getName());
        } else {
            dto.setCustomerName("Unknown");
        }

        List<BillResponseDTO.BillRoomDTO> roomDTOs = new ArrayList<>();

        if (bill.getBookingRooms() != null && !bill.getBookingRooms().isEmpty()) {
            for (BookingRoomEntity br : bill.getBookingRooms()) {
                if (br == null) continue;

                BillResponseDTO.BillRoomDTO rDto = new BillResponseDTO.BillRoomDTO();
                rDto.setBookingRoomId(br.getId());

                if (br.getRoom() != null) {
                    rDto.setRoomNumber(br.getRoom().getRoomNumber());

                    if (br.getRoom().getRoomType() != null) {
                        rDto.setRoomType(br.getRoom().getRoomType().getName());
                        rDto.setRoomPrice(br.getRoom().getRoomType().getPrice());
                    } else {
                        rDto.setRoomType("Unknown");
                        rDto.setRoomPrice(0f);
                    }
                } else {
                    rDto.setRoomNumber(0);
                }

                String checkIn = (br.getActualCheckInTime() != null)
                        ? br.getActualCheckInTime().toString()
                        : (br.getContractCheckInTime() != null ? br.getContractCheckInTime().toString() : "");

                String checkOut = (br.getActualCheckOutTime() != null)
                        ? br.getActualCheckOutTime().toString()
                        : (br.getContractCheckOutTime() != null ? br.getContractCheckOutTime().toString() : "");

                rDto.setCheckIn(checkIn);
                rDto.setCheckOut(checkOut);

                List<BillResponseDTO.BillServiceDTO> serviceDTOs = new ArrayList<>();

                if (br.getBookingServices() != null && !br.getBookingServices().isEmpty()) {
                    for (BookingServiceEntity bs : br.getBookingServices()) {
                        if (bs != null && (bs.getStatus() == null || bs.getStatus() != 2)) {
                            if (bs.getService() != null) {
                                BillResponseDTO.BillServiceDTO sDto = new BillResponseDTO.BillServiceDTO();
                                sDto.setServiceName(bs.getService().getName());
                                sDto.setUnit(bs.getService().getUnit());
                                int qty = (bs.getQuantity() != null) ? bs.getQuantity() : 0;
                                float price = (bs.getPrice() != null) ? bs.getPrice() : 0f;
                                sDto.setQuantity(qty);
                                sDto.setPrice(price);
                                sDto.setTotalPrice((double) (qty * price));
                                serviceDTOs.add(sDto);
                            }
                        }
                    }
                }
                rDto.setServices(serviceDTOs);
                roomDTOs.add(rDto);
            }
        }
        dto.setBookingRooms(roomDTOs);
        return dto;
    }
}