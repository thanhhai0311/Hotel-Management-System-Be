package com.javaweb.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "ReviewImage")
public class ReviewImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String details;
    private String src;

    @ManyToOne
    @JoinColumn(name = "idReview")
    private ReviewEntity review;

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

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public ReviewEntity getReview() {
		return review;
	}

	public void setReview(ReviewEntity review) {
		this.review = review;
	}

    
}
