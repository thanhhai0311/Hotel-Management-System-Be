package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NotifacationRoom")
public class NotifacationRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date notiTime;

    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "idBookingRoom")
    private BookingRoomEntity bookingRoom;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getNotiTime() {
		return notiTime;
	}

	public void setNotiTime(Date notiTime) {
		this.notiTime = notiTime;
	}

	public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	public BookingRoomEntity getBookingRoom() {
		return bookingRoom;
	}

	public void setBookingRoom(BookingRoomEntity bookingRoom) {
		this.bookingRoom = bookingRoom;
	}

    
}
