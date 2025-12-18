package com.javaweb.scheduler;

import com.javaweb.service.CustomerIdentificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IdentificationCleanupScheduler {

    @Autowired
    private CustomerIdentificationService identificationService;

    // Chạy lúc 02:00 sáng mỗi ngày
    @Scheduled(cron = "0 0 2 * * ?")
    public void runCleanup() {
        identificationService.cleanupExpiredImages();
    }
}