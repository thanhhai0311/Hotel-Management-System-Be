package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BookingService")
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

    @ManyToOne
    @JoinColumn(name = "idBill")
    private BillEntity bill;

    @OneToMany(mappedBy = "bookingService")
    private List<NotifacationServiceEntity> notifacationServices;

    @OneToMany(mappedBy = "bookingService")
    private List<DoingServiceEntity> doingServices;
    
    @OneToOne()
    @JoinColumn(name = "idServicePromotion")
    private ServicePromotionEntity servicePromotion;

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

	public BillEntity getBill() {
		return bill;
	}

	public void setBill(BillEntity bill) {
		this.bill = bill;
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

	public ServicePromotionEntity getServicePromotion() {
		return servicePromotion;
	}

	public void setServicePromotion(ServicePromotionEntity servicePromotion) {
		this.servicePromotion = servicePromotion;
	}

    
}
