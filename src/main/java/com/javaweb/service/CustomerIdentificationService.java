package com.javaweb.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CustomerIdentificationService {
    void processCheckInImage(Integer customerId, MultipartFile file) throws IOException;

    void processCheckOutExpiry(Integer customerId);

    void cleanupExpiredImages();
}
