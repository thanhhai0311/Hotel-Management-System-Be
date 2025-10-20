package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NotifacationService")
public class NotifacationServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date notiTime;

    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "idBookingService")
    private BookingServiceEntity bookingService;

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

	public BookingServiceEntity getBookingService() {
		return bookingService;
	}

	public void setBookingService(BookingServiceEntity bookingService) {
		this.bookingService = bookingService;
	}

    
}
