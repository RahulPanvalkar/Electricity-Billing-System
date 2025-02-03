package com.ebs.entities;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table(name = "cost_per_unit")
public class CostPerUnit {

	@Id
	private int id;

	@Column(name = "0_to_100", nullable = false)
	private double unitsZeroToHundred;

	@Column(name = "101_to_300", nullable = false)
	private double unitsOneHundredOneToThreeHundred;

	@Column(name = "301_to_500", nullable = false)
	private double unitsThreeHundredOneToFiveHundred;

	@Column(name = "501_and_above", nullable = false)
	private double unitsFiveHundredOneAndAbove;

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

	public CostPerUnit() {
		
	}

	public CostPerUnit(double unitsZeroToHundred, double unitsOneHundredOneToThreeHundred,
                       double unitsThreeHundredOneToFiveHundred, double unitsFiveHundredOneAndAbove) {

		this.unitsZeroToHundred = unitsZeroToHundred;
		this.unitsOneHundredOneToThreeHundred = unitsOneHundredOneToThreeHundred;
		this.unitsThreeHundredOneToFiveHundred = unitsThreeHundredOneToFiveHundred;
		this.unitsFiveHundredOneAndAbove = unitsFiveHundredOneAndAbove;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getUnitsZeroToHundred() {
		return unitsZeroToHundred;
	}

	public void setUnitsZeroToHundred(double unitsZeroToHundred) {
		this.unitsZeroToHundred = unitsZeroToHundred;
	}

	public double getUnitsOneHundredOneToThreeHundred() {
		return unitsOneHundredOneToThreeHundred;
	}

	public void setUnitsOneHundredOneToThreeHundred(double unitsOneHundredOneToThreeHundred) {
		this.unitsOneHundredOneToThreeHundred = unitsOneHundredOneToThreeHundred;
	}

	public double getUnitsThreeHundredOneToFiveHundred() {
		return unitsThreeHundredOneToFiveHundred;
	}

	public void setUnitsThreeHundredOneToFiveHundred(double unitsThreeHundredOneToFiveHundred) {
		this.unitsThreeHundredOneToFiveHundred = unitsThreeHundredOneToFiveHundred;
	}

	public double getUnitsFiveHundredOneAndAbove() {
		return unitsFiveHundredOneAndAbove;
	}

	public void setUnitsFiveHundredOneAndAbove(double unitsFiveHundredOneAndAbove) {
		this.unitsFiveHundredOneAndAbove = unitsFiveHundredOneAndAbove;
	}

	public Date getAddDate() {
		return Date.valueOf(addDate);
	}

	public Date getUpdateDate() {
		return Date.valueOf(updateDate);
	}

	@Override
	public String toString() {
		
		return "CostPerUnit :: unitsZeroToHundred = " + unitsZeroToHundred + ",\nunitsOneHundredOneToThreeHundred = "
				+ unitsOneHundredOneToThreeHundred + ", \nunitsThreeHundredOneToFiveHundred = "
				+ unitsThreeHundredOneToFiveHundred + ", \nunitsFiveHundredOneAndAbove=" + unitsFiveHundredOneAndAbove;
		
	}
	
	
}
