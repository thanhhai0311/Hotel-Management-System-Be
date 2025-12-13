package com.javaweb.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "phone")
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "identification")
    private String identification;

    @Column(name = "dob")
    private LocalDate dob;

    @OneToMany(mappedBy = "employee")
    private List<EvaluateEntity> evaluates;

    @OneToMany(mappedBy = "employee")
    private List<ShiftingEntity> shiftings;

    @OneToMany(mappedBy = "employee")
    private List<DoingServiceEntity> doingServices;

    @OneToMany(mappedBy = "customer")
    private List<ReviewEntity> reviews;

    @OneToMany(mappedBy = "customer")
    private List<BookingRoomEntity> bookingRooms;

    @OneToMany(mappedBy = "customer")
    private List<BillEntity> bills;

    @OneToMany(mappedBy = "customer")
    private List<NotifacationServiceEntity> notifacationServices;

    @OneToMany(mappedBy = "customer")
    private List<NotifacationRoomEntity> notifacationRooms;

    @OneToOne()
    @JoinColumn(name = "idAccount")
    private AccountEntity account;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public List<EvaluateEntity> getEvaluates() {
        return evaluates;
    }

    public void setEvaluates(List<EvaluateEntity> evaluates) {
        this.evaluates = evaluates;
    }

    public List<ShiftingEntity> getShiftings() {
        return shiftings;
    }

    public void setShiftings(List<ShiftingEntity> shiftings) {
        this.shiftings = shiftings;
    }

    public List<DoingServiceEntity> getDoingServices() {
        return doingServices;
    }

    public void setDoingServices(List<DoingServiceEntity> doingServices) {
        this.doingServices = doingServices;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public List<BookingRoomEntity> getBookingRooms() {
        return bookingRooms;
    }

    public void setBookingRooms(List<BookingRoomEntity> bookingRooms) {
        this.bookingRooms = bookingRooms;
    }

    public List<BillEntity> getBills() {
        return bills;
    }

    public void setBills(List<BillEntity> bills) {
        this.bills = bills;
    }

    public List<NotifacationServiceEntity> getNotifacationServices() {
        return notifacationServices;
    }

    public void setNotifacationServices(List<NotifacationServiceEntity> notifacationServices) {
        this.notifacationServices = notifacationServices;
    }

    public List<NotifacationRoomEntity> getNotifacationRooms() {
        return notifacationRooms;
    }

    public void setNotifacationRooms(List<NotifacationRoomEntity> notifacationRooms) {
        this.notifacationRooms = notifacationRooms;
    }

    public AccountEntity getAccount() {
        return account;
    }

    public void setAccount(AccountEntity account) {
        this.account = account;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }


}
