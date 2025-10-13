-- NeuroFleetX Database Initialization Script

-- Create Database
CREATE DATABASE IF NOT EXISTS neurofleetx;
USE neurofleetx;

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS trips;
DROP TABLE IF EXISTS maintenance;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS users;

-- Create Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role ENUM('ADMIN', 'MANAGER', 'DRIVER', 'CUSTOMER') NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL
);

-- Create Vehicles Table
CREATE TABLE vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_number VARCHAR(50) UNIQUE NOT NULL,
    model VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(100) NOT NULL,
    type ENUM('SEDAN', 'SUV', 'VAN', 'TRUCK', 'BUS', 'BIKE') NOT NULL,
    capacity INT NOT NULL,
    is_electric BOOLEAN DEFAULT FALSE,
    status ENUM('AVAILABLE', 'IN_USE', 'MAINTENANCE', 'OUT_OF_SERVICE') DEFAULT 'AVAILABLE',
    latitude DOUBLE,
    longitude DOUBLE,
    battery_level INT,
    fuel_level INT,
    mileage INT DEFAULT 0,
    health_score INT DEFAULT 100,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    last_maintenance_date TIMESTAMP NULL
);

-- Create Bookings Table
CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    pickup_location VARCHAR(255),
    dropoff_location VARCHAR(255),
    status ENUM('PENDING', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    total_price DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    is_recommended BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

-- Create Trips Table
CREATE TABLE trips (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    driver_id BIGINT,
    vehicle_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NULL,
    start_location VARCHAR(255),
    end_location VARCHAR(255),
    distance DOUBLE,
    duration INT,
    status ENUM('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'SCHEDULED',
    route_data TEXT,
    estimated_arrival DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(id),
    FOREIGN KEY (driver_id) REFERENCES users(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

-- Create Maintenance Table
CREATE TABLE maintenance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    issue_type VARCHAR(100) NOT NULL,
    description TEXT,
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING',
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') DEFAULT 'MEDIUM',
    scheduled_date TIMESTAMP NULL,
    completed_date TIMESTAMP NULL,
    cost DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    is_predictive BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

-- Insert Sample Users (Password: admin123 for all - BCrypt hash)
INSERT INTO users (username, email, password, full_name, phone, role, active, created_at) VALUES
('admin', 'admin@neurofleetx.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', '555-0001', 'ADMIN', true, NOW()),
('manager1', 'manager@neurofleetx.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Fleet Manager', '555-0002', 'MANAGER', true, NOW()),
('driver1', 'driver1@neurofleetx.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'John Driver', '555-0003', 'DRIVER', true, NOW()),
('driver2', 'driver2@neurofleetx.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jane Driver', '555-0004', 'DRIVER', true, NOW()),
('customer1', 'customer1@neurofleetx.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Alice Customer', '555-0005', 'CUSTOMER', true, NOW()),
('customer2', 'customer2@neurofleetx.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Bob Customer', '555-0006', 'CUSTOMER', true, NOW());

-- Insert Sample Vehicles
INSERT INTO vehicles (vehicle_number, model, manufacturer, type, capacity, is_electric, status, latitude, longitude, battery_level, fuel_level, mileage, health_score, created_at) VALUES
('NF-001', 'Model S', 'Tesla', 'SEDAN', 5, true, 'AVAILABLE', 40.7128, -74.0060, 85, null, 12000, 95, NOW()),
('NF-002', 'Model 3', 'Tesla', 'SEDAN', 5, true, 'AVAILABLE', 40.7580, -73.9855, 92, null, 8000, 98, NOW()),
('NF-003', 'Camry', 'Toyota', 'SEDAN', 5, false, 'AVAILABLE', 40.7489, -73.9680, null, 70, 25000, 88, NOW()),
('NF-004', 'Accord', 'Honda', 'SEDAN', 5, false, 'IN_USE', 40.7614, -73.9776, null, 65, 30000, 85, NOW()),
('NF-005', 'Explorer', 'Ford', 'SUV', 7, false, 'AVAILABLE', 40.7306, -73.9352, null, 80, 18000, 92, NOW()),
('NF-006', 'Highlander', 'Toyota', 'SUV', 7, false, 'AVAILABLE', 40.7589, -73.9851, null, 75, 22000, 90, NOW()),
('NF-007', 'Transit', 'Ford', 'VAN', 12, false, 'AVAILABLE', 40.7527, -73.9772, null, 90, 15000, 94, NOW()),
('NF-008', 'Sprinter', 'Mercedes', 'VAN', 12, false, 'MAINTENANCE', 40.7282, -73.9942, null, 45, 50000, 65, NOW()),
('NF-009', 'F-150', 'Ford', 'TRUCK', 5, false, 'AVAILABLE', 40.7614, -73.9776, null, 85, 28000, 87, NOW()),
('NF-010', 'Leaf', 'Nissan', 'SEDAN', 5, true, 'AVAILABLE', 40.7489, -73.9680, 78, null, 20000, 82, NOW());

-- Insert Sample Bookings
INSERT INTO bookings (customer_id, vehicle_id, start_time, end_time, pickup_location, dropoff_location, status, total_price, is_recommended, created_at) VALUES
(5, 1, DATE_ADD(NOW(), INTERVAL 2 DAY), DATE_ADD(NOW(), INTERVAL 3 DAY), '123 Main St, New York', '456 Park Ave, New York', 'CONFIRMED', 150.00, true, NOW()),
(5, 3, DATE_ADD(NOW(), INTERVAL 5 DAY), DATE_ADD(NOW(), INTERVAL 7 DAY), '789 Broadway, New York', '321 5th Ave, New York', 'PENDING', 200.00, false, NOW()),
(6, 4, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), '555 Madison Ave, New York', '777 Lexington Ave, New York', 'IN_PROGRESS', 100.00, false, NOW()),
(6, 5, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), '100 Wall St, New York', '200 Broadway, New York', 'COMPLETED', 250.00, true, DATE_SUB(NOW(), INTERVAL 5 DAY));

-- Insert Sample Maintenance Records
INSERT INTO maintenance (vehicle_id, issue_type, description, status, priority, scheduled_date, is_predictive, created_at) VALUES
(8, 'Engine Check', 'Routine engine maintenance required', 'IN_PROGRESS', 'HIGH', NOW(), false, NOW()),
(8, 'Brake System', 'Brake pads showing wear', 'PENDING', 'CRITICAL', DATE_ADD(NOW(), INTERVAL 2 DAY), true, NOW()),
(10, 'Battery Health', 'Battery capacity degrading', 'PENDING', 'MEDIUM', DATE_ADD(NOW(), INTERVAL 7 DAY), true, NOW()),
(3, 'Oil Change', 'Scheduled oil change', 'PENDING', 'MEDIUM', DATE_ADD(NOW(), INTERVAL 3 DAY), false, NOW()),
(4, 'Tire Rotation', 'Tires need rotation', 'COMPLETED', 'LOW', DATE_SUB(NOW(), INTERVAL 2 DAY), false, DATE_SUB(NOW(), INTERVAL 5 DAY));

-- Insert Sample Trips
INSERT INTO trips (booking_id, driver_id, vehicle_id, start_time, start_location, end_location, distance, duration, status, created_at) VALUES
(3, 3, 4, NOW(), '555 Madison Ave, New York', '777 Lexington Ave, New York', 5.2, 25, 'IN_PROGRESS', NOW()),
(4, 4, 5, DATE_SUB(NOW(), INTERVAL 5 DAY), '100 Wall St, New York', '200 Broadway, New York', 8.5, 35, 'COMPLETED', DATE_SUB(NOW(), INTERVAL 5 DAY));

-- Create indexes for better performance
CREATE INDEX idx_vehicles_status ON vehicles(status);
CREATE INDEX idx_bookings_customer ON bookings(customer_id);
CREATE INDEX idx_bookings_status ON bookings(status);
CREATE INDEX idx_trips_driver ON trips(driver_id);
CREATE INDEX idx_trips_status ON trips(status);
CREATE INDEX idx_maintenance_vehicle ON maintenance(vehicle_id);
CREATE INDEX idx_maintenance_status ON maintenance(status);

-- Display summary
SELECT 'Database initialized successfully!' AS Status;
SELECT COUNT(*) AS Total_Users FROM users;
SELECT COUNT(*) AS Total_Vehicles FROM vehicles;
SELECT COUNT(*) AS Total_Bookings FROM bookings;
SELECT COUNT(*) AS Total_Maintenance_Records FROM maintenance;
