package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Bill")
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
    private CustomerEntity customer;

    @OneToMany(mappedBy = "bill")
    private List<BookingServiceEntity> bookingServices;

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

	public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	public List<BookingServiceEntity> getBookingServices() {
		return bookingServices;
	}

	public void setBookingServices(List<BookingServiceEntity> bookingServices) {
		this.bookingServices = bookingServices;
	}

    
}
