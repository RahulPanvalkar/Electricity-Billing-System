# CREATING DATABASE
#CREATE DATABASE EBS;
USE EBS;

#CREATING TABLE ADMIN_DETAILS
CREATE TABLE admin_details (
    id VARCHAR(8),
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    email_id VARCHAR(50) NOT NULL UNIQUE,
    mob_number VARCHAR(10) NOT NULL UNIQUE,
    address VARCHAR(100) NOT NULL,
    add_date DATE,
	update_date DATE,
	PRIMARY KEY (id)
);

#CREATING TRIGGER
DELIMITER //
CREATE TRIGGER tgr_admin_id
BEFORE INSERT ON admin_details
FOR EACH ROW
BEGIN
    DECLARE next_id INT;
    DECLARE new_id VARCHAR(8); -- ADM00001
    SET next_id = IFNULL((SELECT MAX(CAST(SUBSTRING(id, 4) AS UNSIGNED)) + 1 FROM admin_details), 1);
    SET new_id = CONCAT('ADM', LPAD(next_id, 5, '0'));
    SET NEW.id = new_id;
END;
//
DELIMITER ;



#------------------------------------------------------------------------------------------------------

#CREATING TABLE USERS
CREATE TABLE users (
    user_id VARCHAR(10),
    name VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email_id VARCHAR(30) NOT NULL UNIQUE,
    mob_number VARCHAR(10) NOT NULL UNIQUE,
    user_type ENUM('A', 'E', 'C') NOT NULL,
    user_code VARCHAR(8) NOT NULL,
    address VARCHAR(100),
    ver_code VARCHAR(6),
    expires_at TIMESTAMP,
	add_date DATE,
	update_date DATE,
	PRIMARY KEY (user_id),
    UNIQUE (user_type, user_code)
);

#CREATING TRIGGER
DELIMITER //
CREATE TRIGGER tgr_user_id
BEFORE INSERT ON users
FOR EACH ROW
BEGIN
    DECLARE next_id VARCHAR(10);
    SET next_id = LPAD(IFNULL((SELECT MAX(user_id) + 1 FROM users),1), 10, '0');
    SET NEW.user_id = next_id;
END;
//
DELIMITER ;


#------------------------------------------------------------------------------------------------------

#CREATING TABLE CONSUMER_DETAILS
CREATE TABLE consumer_details (
    consumer_num VARCHAR(8),
    full_name VARCHAR(50) NOT NULL,
    email_id VARCHAR(50) NOT NULL UNIQUE,
    mob_number VARCHAR(10) NOT NULL UNIQUE,
    address VARCHAR(100) NOT NULL,
    conn_id VARCHAR(10),
    add_date DATE,
    update_date DATE,
	PRIMARY KEY (consumer_num)
);

#CREATING TRIGGER
DELIMITER //
CREATE TRIGGER tgr_consumer_no
BEFORE INSERT ON consumer_details
FOR EACH ROW
BEGIN
    DECLARE next_id VARCHAR(8);
    SET next_id = LPAD(IFNULL((SELECT MAX(consumer_num) + 1 FROM consumer_details),1), 8, '0');
    SET NEW.Consumer_Num = next_id;
END;
//
DELIMITER ;


#------------------------------------------------------------------------------------------------------

#CREATING TABLE USER_REGISTRATION
CREATE TABLE user_registration (
    reg_id VARCHAR(10),
    first_name VARCHAR(15) NOT NULL,
    last_name VARCHAR(15) NOT NULL,
    email_id VARCHAR(50) NOT NULL UNIQUE,
    mob_number VARCHAR(10) NOT NULL UNIQUE,
    user_type ENUM('A', 'E', 'C') NOT NULL,
    address VARCHAR(100),
    ver_code VARCHAR(6) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    active ENUM('Y', 'N') NOT NULL DEFAULT 'N',
    add_date Date,
	PRIMARY KEY (reg_id)
);

