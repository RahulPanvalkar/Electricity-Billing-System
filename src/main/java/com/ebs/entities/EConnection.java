package com.ebs.entities;

import javax.persistence.*;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "connection_details")
public class EConnection {

	@Id
	@Column(name = "conn_id")
	private String connId;
	@Column(name = "consumer_num", nullable = false, unique = true)
	private String consumerNum;
	@Column(name = "meter_num", nullable = false, unique = true)
	private String meterNum;
	@Column(name = "full_name", nullable = false)
	private String fullName;
	@Column(name = "mob_number", nullable = false)
	private String mobNumber;
	private String address;
	@Column(name = "start_date", nullable = false)
	private Date startDate;
	@Column(nullable = false)
	private String type;
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
	
	public EConnection() {}

	public EConnection(String consumerNum, String meterNum, String fullName, String mobNumber, String address, Date startDate,
                       String type) {
		this.consumerNum = consumerNum;
		this.fullName=fullName;
		this.meterNum = meterNum;
		this.mobNumber = mobNumber;
		this.address = address;
		this.startDate = startDate;
		this.type = type;
	}

	public EConnection(String connId, String consumerNum, String meterNum, String fullName, String mobNumber, String address, Date startDate,
                       String type) {
		this.connId = connId;
		this.consumerNum = consumerNum;
		this.fullName=fullName;
		this.meterNum = meterNum;
		this.mobNumber = mobNumber;
		this.address = address;
		this.startDate = startDate;
		this.type = type;
	}

	public String getConnId() {
		return connId;
	}

	public void setConnId(String connId) {
		this.connId = connId;
	}

	public String getConsumerNum() {
		return consumerNum;
	}

	public void setConsumerNum(String consumerNum) {
		this.consumerNum = consumerNum;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMeterNum() {
		return meterNum;
	}

	public void setMeterNum(String meterNum) {
		this.meterNum = meterNum;
	}

	public String getMobNumber() {
		return mobNumber;
	}

	public void setMobNumber(String mobNumber) {
		this.mobNumber = mobNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
				"EConnection : [connId = %s, consumerNum = %s, meterNo = %s, fullName = %s, mob = %s, address = %s, startDate = %s, Type = %s] \n",
				connId, consumerNum, meterNum, fullName, mobNumber, address, startDate, type
		);
	}

}
