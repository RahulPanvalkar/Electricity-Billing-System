package com.ebs.entities;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "consumer_details")
public class Consumer {
	@Id
	@Column(name = "consumer_num")
	private String consumerNum;
	@Column(name = "full_name", nullable = false)
	private String fullName;
	@Column(name = "mob_number", nullable = false, unique = true)
	private String mobNumber;
	@Column(name = "email_id", nullable = false, unique = true)
	private String emailId;
	@Column
	private String address;
	@Column(name = "conn_id")
	private String connId;
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

	public Consumer() { }

	public Consumer(String consumerNum, String fullName, String mob, String emailId, String address, String connId, Date addDate) {
		this.consumerNum = consumerNum;
		this.fullName = fullName;
		this.mobNumber = mob;
		this.emailId = emailId;
		this.address = address;
		this.connId = connId;
	}
	
	public Consumer(String fullName, String mob, String emailId, String address, String connId, Date addDate) {
		this.fullName = fullName;
		this.mobNumber = mob;
		this.emailId = emailId;
		this.address = address;
		this.connId = connId;
	}

	public Consumer(UserRegistration user) {
		this.fullName = user.getFirstName() + " " + user.getLastName();
		this.mobNumber = user.getMobNumber();
		this.emailId = user.getEmailId();
		this.address = user.getAddress();
	}
	
	public String getConsumerNum() {
		return consumerNum;
	}

	public void setConsumerNum(String consumerNum) {
		this.consumerNum = consumerNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String location) {
		this.address = location;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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

	public String getConnId() {
		return connId;
	}

	public void setConnId(String connId) {
		this.connId = connId;
	}

	public Date getAddDate() {
		return Date.valueOf(addDate);
	}

	public Date getUpdateDate() {
		return Date.valueOf(addDate);
	}


	@Override
	public String toString() {
		return String.format(
				"[consumerNum=%s, fullName=%s, mobileNo=%s, email=%s, address=%s, connId=%s, addDate=%s]\n",
				consumerNum, fullName, mobNumber, emailId, address, connId, addDate
		);
	}

}
