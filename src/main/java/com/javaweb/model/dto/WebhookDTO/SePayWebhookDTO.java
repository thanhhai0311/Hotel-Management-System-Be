package com.javaweb.model.dto.WebhookDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SePayWebhookDTO {
    private Long id;
    private String gateway;
    @JsonProperty("transactionDate")
    private String transactionDate;
    @JsonProperty("transferType")
    private String transferType; // "in" hoặc "out"
    @JsonProperty("transferAmount")
    private Float transferAmount;
    @JsonProperty("content")
    private String content; // Nội dung chuyển khoản
    @JsonProperty("accountNumber")
    private String accountNumber;
}