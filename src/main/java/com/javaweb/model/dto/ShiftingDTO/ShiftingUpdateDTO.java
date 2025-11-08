package com.javaweb.model.dto.ShiftingDTO;

import java.time.LocalDate;

public class ShiftingUpdateDTO {
	private String details;
	private LocalDate day;
	private Integer idEmployee;
	private Integer idShift;

	// Getters & Setters
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	public Integer getIdEmployee() {
		return idEmployee;
	}

	public void setIdEmployee(Integer idEmployee) {
		this.idEmployee = idEmployee;
	}

	public Integer getIdShift() {
		return idShift;
	}

	public void setIdShift(Integer idShift) {
		this.idShift = idShift;
	}

}
