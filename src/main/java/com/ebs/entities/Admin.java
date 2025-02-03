package com.ebs.entities;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "admin_details")
public class Admin{

	@Id
	private String id;
	@Column(name = "first_name", nullable = false)
	private String firstName;
	@Column(name = "last_name", nullable = false)
	private String lastName;
	@Column(name = "mob_number", nullable = false, unique = true)
	private String mobNumber;
	@Column(name = "email_id", nullable = false, unique = true)
	private String emailId;
	@Column(nullable = false)
	private String address;
	@Column(name = "add_date", updatable = false)
	private LocalDate addDate;

	@Column(name = "update_date")
	private LocalDate updateDate;

	@PrePersist
	protected void onCreate() {
		addDate = LocalDate.now();
		updateDate = LocalDate.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updateDate = LocalDate.now();
	}

	public Admin() {	}

	public Admin(String id, String firstName, String lastName, String mobNumber, String emailId, String address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobNumber = mobNumber;
		this.emailId = emailId;
		this.address = address;
	}

	/*public Admin(UserRegistration user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.mobNumber = user.getMobNumber();
		this.emailId = user.getEmailId();
		this.address = user.getAddress();
	}*/

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobNumber() {
		return mobNumber;
	}

	public void setMobNumber(String mobNumber) {
		this.mobNumber = mobNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getAddDate() {
		return Date.valueOf(addDate);
	}

	public Date getUpdateDate() {
		return Date.valueOf(updateDate);
	}

	@Override
	public String toString() {
		return String.format("id : %s, name : %s, email : %s",id,firstName+" "+lastName,emailId);
	}
}