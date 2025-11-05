-- Database Migration: Customer Preferences for AI Recommendations

-- Create customer_preferences table to track booking patterns
CREATE TABLE IF NOT EXISTS customer_preferences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    preferred_vehicle_type ENUM('SEDAN', 'SUV', 'VAN', 'TRUCK', 'BUS', 'BIKE'),
    preferred_electric BOOLEAN DEFAULT FALSE,
    preferred_capacity INT,
    average_booking_duration INT, -- in hours
    preferred_locations TEXT, -- JSON array of frequently used locations
    booking_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    UNIQUE KEY unique_customer_pref (customer_id)
);

-- Add indexes for performance
CREATE INDEX idx_customer_preferences_customer ON customer_preferences(customer_id);

-- Add columns to bookings table for recommendation tracking
ALTER TABLE bookings 
ADD COLUMN IF NOT EXISTS recommendation_score DOUBLE DEFAULT 0.0,
ADD COLUMN IF NOT EXISTS recommendation_reason VARCHAR(255);

-- Create vehicle_ratings table for customer feedback
CREATE TABLE IF NOT EXISTS vehicle_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    booking_id BIGINT,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

CREATE INDEX idx_vehicle_ratings_customer ON vehicle_ratings(customer_id);
CREATE INDEX idx_vehicle_ratings_vehicle ON vehicle_ratings(vehicle_id);

-- Insert sample customer preferences based on existing bookings
INSERT INTO customer_preferences (customer_id, preferred_vehicle_type, preferred_electric, preferred_capacity, booking_count)
SELECT 
    customer_id,
    (SELECT v.type FROM vehicles v WHERE v.id = b.vehicle_id LIMIT 1) as preferred_vehicle_type,
    (SELECT v.is_electric FROM vehicles v WHERE v.id = b.vehicle_id LIMIT 1) as preferred_electric,
    (SELECT v.capacity FROM vehicles v WHERE v.id = b.vehicle_id LIMIT 1) as preferred_capacity,
    COUNT(*) as booking_count
FROM bookings b
WHERE b.status IN ('COMPLETED', 'CONFIRMED')
GROUP BY customer_id
ON DUPLICATE KEY UPDATE 
    booking_count = VALUES(booking_count),
    updated_at = CURRENT_TIMESTAMP;

SELECT 'Customer preferences migration completed successfully!' AS Status;
