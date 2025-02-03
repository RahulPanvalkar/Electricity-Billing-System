package com.ebs.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {
	@Id
	@Column(name = "user_id")
	private String userId;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "password", nullable = false)
	private String password;
	@Column(name = "email_id", nullable = false, unique = true)
	private String emailId;
	@Column(name = "mob_number", nullable = false, unique = true)
	private String mobNumber;
	@Column(name = "user_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private UserType userType;
	@Column(name = "user_code", nullable = false, unique = true)
	private String userCode;
	@Column
	private String address;
	@Column(name = "ver_code")
	private String verCode;
	@Column(name = "expires_at")
	private Timestamp expiresAt;
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

	public User() {}


	public User(Consumer consumer) {
		this.name = consumer.getFullName();
		this.emailId = consumer.getEmailId();
		this.mobNumber = consumer.getMobNumber();
		this.userType = UserType.C;
		this.userCode = consumer.getConsumerNum();
		this.address = consumer.getAddress();
	}


	public User(Admin admin, String password) {
		this.name = admin.getFirstName() + " " + admin.getLastName();
		this.password = password;
		this.emailId = admin.getEmailId();
		this.mobNumber = admin.getMobNumber();
		this.userType = UserType.A;
		this.userCode = admin.getId();
		this.address = admin.getAddress();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobNumber() {
		return mobNumber;
	}

	public void setMobNumber(String mobNumber) {
		this.mobNumber = mobNumber;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVerCode() {
		return verCode;
	}

	public void setVerCode(String verCode) {
		this.verCode = verCode;
	}

	public Timestamp getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Timestamp expiresAt) {
		this.expiresAt = expiresAt;
	}

	public Date getAddDate() {
		return Date.valueOf(addDate);
	}

	public Date getUpdateDate() {
		return Date.valueOf(updateDate);
	}

	@Override
	public String toString() {
		return String.format(
				"User : [userId=%s, Name=%s, userCode=%s, email=%s, verCode=%s]",
				userId, name, userCode, emailId, verCode
		);
	}
	
}
