package com.javaweb.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "blacklist")
public class BlackListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "count", nullable = false)
    private Integer count = 0;

    @OneToOne
    @JoinColumn(name = "idCustomer", unique = true, nullable = false)
    private UserEntity customer;

    @ManyToOne
    @JoinColumn(name = "idHotel", nullable = false)
    private HotelEntity hotel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public UserEntity getCustomer() {
        return customer;
    }

    public void setCustomer(UserEntity customer) {
        this.customer = customer;
    }

    public HotelEntity getHotel() {
        return hotel;
    }

    public void setHotel(HotelEntity hotel) {
        this.hotel = hotel;
    }
}
