package com.javaweb.model.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bookingservice")
public class BookingServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startTime;

    //    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endTime;

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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
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
