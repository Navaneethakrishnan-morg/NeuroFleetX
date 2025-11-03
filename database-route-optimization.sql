-- NeuroFleetX Route & Load Optimization System
-- SQLite Database Schema for Route and Load Management

-- Routes Table
CREATE TABLE IF NOT EXISTS routes (
    route_id INTEGER PRIMARY KEY AUTOINCREMENT,
    vehicle_id INTEGER,
    start_location VARCHAR(255) NOT NULL,
    end_location VARCHAR(255) NOT NULL,
    start_latitude DOUBLE,
    start_longitude DOUBLE,
    end_latitude DOUBLE,
    end_longitude DOUBLE,
    distance_km DOUBLE NOT NULL,
    eta_minutes INTEGER NOT NULL,
    energy_cost DOUBLE,
    traffic_level VARCHAR(20),
    optimization_type VARCHAR(50) NOT NULL,
    optimized_path TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    priority INTEGER DEFAULT 5,
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME
);

-- Loads Table
CREATE TABLE IF NOT EXISTS loads (
    load_id INTEGER PRIMARY KEY AUTOINCREMENT,
    vehicle_id INTEGER,
    weight DOUBLE NOT NULL,
    destination VARCHAR(255) NOT NULL,
    destination_latitude DOUBLE,
    destination_longitude DOUBLE,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    assigned_route_id INTEGER,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    pickup_location VARCHAR(255),
    pickup_latitude DOUBLE,
    pickup_longitude DOUBLE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    assigned_at DATETIME,
    delivered_at DATETIME,
    special_instructions TEXT,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    FOREIGN KEY (assigned_route_id) REFERENCES routes(route_id)
);

-- Sample Data for Testing

-- Insert sample loads
INSERT INTO loads (weight, destination, destination_latitude, destination_longitude, priority, pickup_location, pickup_latitude, pickup_longitude, status)
VALUES 
(150.5, 'Manhattan, NY', 40.7831, -73.9712, 'HIGH', 'Brooklyn, NY', 40.6782, -73.9442, 'PENDING'),
(200.0, 'Queens, NY', 40.7282, -73.7949, 'MEDIUM', 'Bronx, NY', 40.8448, -73.8648, 'PENDING'),
(350.8, 'Staten Island, NY', 40.5795, -74.1502, 'URGENT', 'Manhattan, NY', 40.7589, -73.9851, 'PENDING'),
(100.2, 'Jersey City, NJ', 40.7178, -74.0431, 'LOW', 'Lower Manhattan, NY', 40.7128, -74.0060, 'PENDING');

-- Indexes for Performance
CREATE INDEX IF NOT EXISTS idx_routes_vehicle_id ON routes(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_routes_status ON routes(status);
CREATE INDEX IF NOT EXISTS idx_routes_optimization_type ON routes(optimization_type);
CREATE INDEX IF NOT EXISTS idx_loads_vehicle_id ON loads(vehicle_id);
CREATE INDEX IF NOT EXISTS idx_loads_status ON loads(status);
CREATE INDEX IF NOT EXISTS idx_loads_priority ON loads(priority);
CREATE INDEX IF NOT EXISTS idx_loads_assigned_route_id ON loads(assigned_route_id);

-- Views for Analytics

-- Active Routes Summary
CREATE VIEW IF NOT EXISTS active_routes_summary AS
SELECT 
    r.route_id,
    r.vehicle_id,
    v.vehicle_number,
    r.start_location,
    r.end_location,
    r.distance_km,
    r.eta_minutes,
    r.optimization_type,
    r.traffic_level,
    r.status
FROM routes r
LEFT JOIN vehicles v ON r.vehicle_id = v.id
WHERE r.status = 'ACTIVE';

-- Load Assignment Summary
CREATE VIEW IF NOT EXISTS load_assignment_summary AS
SELECT 
    l.load_id,
    l.vehicle_id,
    v.vehicle_number,
    l.weight,
    l.destination,
    l.priority,
    l.status,
    l.assigned_route_id,
    r.optimization_type as route_type,
    r.eta_minutes as estimated_delivery_time
FROM loads l
LEFT JOIN vehicles v ON l.vehicle_id = v.id
LEFT JOIN routes r ON l.assigned_route_id = r.route_id;

-- Route Performance Metrics
CREATE VIEW IF NOT EXISTS route_performance_metrics AS
SELECT 
    optimization_type,
    COUNT(*) as total_routes,
    AVG(distance_km) as avg_distance,
    AVG(eta_minutes) as avg_eta,
    AVG(energy_cost) as avg_energy_cost,
    traffic_level,
    status
FROM routes
GROUP BY optimization_type, traffic_level, status;
