package com.javaweb.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.javaweb.model.dto.request.RoomTypeRequest;
import com.javaweb.model.dto.response.RoomTypeResponse;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.RoomTypeService;

@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {

    private final RoomTypeService roomTypeService;

    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<RoomTypeResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String keyword) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Page<RoomTypeResponse> data = roomTypeService.getAll(PageRequest.of(page, size, sort), keyword);
        ApiResponse<Page<RoomTypeResponse>> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Lấy danh sách thành công", data, "/api/room-types");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomTypeResponse>> getById(@PathVariable Integer id) {
        RoomTypeResponse data = roomTypeService.getById(id);
        ApiResponse<RoomTypeResponse> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Lấy chi tiết thành công", data, "/api/room-types/" + id);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<RoomTypeResponse>> create(@Valid @RequestBody RoomTypeRequest req) {
        RoomTypeResponse data = roomTypeService.create(req);
        ApiResponse<RoomTypeResponse> res = new ApiResponse<>(true, HttpStatus.CREATED.value(), "Tạo loại phòng thành công", data, "/api/room-types");
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoomTypeResponse>> update(@PathVariable Integer id,
                                                                @Valid @RequestBody RoomTypeRequest req) {
        RoomTypeResponse data = roomTypeService.update(id, req);
        ApiResponse<RoomTypeResponse> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Cập nhật loại phòng thành công", data, "/api/room-types/" + id);
        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        roomTypeService.delete(id);
        ApiResponse<Object> res = new ApiResponse<>(true, HttpStatus.OK.value(), "Xóa loại phòng thành công", null, "/api/room-types/" + id);
        return ResponseEntity.ok(res);
    }
}
