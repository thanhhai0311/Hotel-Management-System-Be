package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Customer")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "address")
    private String address;

    @Temporal(TemporalType.DATE)
    @Column(name = "dob")
    private Date dob;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

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

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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


    
}
