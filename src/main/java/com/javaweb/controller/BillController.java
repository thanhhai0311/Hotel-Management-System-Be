package com.javaweb.controller;

import com.javaweb.model.dto.BillDTO.BillResponseDTO;
import com.javaweb.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping
    public ResponseEntity<List<BillResponseDTO>> getAllBills(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit
    ) {
        List<BillResponseDTO> result = billService.getAllBills(page, limit);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillResponseDTO> getBillById(@PathVariable Integer id) {
        BillResponseDTO result = billService.getBillById(id);
        return ResponseEntity.ok(result);
    }
}