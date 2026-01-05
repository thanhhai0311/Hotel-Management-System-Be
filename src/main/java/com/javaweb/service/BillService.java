package com.javaweb.service;

import com.javaweb.model.dto.BillDTO.BillResponseDTO;

import java.util.List;

public interface BillService {
    List<BillResponseDTO> getAllBills(Integer page, Integer limit);

    BillResponseDTO getBillById(Integer id);

    BillResponseDTO updateStatusBill(Integer id);
}
