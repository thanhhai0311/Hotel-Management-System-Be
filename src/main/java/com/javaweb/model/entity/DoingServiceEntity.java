package com.javaweb.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "DoingService")
public class DoingServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String status;
    private String details;

    @ManyToOne
    @JoinColumn(name = "idBookingService")
    private BookingServiceEntity bookingService;

    @ManyToOne
    @JoinColumn(name = "idEmployee")
    private EmployeeEntity employee;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public BookingServiceEntity getBookingService() {
		return bookingService;
	}

	public void setBookingService(BookingServiceEntity bookingService) {
		this.bookingService = bookingService;
	}

	public EmployeeEntity getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeEntity employee) {
		this.employee = employee;
	}

    
}
