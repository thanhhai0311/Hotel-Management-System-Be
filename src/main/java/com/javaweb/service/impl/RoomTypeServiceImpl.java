package com.javaweb.service.impl;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.dto.request.RoomTypeRequest;
import com.javaweb.model.dto.response.RoomTypeResponse;
import com.javaweb.model.entity.RoomTypeEntity;
import com.javaweb.repository.RoomTypeRepository;
import com.javaweb.service.RoomTypeService;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeServiceImpl(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public RoomTypeResponse create(RoomTypeRequest req) {
        if (roomTypeRepository.existsByNameIgnoreCase(req.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên loại phòng đã tồn tại");
        }
        RoomTypeEntity entity = new RoomTypeEntity();
        entity.setName(req.getName().trim());
        entity.setDetails(req.getDetails());
        RoomTypeEntity saved = roomTypeRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    public RoomTypeResponse update(Integer id, RoomTypeRequest req) {
        RoomTypeEntity entity = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng"));

        // Check duplicate name (ignore same record)
        if (req.getName() != null) {
            roomTypeRepository.findByNameIgnoreCase(req.getName().trim()).ifPresent(existed -> {
                if (!Objects.equals(existed.getId(), id)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Tên loại phòng đã tồn tại");
                }
            });
            entity.setName(req.getName().trim());
        }
        entity.setDetails(req.getDetails());

        RoomTypeEntity saved = roomTypeRepository.save(entity);
        return toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        RoomTypeEntity entity = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng"));

        // Nếu có ràng buộc (rooms) có thể kiểm tra trước khi xóa
        // if (entity.getRooms() != null && !entity.getRooms().isEmpty()) {
        //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể xóa: Loại phòng đang được sử dụng");
        // }

        roomTypeRepository.delete(entity);
    }

    @Override
    public RoomTypeResponse getById(Integer id) {
        RoomTypeEntity entity = roomTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy loại phòng"));
        return toResponse(entity);
    }

    @Override
    public Page<RoomTypeResponse> getAll(Pageable pageable, String keyword) {
        Page<RoomTypeEntity> page = ((keyword == null || keyword.trim().isEmpty()))
                ? roomTypeRepository.findAll(pageable)
                : roomTypeRepository.findAll(pageable) // Simplest: filter ở controller/service nếu muốn
                  .map(e -> e); // (Có thể thay bằng Specification để filter DB-side)
        return page.map(this::toResponse);
    }

    private RoomTypeResponse toResponse(RoomTypeEntity e) {
        return new RoomTypeResponse(e.getId(), e.getName(), e.getDetails());
    }
}
