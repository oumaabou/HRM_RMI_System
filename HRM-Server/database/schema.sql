-- ============================================
-- HRM SYSTEM DATABASE SCHEMA
-- Database: HRM_DB
-- ============================================

-- 1. EMPLOYEES TABLE
CREATE TABLE employees (
    employee_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    ic_number VARCHAR(20) NOT NULL UNIQUE,
    address VARCHAR(200),
    gender VARCHAR(10),
    phone_number VARCHAR(15),
    email VARCHAR(100) NOT NULL UNIQUE,
    hire_date DATE NOT NULL,
    department VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE
);

-- 2. USERS TABLE (Login & Security)
CREATE TABLE users (
    user_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    employee_id INT NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    salt VARCHAR(64) NOT NULL,
    role VARCHAR(20) NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- 3. FAMILY DETAILS TABLE
CREATE TABLE family_details (
    family_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    employee_id INT NOT NULL,
    family_member_name VARCHAR(100) NOT NULL,
    relationship VARCHAR(30) NOT NULL,
    phone_number VARCHAR(15),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- 4. LEAVE BALANCE TABLE
CREATE TABLE leave_balance (
    balance_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    employee_id INT NOT NULL UNIQUE,
    leave_year INT NOT NULL,
    annual_remaining DECIMAL(5,1) DEFAULT 12.0,
    sick_remaining DECIMAL(5,1) DEFAULT 14.0,
    emergency_remaining DECIMAL(5,1) DEFAULT 3.0,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- 5. LEAVES TABLE (All leave applications)
CREATE TABLE leaves (
    leave_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    employee_id INT NOT NULL,
    leave_type VARCHAR(20) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_days DECIMAL(5,1) NOT NULL,
    reason VARCHAR(500),
    status VARCHAR(20) DEFAULT 'PENDING',
    approved_by INT,
    applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approval_date TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);