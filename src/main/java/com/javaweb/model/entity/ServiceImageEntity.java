package com.javaweb.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "serviceimage")
public class ServiceImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String details;

    @Column(nullable = false)
    private String src;

    @ManyToOne
    @JoinColumn(name = "idService", nullable = false)
    private ServiceEntity service;

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

    public ServiceEntity getService() {
        return service;
    }

    public void setService(ServiceEntity service) {
        this.service = service;
    }


}
