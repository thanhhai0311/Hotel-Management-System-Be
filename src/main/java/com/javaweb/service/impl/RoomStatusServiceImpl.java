package com.javaweb.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.javaweb.model.dto.RoomStatusDTO.RoomStatusRequest;
import com.javaweb.model.dto.RoomStatusDTO.RoomStatusResponse;
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
    public Page<RoomStatusResponse> getAll(Pageable pageable) {
        try {
            // 🔹 Nếu không truyền pageable hoặc không phân trang → lấy toàn bộ
            if (pageable == null || pageable.isUnpaged()) {
                List<RoomStatusEntity> list = roomStatusRepository.findAll(Sort.by("id").ascending());

                List<RoomStatusResponse> dtos = list.stream()
                        .map(this::toResponse)
                        .collect(Collectors.toList());

                // Trả về PageImpl giả để thống nhất kiểu trả về
                return new PageImpl<>(dtos);
            }

            // 🔹 Nếu có pageable → phân trang bình thường
            Page<RoomStatusEntity> page = roomStatusRepository.findAll(pageable);
            return page.map(this::toResponse);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Lỗi khi lấy danh sách trạng thái phòng: " + e.getMessage()
            );
        }
    }


    private RoomStatusResponse toResponse(RoomStatusEntity e) {
        return new RoomStatusResponse(e.getId(), e.getName(), e.getDetails());
    }
}