#CREATING TRIGGER
DELIMITER //
CREATE TRIGGER tgr_reg_id
BEFORE INSERT ON user_registration
FOR EACH ROW
BEGIN
	DECLARE next_id VARCHAR(10);
    SET next_id = LPAD(IFNULL((SELECT MAX(reg_id) + 1 FROM user_registration),1), 10, '0');
    SET NEW.reg_id = next_id;
END;
//
DELIMITER ;

#------------------------------------------------------------------------------------------------------

#CREATING TABLE CONNECTION_DETAILS
CREATE TABLE connection_details (
	conn_id VARCHAR(10),
    consumer_num VARCHAR(8) NOT NULL UNIQUE,
    meter_num VARCHAR(8) NOT NULL UNIQUE,
	full_name VARCHAR(50) NOT NULL,
    mob_number VARCHAR(10) NOT NULL UNIQUE,
    address VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    type VARCHAR(15) NOT NULL,
    add_date DATE,
    update_date DATE,
	PRIMARY KEY (conn_id)
);

#CREATING TRIGGER
DELIMITER //
CREATE TRIGGER tgr_conn_id
BEFORE INSERT
ON connection_details
FOR EACH ROW
BEGIN
    DECLARE next_conn_id VARCHAR(10);

    -- Get the next bill number
    SELECT LPAD(IFNULL(MAX(CAST(SUBSTRING(conn_id, 4) AS UNSIGNED)) + 1, 1), 7, '0')
    INTO next_conn_id
    FROM connection_details;

    -- Set the new bill number
    SET NEW.conn_id = CONCAT('CON', next_conn_id);
END //
DELIMITER ;

#------------------------------------------------------------------------------------------------------

# CREATING TABLE BILL_DETAILS
CREATE TABLE bill_details (
    bill_no VARCHAR(10) PRIMARY KEY,
    bill_date DATE NOT NULL,
    consumer_num VARCHAR(8),
    meter_num VARCHAR(8) NOT NULL,
    month VARCHAR(10) NOT NULL,
    current_reading INT NOT NULL,
    previous_reading INT NOT NULL,
    total_units INT NOT NULL,
    previous_balance DOUBLE DEFAULT 0.0 NOT NULL,
    current_amount DOUBLE NOT NULL,
    total_amount DOUBLE NOT NULL,
    due_date DATE NOT NULL,
    payment_date DATE,
    status ENUM('Paid', 'Unpaid', 'Pending') DEFAULT 'Pending' NOT NULL,
    add_date DATE,
    update_date DATE,
    UNIQUE (consumer_num, bill_date)
);


#CREATING TRIGGER
DELIMITER //
CREATE TRIGGER tgr_bill_no
BEFORE INSERT
ON bill_details
FOR EACH ROW
BEGIN
    DECLARE next_bill_no VARCHAR(10);

    -- Get the next bill number
    SELECT LPAD(IFNULL(MAX(CAST(SUBSTRING(bill_no, 4) AS UNSIGNED)) + 1, 1), 7, '0')
    INTO next_bill_no
    FROM bill_details;

    -- Set the new bill number
    SET NEW.bill_no = CONCAT('EBS', next_bill_no);
END //
DELIMITER ;

#CREATING EVENT : Create an event to update the status to 'Unpaid' after the due date
-- Enable event scheduling (if not enabled)
SET GLOBAL event_scheduler = ON;

CREATE EVENT update_bill_status_event
ON SCHEDULE EVERY 1 DAY
DO
  UPDATE bill_details
  SET status = 'Unpaid'
  WHERE due_date < CURDATE() AND status='Pending';


#------------------------------------------------------------------------------------------------------

#CREATING TABLE COST_PER_UNIT
CREATE TABLE cost_per_unit (
	id INT PRIMARY KEY CHECK (id = 1),
    0_to_100 DOUBLE NOT NULL,
    101_to_300 DOUBLE NOT NULL,
    301_to_500 DOUBLE NOT NULL,
    501_and_above DOUBLE NOT NULL,
    add_date DATE,
    update_date DATE
);

INSERT INTO cost_per_unit VALUES (1,3.45,5.55,7.45,8.55, curdate(), curdate());

#------------------------------------------------------------------------------------------------------
