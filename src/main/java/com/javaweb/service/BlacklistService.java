package com.javaweb.service;

public interface BlacklistService {
    void resetBlackList(Integer customerId);

    boolean isBlackList(Integer customerId);
}
