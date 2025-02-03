package com.ebs.entities;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "bill_details")
public class Bill {
	@Id
	@Column(name = "bill_no")
	private String billNo;
	@Column(name = "bill_date", nullable = false)
	private Date billDate;
	@Column(name = "consumer_num", nullable = false)
	private String consumerNum;
	@Column(name = "meter_num", nullable = false)
	private String meterNum;
	@Column(nullable = false)
	private String month;
	@Column(name = "current_reading", nullable = false)
	private int currentReading;
	@Column(name = "previous_reading", nullable = false)
	private int previousReading;
	@Column(name = "total_units", nullable = false)
	private int totalUnits;
	@Column(name = "previous_balance", columnDefinition = "DOUBLE DEFAULT 0.0")
	private double previousBalance;
	@Column(name = "current_amount", nullable = false)
	private double currentAmount;
	@Column(name = "total_amount", nullable = false)
	private double totalAmount;
	@Column(name = "due_date", nullable = false)
	private Date dueDate;
	@Column(name = "payment_date")
	private Date paymentDate;
	@Column(name = "status", nullable = false)
	private String status;
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

	public Bill() { }
	
	public double getCurrentAmount() {
		return currentAmount;
	}

	public void setCurrentAmount(double currentAmount) {
		this.currentAmount = currentAmount;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getConsumerNum() {
		return consumerNum;
	}

	public void setConsumerNum(String consumerNum) {
		this.consumerNum = consumerNum;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public int getCurrentReading() {
		return currentReading;
	}

	public void setCurrentReading(int currentReading) {
		this.currentReading = currentReading;
	}

	public int getPreviousReading() {
		return previousReading;
	}

	public void setPreviousReading(int previousReading) {
		this.previousReading = previousReading;
	}

	public int getTotalUnits() {
		return totalUnits;
	}

	public void setTotalUnits(int totalUnits) {
		this.totalUnits = totalUnits;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double amount) {
		this.totalAmount = amount;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	public String getMeterNum() {
		return meterNum;
	}

	public void setMeterNum(String meterNum) {
		this.meterNum = meterNum;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public double getPreviousBalance() {
		return previousBalance;
	}

	public void setPreviousBalance(double previousBalance) {
		this.previousBalance = previousBalance;
	}


	public Date getUpdateDate() {
		return Date.valueOf(updateDate);
	}

	public Date getAddDate() {
		return Date.valueOf(addDate);
	}

	@Override
	public String toString() {
		return String.format(
				"Bill : [billNo=%s, billDate=%s, consumerNum=%s, meterNum=%s, month=%s, currentReading=%d, previousReading=%d, totalUnits=%d, previousBalance=%.2f, currentAmount=%.2f, totalAmount=%.2f, dueDate=%s, paymentDate=%s, status=%s, updateDate=%s]",
				billNo, billDate, consumerNum, meterNum, month, currentReading, previousReading, totalUnits, previousBalance, currentAmount, totalAmount, dueDate, paymentDate, status, updateDate
		);
	}

}
