package com.javaweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.javaweb.model.dto.request.RoomTypeRequest;
import com.javaweb.model.dto.response.RoomTypeResponse;

public interface RoomTypeService {
    RoomTypeResponse create(RoomTypeRequest req);
    RoomTypeResponse update(Integer id, RoomTypeRequest req);
    void delete(Integer id);
    RoomTypeResponse getById(Integer id);
    Page<RoomTypeResponse> getAll(Pageable pageable, String keyword);
}
