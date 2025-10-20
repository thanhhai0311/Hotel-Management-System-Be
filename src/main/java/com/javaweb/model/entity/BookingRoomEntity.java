package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BookingRoom")
public class BookingRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date contractCheckInTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date contractCheckOutTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date actualCheckInTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date actualCheckOutTime;

    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private CustomerEntity customer;

    @OneToOne
    @JoinColumn(name = "idRoomPromotion")
    private RoomPromotionEntity roomPromotion;

    @ManyToOne
    @JoinColumn(name = "idRoom")
    private RoomEntity room;

    @ManyToOne
    @JoinColumn(name = "idEmployee")
    private EmployeeEntity employee;

    @OneToMany(mappedBy = "bookingRoom")
    private List<BookingServiceEntity> bookingServices;

    @OneToMany(mappedBy = "bookingRoom")
    private List<NotifacationRoomEntity> notifacationRooms;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getContractCheckInTime() {
		return contractCheckInTime;
	}

	public void setContractCheckInTime(Date contractCheckInTime) {
		this.contractCheckInTime = contractCheckInTime;
	}

	public Date getContractCheckOutTime() {
		return contractCheckOutTime;
	}

	public void setContractCheckOutTime(Date contractCheckOutTime) {
		this.contractCheckOutTime = contractCheckOutTime;
	}

	public Date getActualCheckInTime() {
		return actualCheckInTime;
	}

	public void setActualCheckInTime(Date actualCheckInTime) {
		this.actualCheckInTime = actualCheckInTime;
	}

	public Date getActualCheckOutTime() {
		return actualCheckOutTime;
	}

	public void setActualCheckOutTime(Date actualCheckOutTime) {
		this.actualCheckOutTime = actualCheckOutTime;
	}

	public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	public RoomPromotionEntity getRoomPromotion() {
		return roomPromotion;
	}

	public void setRoomPromotion(RoomPromotionEntity roomPromotion) {
		this.roomPromotion = roomPromotion;
	}

	public RoomEntity getRoom() {
		return room;
	}

	public void setRoom(RoomEntity room) {
		this.room = room;
	}

	public EmployeeEntity getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeEntity employee) {
		this.employee = employee;
	}

	public List<BookingServiceEntity> getBookingServices() {
		return bookingServices;
	}

	public void setBookingServices(List<BookingServiceEntity> bookingServices) {
		this.bookingServices = bookingServices;
	}

	public List<NotifacationRoomEntity> getNotifacationRooms() {
		return notifacationRooms;
	}

	public void setNotifacationRooms(List<NotifacationRoomEntity> notifacationRooms) {
		this.notifacationRooms = notifacationRooms;
	}

    
}
