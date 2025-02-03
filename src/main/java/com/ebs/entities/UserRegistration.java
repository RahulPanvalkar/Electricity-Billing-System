package com.ebs.entities;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "user_registration")
public class UserRegistration {

    @Id
    @Column(name = "reg_id")
    private String regId;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "email_id", nullable = false, unique = true)
    private String emailId;
    @Column(name = "mob_number", nullable = false, unique = true)
    private String mobNumber;
    @Column(name = "user_type", nullable = false)
    private char userType;
    @Column
    private String address;
    @Column(name = "ver_code")
    private String verCode;
    @Column(name = "expires_at")
    private Timestamp expiresAt;
    @Column
    private char active;
    @Column(name = "add_date", updatable = false)
    private LocalDate addDate;

    @PrePersist
    protected void onCreate() {
        addDate = LocalDate.now();
    }


    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
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

    public char getUserType() {
        return userType;
    }

    public void setUserType(char userType) {
        this.userType = userType;
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

    public char getActive() {
        return active;
    }

    public void setActive(char active) {
        this.active = active;
    }

    public Date getAddDate() {
        return Date.valueOf(addDate);
    }

    @Override
    public String toString() {
        return String.format(
                "UserReg : [regId=%s, firstName=%s, lastName=%s, active=%c, emailId=%s, mobNumber=%s, userType=%c, verCode=%s, address=%s]",
                regId, firstName, lastName, active, emailId, mobNumber, userType, verCode, address
        );
    }
}
