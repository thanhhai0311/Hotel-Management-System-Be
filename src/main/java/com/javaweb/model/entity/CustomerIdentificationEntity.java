package com.javaweb.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customeridentification")
public class CustomerIdentificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "idUser")
    private UserEntity customer;

    @Column(name = "identificationImage")
    private String identificationImage;

    @Column(name = "expiryDate")
    private LocalDateTime expiryDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserEntity getCustomer() {
        return customer;
    }

    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }

    public String getIdentificationImage() {
        return identificationImage;
    }

    public void setIdentificationImage(String identificationImage) {
        this.identificationImage = identificationImage;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
}
