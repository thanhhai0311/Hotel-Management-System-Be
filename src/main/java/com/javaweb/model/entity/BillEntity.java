package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bill")
public class BillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    private Float totalBeforeTax;
    private Float totalAfterTax;

    @ManyToOne
    @JoinColumn(name = "idPaymentMethod")
    private PaymentMethodEntity paymentMethod;

    @ManyToOne
    @JoinColumn(name = "idPaymentStatus")
    private PaymentStatusEntity paymentStatus;

    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private UserEntity customer;

    @OneToMany(mappedBy = "bill")
    private List<BookingRoomEntity> bookingRooms;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Float getTotalBeforeTax() {
		return totalBeforeTax;
	}

	public void setTotalBeforeTax(Float totalBeforeTax) {
		this.totalBeforeTax = totalBeforeTax;
	}

	public Float getTotalAfterTax() {
		return totalAfterTax;
	}

	public void setTotalAfterTax(Float totalAfterTax) {
		this.totalAfterTax = totalAfterTax;
	}

	public PaymentMethodEntity getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethodEntity paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public PaymentStatusEntity getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatusEntity paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public UserEntity getCustomer() {
		return customer;
	}

	public void setCustomer(UserEntity customer) {
		this.customer = customer;
	}

	public List<BookingRoomEntity> getBookingRooms() {
		return bookingRooms;
	}

	public void setBookingRooms(List<BookingRoomEntity> bookingRooms) {
		this.bookingRooms = bookingRooms;
	}


    
}
