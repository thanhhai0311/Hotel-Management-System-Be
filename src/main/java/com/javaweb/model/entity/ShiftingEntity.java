package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Shifting")
public class ShiftingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "details")
    private String details;

    @Temporal(TemporalType.DATE)
    @Column(name = "day")
    private Date day;

    @ManyToOne
    @JoinColumn(name = "idEmployee")
    private EmployeeEntity employee;

    @ManyToOne
    @JoinColumn(name = "idShift")
    private ShiftEntity shift;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public EmployeeEntity getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeEntity employee) {
		this.employee = employee;
	}

	public ShiftEntity getShift() {
		return shift;
	}

	public void setShift(ShiftEntity shift) {
		this.shift = shift;
	}

  
}
