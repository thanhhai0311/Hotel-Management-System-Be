package com.javaweb.service.impl;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.dto.request.RoomStatusRequest;
import com.javaweb.model.dto.response.RoomStatusResponse;
import com.javaweb.model.entity.RoomStatusEntity;
import com.javaweb.repository.RoomStatusRepository;
import com.javaweb.service.RoomStatusService;

@Service
public class RoomStatusServiceImpl implements RoomStatusService {

    private final RoomStatusRepository roomStatusRepository;

    public RoomStatusServiceImpl(RoomStatusRepository roomStatusRepository) {
        this.roomStatusRepository = roomStatusRepository;
    }

    @Override
    public RoomStatusResponse create(RoomStatusRequest req) {
        if (roomStatusRepository.existsByNameIgnoreCase(req.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên trạng thái đã tồn tại");
        }
        RoomStatusEntity entity = new RoomStatusEntity();
        entity.setName(req.getName().trim());
        entity.setDetails(req.getDetails());
        RoomStatusEntity saved = roomStatusRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    public RoomStatusResponse update(Integer id, RoomStatusRequest req) {
        RoomStatusEntity entity = roomStatusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy trạng thái"));

        if (req.getName() != null) {
            roomStatusRepository.findByNameIgnoreCase(req.getName().trim()).ifPresent(existed -> {
                if (!Objects.equals(existed.getId(), id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên trạng thái đã tồn tại");
                }
            });
            entity.setName(req.getName().trim());
        }
        entity.setDetails(req.getDetails());

        RoomStatusEntity saved = roomStatusRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        RoomStatusEntity entity = roomStatusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy trạng thái"));

         if (entity.getRooms() != null && !entity.getRooms().isEmpty()) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể xóa: Trạng thái đang được sử dụng");
         }

        roomStatusRepository.delete(entity);
    }

    @Override
    public RoomStatusResponse getById(Integer id) {
        RoomStatusEntity entity = roomStatusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy trạng thái"));
        return toResponse(entity);
    }

    @Override
    public Page<RoomStatusResponse> getAll(Pageable pageable, String keyword) {
        Page<RoomStatusEntity> page = ((keyword == null || keyword.trim().isEmpty()))
                ? roomStatusRepository.findAll(pageable)
                : roomStatusRepository.findAll(pageable);
        return page.map(this::toResponse);
    }

    private RoomStatusResponse toResponse(RoomStatusEntity e) {
        return new RoomStatusResponse(e.getId(), e.getName(), e.getDetails());
    }
}
