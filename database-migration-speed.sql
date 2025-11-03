-- Add speed column to vehicles table for real-time telemetry
ALTER TABLE vehicles ADD COLUMN speed DOUBLE DEFAULT 0.0;

-- Update existing vehicles with initial speed values based on status
UPDATE vehicles SET speed = 0.0 WHERE status IN ('AVAILABLE', 'MAINTENANCE', 'OUT_OF_SERVICE');
UPDATE vehicles SET speed = 45.5 WHERE status = 'IN_USE' AND type = 'SEDAN';
UPDATE vehicles SET speed = 42.3 WHERE status = 'IN_USE' AND type = 'SUV';
UPDATE vehicles SET speed = 38.7 WHERE status = 'IN_USE' AND type = 'VAN';
UPDATE vehicles SET speed = 35.2 WHERE status = 'IN_USE' AND type = 'TRUCK';
UPDATE vehicles SET speed = 25.8 WHERE status = 'IN_USE' AND type = 'BIKE';

-- Ensure all vehicles have proper telemetry data
UPDATE vehicles SET latitude = 40.7128 WHERE latitude IS NULL;
UPDATE vehicles SET longitude = -74.0060 WHERE longitude IS NULL;
UPDATE vehicles SET battery_level = 85 WHERE battery_level IS NULL AND is_electric = 1;
UPDATE vehicles SET fuel_level = 75 WHERE fuel_level IS NULL AND is_electric = 0;
UPDATE vehicles SET health_score = 95 WHERE health_score IS NULL;
UPDATE vehicles SET mileage = 10000 WHERE mileage IS NULL OR mileage = 0;

-- Display updated vehicle data
SELECT 
    vehicle_number,
    model,
    type,
    status,
    speed,
    COALESCE(battery_level, fuel_level) as power_level,
    latitude,
    longitude
FROM vehicles
ORDER BY status, type;
