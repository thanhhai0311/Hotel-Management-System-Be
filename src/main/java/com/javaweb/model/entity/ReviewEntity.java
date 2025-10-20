package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Review")
public class ReviewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String details;
    private Integer star;

    @Temporal(TemporalType.TIMESTAMP)
    private Date day;

    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private CustomerEntity customer;

    @OneToMany(mappedBy = "review")
    private List<ReviewImageEntity> reviewImages;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Integer getStar() {
		return star;
	}

	public void setStar(Integer star) {
		this.star = star;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public CustomerEntity getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerEntity customer) {
		this.customer = customer;
	}

	public List<ReviewImageEntity> getReviewImages() {
		return reviewImages;
	}

	public void setReviewImages(List<ReviewImageEntity> reviewImages) {
		this.reviewImages = reviewImages;
	}


    
}
