package com.javaweb.model.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "bookingservice")
public class BookingServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @ManyToOne
    @JoinColumn(name = "idBookingRoom")
    private BookingRoomEntity bookingRoom;

    @ManyToOne
    @JoinColumn(name = "idService")
    private ServiceEntity service;

    @OneToMany(mappedBy = "bookingService")
    private List<NotifacationServiceEntity> notifacationServices;

    @OneToMany(mappedBy = "bookingService")
    private List<DoingServiceEntity> doingServices;
    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public BookingRoomEntity getBookingRoom() {
		return bookingRoom;
	}

	public void setBookingRoom(BookingRoomEntity bookingRoom) {
		this.bookingRoom = bookingRoom;
	}

	public ServiceEntity getService() {
		return service;
	}

	public void setService(ServiceEntity service) {
		this.service = service;
	}

	public List<NotifacationServiceEntity> getNotifacationServices() {
		return notifacationServices;
	}

	public void setNotifacationServices(List<NotifacationServiceEntity> notifacationServices) {
		this.notifacationServices = notifacationServices;
	}

	public List<DoingServiceEntity> getDoingServices() {
		return doingServices;
	}

	public void setDoingServices(List<DoingServiceEntity> doingServices) {
		this.doingServices = doingServices;
	}    
}
