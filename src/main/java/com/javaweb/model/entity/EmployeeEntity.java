package com.javaweb.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Employee")
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "gender")
    private String gender;
    
    @Column(name = "address")
    private String address;
    
    @Temporal(TemporalType.DATE)
	@Column(name = "dob")
	private Date dob;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "idRole")
    private RoleEntity role;
    
    @OneToMany(mappedBy = "employee")
    private List<EvaluateEntity> evaluates;
    
    @OneToMany(mappedBy = "employee")
    private List<ShiftingEntity> shiftings;
    
    @OneToMany(mappedBy = "employee")
    private List<DoingServiceEntity> doingServices;
    
    

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public List<EvaluateEntity> getEvaluates() {
		return evaluates;
	}

	public void setEvaluates(List<EvaluateEntity> evaluates) {
		this.evaluates = evaluates;
	}

	public List<ShiftingEntity> getShiftings() {
		return shiftings;
	}

	public void setShiftings(List<ShiftingEntity> shiftings) {
		this.shiftings = shiftings;
	}

	public List<DoingServiceEntity> getDoingServices() {
		return doingServices;
	}

	public void setDoingServices(List<DoingServiceEntity> doingServices) {
		this.doingServices = doingServices;
	}

	
    
}
