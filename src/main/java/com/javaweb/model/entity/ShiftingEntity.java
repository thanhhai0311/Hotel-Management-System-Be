package com.javaweb.model.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "shifting")
public class ShiftingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "details")
    private String details;

    @Column(name = "day", nullable = false)
    private LocalDate day;

    @ManyToOne
    @JoinColumn(name = "idEmployee", nullable = false)
    private UserEntity employee;

    @ManyToOne
    @JoinColumn(name = "idShift", nullable = false)
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

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public UserEntity getEmployee() {
        return employee;
    }

    public void setEmployee(UserEntity employee) {
        this.employee = employee;
    }

    public ShiftEntity getShift() {
        return shift;
    }

    public void setShift(ShiftEntity shift) {
        this.shift = shift;
    }


}
