package com.javaweb.service.impl;

import com.javaweb.model.entity.BlackListEntity;
import com.javaweb.repository.BlackListRepository;
import com.javaweb.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlackListServiceImpl implements BlacklistService {
    @Autowired
    private BlackListRepository blackListRepository;

    @Override
    public void resetBlackList(Integer customerId) {
        BlackListEntity blackList = blackListRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Khách hàng này chưa có trong sổ đen"));

        blackList.setCount(0); // Reset về 0
        blackListRepository.save(blackList);
    }

    @Override
    public boolean isBlackList(Integer customerId) {
        BlackListEntity blackList = blackListRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Khách hàng này chưa có trong sổ đen"));
        if (blackList.getCount() >= 3) return true;
        return false;
    }
}
