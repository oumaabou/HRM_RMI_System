-- ============================================
-- SAMPLE TEST DATA FOR HRM SYSTEM
-- ============================================

-- 1. INSERT EMPLOYEES
INSERT INTO employees (first_name, last_name, ic_number, address, gender, phone_number, email, hire_date, department) 
VALUES ('John', 'Smith', '123456-10-1234', '123 Main St, Kuala Lumpur', 'MALE', '012-3456789', 'john.smith@crest.com', '2023-01-15', 'IT');

INSERT INTO employees (first_name, last_name, ic_number, address, gender, phone_number, email, hire_date, department) 
VALUES ('Jane', 'Doe', '789012-12-5678', '456 Park Ave, Penang', 'FEMALE', '019-8765432', 'jane.doe@crest.com', '2023-03-20', 'HR');

INSERT INTO employees (first_name, last_name, ic_number, address, gender, phone_number, email, hire_date, department) 
VALUES ('Mike', 'Johnson', '345678-14-9012', '789 Beach Rd, Johor', 'MALE', '016-1234567', 'mike.johnson@crest.com', '2024-01-10', 'Finance');

-- 2. INSERT USERS
INSERT INTO users (employee_id, username, password_hash, salt, role) 
VALUES (1, 'john.smith', 'encrypted_hash_123', 'salt_123', 'EMPLOYEE');

INSERT INTO users (employee_id, username, password_hash, salt, role) 
VALUES (2, 'jane.doe', 'encrypted_hash_456', 'salt_456', 'HR_STAFF');

INSERT INTO users (employee_id, username, password_hash, salt, role) 
VALUES (3, 'mike.johnson', 'encrypted_hash_789', 'salt_789', 'EMPLOYEE');

-- 3. INSERT FAMILY DETAILS
INSERT INTO family_details (employee_id, family_member_name, relationship, phone_number) 
VALUES (1, 'Mary Smith', 'SPOUSE', '012-1112223');

INSERT INTO family_details (employee_id, family_member_name, relationship, phone_number) 
VALUES (1, 'Tommy Smith', 'CHILD', '012-1112224');

INSERT INTO family_details (employee_id, family_member_name, relationship, phone_number) 
VALUES (2, 'Robert Doe', 'SPOUSE', '019-2223334');

INSERT INTO family_details (employee_id, family_member_name, relationship, phone_number) 
VALUES (3, 'Sarah Johnson', 'SPOUSE', '016-3334445');

-- 4. INSERT LEAVE BALANCE
INSERT INTO leave_balance (employee_id, leave_year, annual_remaining, sick_remaining, emergency_remaining) 
VALUES (1, 2026, 12.0, 14.0, 3.0);

INSERT INTO leave_balance (employee_id, leave_year, annual_remaining, sick_remaining, emergency_remaining) 
VALUES (2, 2026, 15.0, 14.0, 3.0);

INSERT INTO leave_balance (employee_id, leave_year, annual_remaining, sick_remaining, emergency_remaining) 
VALUES (3, 2026, 12.0, 14.0, 3.0);

-- 5. INSERT LEAVE APPLICATIONS
INSERT INTO leaves (employee_id, leave_type, start_date, end_date, total_days, reason, status) 
VALUES (1, 'ANNUAL', '2026-04-10', '2026-04-12', 3.0, 'Family vacation', 'APPROVED');

INSERT INTO leaves (employee_id, leave_type, start_date, end_date, total_days, reason, status) 
VALUES (1, 'SICK', '2026-03-01', '2026-03-01', 1.0, 'Flu', 'APPROVED');

INSERT INTO leaves (employee_id, leave_type, start_date, end_date, total_days, reason, status) 
VALUES (1, 'ANNUAL', '2026-05-20', '2026-05-22', 3.0, 'Wedding', 'PENDING');

INSERT INTO leaves (employee_id, leave_type, start_date, end_date, total_days, reason, status) 
VALUES (2, 'ANNUAL', '2026-06-01', '2026-06-05', 5.0, 'Overseas trip', 'PENDING');

INSERT INTO leaves (employee_id, leave_type, start_date, end_date, total_days, reason, status) 
VALUES (3, 'SICK', '2026-02-15', '2026-02-16', 2.0, 'Food poisoning', 'APPROVED');

INSERT INTO leaves (employee_id, leave_type, start_date, end_date, total_days, reason, status) 
VALUES (3, 'EMERGENCY', '2026-07-10', '2026-07-10', 1.0, 'Family emergency', 'APPROVED');