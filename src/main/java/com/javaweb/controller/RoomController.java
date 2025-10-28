package com.javaweb.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

import com.javaweb.model.dto.RoomDTO.RoomCreateDTO;
import com.javaweb.model.dto.RoomDTO.RoomResponseDTO;
import com.javaweb.model.dto.RoomDTO.RoomUpdateDTO;
import com.javaweb.model.response.ApiResponse;
import com.javaweb.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

	@Autowired
	private RoomService roomService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping(value = "/create", consumes = "multipart/form-data")
	public ResponseEntity<?> createRoom(@ModelAttribute RoomCreateDTO dto) {
		try {
			RoomResponseDTO response = roomService.createRoom(dto);
			ApiResponse<RoomResponseDTO> res = new ApiResponse<>(true, HttpStatus.CREATED.value(),
					"Tạo phòng thành công", response, "api/rooms/create");
			return ResponseEntity.status(HttpStatus.CREATED).body(res);
		} catch (Exception e) {
			e.printStackTrace();

			Map<String, Object> errorBody = new HashMap<>();
			errorBody.put("success", false);
			errorBody.put("status", 500);
			errorBody.put("message", e.getMessage());

			return ResponseEntity.status(500).body(errorBody);
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> getAllRooms(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			ApiResponse<Map<String, Object>> res = new ApiResponse<Map<String, Object>>(true, HttpStatus.OK.value(),
					"Lấy danh sách phòng thành công", roomService.getAllRooms(page, size), "api/rooms/getAll");
			return ResponseEntity.ok(res);
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> body = new HashMap<>();
			body.put("success", false);
			body.put("message", e.getMessage());
			return ResponseEntity.status(500).body(body);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getRoomById(@PathVariable Integer id) {
//	    try {
	        RoomResponseDTO room = roomService.getRoomById(id);
//	        if (room != null) {
	        	ApiResponse<RoomResponseDTO> res = new ApiResponse<RoomResponseDTO>(true, HttpStatus.OK.value(), "Lấy thông tin thành công", room, "api/rooms/get");
//	            Map<String, Object> response = new HashMap<>();
//	            response.put("success", true);
//	            response.put("status", 200);
//	            response.put("message", "Lấy thông tin phòng thành công");
//	            response.put("data", room);
	            return ResponseEntity.ok(res);
	            
//	        } else {
//	            Map<String, Object> response = new HashMap<>();
//	            response.put("success", false);
//	            response.put("status", 404);
//	            response.put("message", "Không tìm thấy phòng có id = " + id);
//	            return ResponseEntity.status(404).body(response);
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        Map<String, Object> error = new HashMap<>();
//	        error.put("success", false);
//	        error.put("status", 500);
//	        error.put("message", e.getMessage());
//	        return ResponseEntity.status(500).body(error);
//	    }
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "/update/{id}", consumes = "multipart/form-data")
	public ResponseEntity<?> updateRoom(
	        @PathVariable Integer id,
	        @ModelAttribute RoomUpdateDTO dto) throws IOException {
//	    try {
	        RoomResponseDTO updatedRoom = roomService.updateRoom(id, dto);
	        ApiResponse<RoomResponseDTO> res = new ApiResponse<>(
	            true,
	            HttpStatus.OK.value(),
	            "Cập nhật phòng thành công",
	            updatedRoom,
	            "api/rooms/update/" + id
	        );
	        return ResponseEntity.ok(res);
//	    } catch (RuntimeException e) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//	            new ApiResponse<>(false, HttpStatus.NOT_FOUND.value(), e.getMessage(), null, "api/rooms/update/" + id)
//	        );
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
//	            new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi cập nhật phòng: " + e.getMessage(), null, "api/rooms/update/" + id)
//	        );
//	    }
	}	
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteRoom(@PathVariable Integer id) {
	    roomService.deleteRoom(id);
	    ApiResponse<Void> response = new ApiResponse<>(
	            true,
	            HttpStatus.OK.value(),
	            "Xóa phòng thành công",
	            null,
	            "api/rooms/" + id
	    );
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping("/search")
	public ResponseEntity<?> searchRooms(
	        @RequestParam(required = false) Integer roomNumber,
	        @RequestParam(required = false) Integer bedCount,
	        @RequestParam(required = false) Integer maxOccupancy,
	        @RequestParam(required = false) Float minPrice,
	        @RequestParam(required = false) Float maxPrice,
	        @RequestParam(required = false) Integer hotelId,
	        @RequestParam(required = false) Integer roomTypeId,
	        @RequestParam(required = false) Integer roomStatusId,
	        @RequestParam(required = false) String details,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "10") int size
	) {
	    Page<RoomResponseDTO> result = roomService.searchRooms(
	            roomNumber, bedCount, maxOccupancy, minPrice, maxPrice,
	            hotelId, roomTypeId, roomStatusId, details, page, size);

	    ApiResponse<Page<RoomResponseDTO>> response = new ApiResponse<>(
	            true,
	            HttpStatus.OK.value(),
	            "Tìm kiếm phòng thành công",
	            result,
	            "api/rooms/search"
	    );

	    return ResponseEntity.ok(response);
	}

}
