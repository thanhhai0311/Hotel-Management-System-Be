package com.javaweb.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.dto.RoomTypeDTO.RoomTypeCreateDTO;
import com.javaweb.model.dto.RoomTypeDTO.RoomTypeResponseDTO;
import com.javaweb.model.dto.RoomTypeDTO.RoomTypeUpdateDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.RoomTypeService;

@RestController
@RequestMapping("/api/roomtypes")
public class RoomTypeController {

	@Autowired
	private RoomTypeService roomTypeService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<RoomTypeResponseDTO>> createRoomType(@ModelAttribute RoomTypeCreateDTO dto)
			throws IOException {
		RoomTypeResponseDTO response = roomTypeService.createRoomType(dto);
		ApiResponse<RoomTypeResponseDTO> res = new ApiResponse<>(true, HttpStatus.CREATED.value(),
				"Tạo loại phòng thành công", response, "api/roomtypes/create");
		return ResponseEntity.status(HttpStatus.CREATED).body(res);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<ApiResponse<RoomTypeResponseDTO>> updateRoomType(@PathVariable Integer id,
			@ModelAttribute RoomTypeUpdateDTO dto) throws IOException {

		RoomTypeResponseDTO updated = roomTypeService.updateRoomType(id, dto);

		ApiResponse<RoomTypeResponseDTO> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Cập nhật loại phòng thành công", updated, "api/roomtypes/update/" + id);

		return ResponseEntity.ok(res);
	}

	@GetMapping("/getAll")
	public ResponseEntity<ApiResponse<Map<String, Object>>> getAllRoomTypes(
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
		Map<String, Object> data = roomTypeService.getAllRoomTypes(page, size);
		ApiResponse<Map<String, Object>> res = new ApiResponse<>(true, 200, "Lấy danh sách loại phòng thành công", data,
				"api/roomtypes/getAll");
		return ResponseEntity.ok(res);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteRoomType(@PathVariable Integer id) {
		roomTypeService.deleteRoomType(id);
		ApiResponse<Void> res = new ApiResponse<>(true, HttpStatus.OK.value(),
				"Xóa loại phòng thành công (soft delete)", null, "api/roomtypes/" + id);
		return ResponseEntity.ok(res);
	}

	@GetMapping("/search")
	public ResponseEntity<?> searchRoomTypes(@RequestParam(required = false) String name,
			@RequestParam(required = false) Float minPrice, @RequestParam(required = false) Float maxPrice,
			@RequestParam(required = false) Integer bedCount, @RequestParam(required = false) Integer maxOccupancy,
			@RequestParam(required = false) Float minArea, @RequestParam(required = false) Float maxArea,
			@RequestParam(required = false) Boolean isSmoking,
			@RequestParam(required = false) Boolean isAirConditioning, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size) {
		Map<String, Object> result = roomTypeService.searchRoomTypes(
	            name, minPrice, maxPrice, bedCount, maxOccupancy,
	            minArea, maxArea, isSmoking, isAirConditioning,
	            page, size
	    );

	    ApiResponse<Map<String, Object>> response = new ApiResponse<>(
	            true,
	            HttpStatus.OK.value(),
	            "Tìm kiếm loại phòng thành công",
	            result,
	            "api/roomtypes/search"
	    );
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<RoomTypeResponseDTO>> getRoomTypeById(@PathVariable Integer id) {
	    RoomTypeResponseDTO roomType = roomTypeService.getRoomTypeById(id);
	    ApiResponse<RoomTypeResponseDTO> response = new ApiResponse<>(
	            true,
	            HttpStatus.OK.value(),
	            "Lấy thông tin loại phòng thành công",
	            roomType,
	            "api/roomtypes/" + id
	    );
	    return ResponseEntity.ok(response);
	}

}
