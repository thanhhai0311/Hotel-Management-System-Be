package com.javaweb.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.javaweb.model.dto.request.RoomStatusRequest;
import com.javaweb.model.dto.response.RoomStatusResponse;

public interface RoomStatusService {
    RoomStatusResponse create(RoomStatusRequest req);
    RoomStatusResponse update(Integer id, RoomStatusRequest req);
    void delete(Integer id);
    RoomStatusResponse getById(Integer id);
    Page<RoomStatusResponse> getAll(Pageable pageable, String keyword);
}
