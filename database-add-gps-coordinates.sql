-- SQL Script to add GPS coordinates to existing vehicles
-- This ensures all vehicles (including AVAILABLE ones) appear on the map

-- Update vehicles without GPS coordinates with random locations around NYC
UPDATE vehicles 
SET 
    latitude = 40.7128 + (RANDOM() % 100 - 50) * 0.001,
    longitude = -74.0060 + (RANDOM() % 100 - 50) * 0.001
WHERE latitude IS NULL OR longitude IS NULL;

-- Set speed to 0 for AVAILABLE and non-moving vehicles
UPDATE vehicles 
SET speed = 0.0
WHERE status IN ('AVAILABLE', 'MAINTENANCE', 'OUT_OF_SERVICE');

-- Initialize battery levels for electric vehicles if not set
UPDATE vehicles 
SET battery_level = 80 + (RANDOM() % 20)
WHERE is_electric = 1 AND battery_level IS NULL;

-- Initialize fuel levels for non-electric vehicles if not set
UPDATE vehicles 
SET fuel_level = 70 + (RANDOM() % 30)
WHERE is_electric = 0 AND fuel_level IS NULL;

-- Verify the updates
SELECT 
    id,
    vehicle_number,
    status,
    latitude,
    longitude,
    speed,
    CASE WHEN is_electric = 1 THEN battery_level ELSE fuel_level END as power_level,
    is_electric
FROM vehicles
ORDER BY status, vehicle_number;
