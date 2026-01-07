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

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "gender")
    private String gender;

    @Column(name = "address")
    private String address;

    @Column(name = "identification", nullable = false)
    private String identification;

    @Column(name = "dob")
    private LocalDate dob;

    @OneToMany(mappedBy = "employee")
    private List<ShiftingEntity> shiftings;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviews;

    @OneToMany(mappedBy = "customer")
    private List<BookingRoomEntity> bookingRooms;

    @OneToMany(mappedBy = "customer")
    private List<BillEntity> bills;

    @OneToOne()
    @JoinColumn(name = "idAccount")
    private AccountEntity account;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private CustomerIdentificationEntity customerIdentification;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private BlackListEntity blackList;

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

    public List<ShiftingEntity> getShiftings() {
        return shiftings;
    }

    public void setShiftings(List<ShiftingEntity> shiftings) {
        this.shiftings = shiftings;
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

    public CustomerIdentificationEntity getCustomerIdentification() {
        return customerIdentification;
    }

    public void setCustomerIdentification(CustomerIdentificationEntity customerIdentification) {
        this.customerIdentification = customerIdentification;
    }

    public BlackListEntity getBlackList() {
        return blackList;
    }

    public void setBlackList(BlackListEntity blackList) {
        this.blackList = blackList;
    }
}
