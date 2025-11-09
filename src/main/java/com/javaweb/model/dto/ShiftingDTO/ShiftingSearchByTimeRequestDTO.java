package com.javaweb.model.dto.ShiftingDTO;

import java.time.LocalDateTime;

public class ShiftingSearchByTimeRequestDTO {
    private LocalDateTime datetime;

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
}
