package com.javaweb.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.javaweb.model.entity.RoomEntity;
import com.javaweb.model.entity.RoomImageEntity;
import com.javaweb.repository.RoomImageRepository;
import com.javaweb.repository.RoomRepository;
import com.javaweb.service.RoomService;
import com.javaweb.service.S3Service;

@Service
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final RoomImageRepository roomImageRepository;
    private final S3Service s3Service;

    public RoomServiceImpl(RoomRepository roomRepository, RoomImageRepository roomImageRepository, S3Service s3Service) {
        this.roomRepository = roomRepository;
        this.roomImageRepository = roomImageRepository;
        this.s3Service = s3Service;
    }

    @Override
    public RoomEntity createRoom(RoomEntity room, List<MultipartFile> images) throws IOException {
        RoomEntity savedRoom = roomRepository.save(room);
        List<RoomImageEntity> imageEntities = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            for (MultipartFile file : images) {
                String imageUrl = s3Service.uploadFile(file);
                RoomImageEntity img = new RoomImageEntity();
                img.setSrc(imageUrl);
                img.setRoom(savedRoom);
                imageEntities.add(img);
            }
            roomImageRepository.saveAll(imageEntities);
            savedRoom.setRoomImage(imageEntities);
        }

        return savedRoom;
    }

    @Override
    public RoomEntity updateRoom(Integer id, RoomEntity newData) {
        RoomEntity room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        room.setRoomNumber(newData.getRoomNumber());
        room.setBedCount(newData.getBedCount());
        room.setMaxOccupancy(newData.getMaxOccupancy());
        room.setPrice(newData.getPrice());
        room.setDetails(newData.getDetails());

        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Integer id) {
        RoomEntity room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getRoomImage() != null) {
            for (RoomImageEntity img : room.getRoomImage()) {
                s3Service.deleteFile(img.getSrc());
            }
            roomImageRepository.deleteAll(room.getRoomImage());
        }
        roomRepository.delete(room);
    }

    @Override
    public RoomEntity getRoomById(Integer id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
    }

    @Override
    public List<RoomEntity> getAllRooms() {
        return roomRepository.findAll();
    }
}
