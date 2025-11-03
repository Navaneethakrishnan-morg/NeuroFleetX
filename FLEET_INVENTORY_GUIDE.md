# Fleet Inventory & Vehicle Telemetry System

## Overview

A comprehensive real-time fleet management system with full CRUD operations, live telemetry simulation, and responsive UI built with React and Spring Boot.

## Features

### Backend (Spring Boot + SQLite)

1. **Enhanced Vehicle Model**
   - Added `speed` field for real-time telemetry
   - Supports both electric (battery) and fuel vehicles
   - Tracks location (latitude/longitude), health score, mileage

2. **REST API Endpoints**
   - `GET /api/admin/vehicles` - Get all vehicles
   - `GET /api/vehicles/{id}` - Get single vehicle
   - `POST /api/admin/vehicles` - Create new vehicle
   - `PUT /api/admin/vehicles/{id}` - Update vehicle
   - `DELETE /api/admin/vehicles/{id}` - Delete vehicle
   - `GET /api/vehicles/filter` - Filter and sort vehicles

3. **Telemetry Simulator**
   - Runs every 3 seconds automatically
   - Status-aware simulation:
     - **IN_USE**: Speed varies by type (20-70 mph), battery/fuel decreases, location changes
     - **AVAILABLE**: Speed = 0, battery charges if electric
     - **MAINTENANCE**: Speed = 0, health score improves
     - **OUT_OF_SERVICE**: Speed = 0, no changes
   - Realistic speed ranges per vehicle type

4. **Real-Time Updates**
   - Polling-based updates every 3 seconds
   - All vehicles update simultaneously
   - Database persistence across restarts

### Frontend (React + Tailwind CSS)

1. **Fleet Inventory Page** (`/manager/fleet-inventory`, `/admin/fleet-inventory`)
   - Grid and compact view modes
   - Real-time status cards (Available, In Use, Maintenance, Total Fleet)
   - Automatic telemetry updates every 3 seconds

2. **Vehicle Cards**
   - Visual status indicators (green/cyan/purple/red)
   - Animated battery/fuel level bars
   - Real-time speed display with pulse animation
   - Location coordinates display
   - Health score and mileage
   - Smooth transition animations on telemetry changes

3. **Filtering & Sorting**
   - Filter by status (Available, In Use, Maintenance, Out of Service)
   - Filter by type (Sedan, SUV, Van, Truck, Bus, Bike)
   - Filter by minimum battery/fuel level
   - Sort by battery/fuel, status, type, or speed
   - Clear filters button

4. **CRUD Operations**
   - **Add Vehicle**: Modal form with validation
   - **Edit Vehicle**: Pre-populated modal form
   - **Delete Vehicle**: Confirmation dialog
   - All operations update immediately

5. **Design**
   - NeuroFleetX theme (dark blue primary, emerald/cyan accents)
   - Glass morphism effects
   - Responsive layout (mobile, tablet, desktop)
   - Accessible controls and indicators

## Database Schema

```sql
CREATE TABLE vehicles (
    id BIGINT PRIMARY KEY,
    vehicle_number VARCHAR(50) UNIQUE NOT NULL,
    model VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(100) NOT NULL,
    type ENUM('SEDAN', 'SUV', 'VAN', 'TRUCK', 'BUS', 'BIKE'),
    capacity INT NOT NULL,
    is_electric BOOLEAN DEFAULT FALSE,
    status ENUM('AVAILABLE', 'IN_USE', 'MAINTENANCE', 'OUT_OF_SERVICE'),
    latitude DOUBLE,
    longitude DOUBLE,
    battery_level INT,
    fuel_level INT,
    speed DOUBLE DEFAULT 0.0,
    mileage INT DEFAULT 0,
    health_score INT DEFAULT 100,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
```

## Setup Instructions

### Backend Setup

1. **Apply Database Migration**:
   ```bash
   cd backend
   # If using SQLite, the migration runs automatically on startup
   # Or manually execute: database-migration-speed.sql
   ```

2. **Start Backend Server**:
   ```bash
   mvn spring-boot:run
   # Server starts on http://localhost:8080
   ```

### Frontend Setup

1. **Install Dependencies** (if not already):
   ```bash
   cd frontend
   npm install
   ```

2. **Start Frontend**:
   ```bash
   npm start
   # Opens browser at http://localhost:3000
   ```

## Usage

1. **Access the System**:
   - Navigate to: `http://localhost:3000`
   - Login as Manager or Admin
   - Go to Fleet Inventory from the navigation menu

2. **View Real-Time Telemetry**:
   - Cards update automatically every 3 seconds
   - Watch speed, battery/fuel, and location changes
   - Animated indicators show when values change

3. **Add a New Vehicle**:
   - Click "+ Add Vehicle" button
   - Fill in all required fields
   - Select electric or fuel type
   - Click "Add Vehicle"

4. **Edit a Vehicle**:
   - Click "Edit" on any vehicle card
   - Update the desired fields
   - Click "Update Vehicle"

5. **Delete a Vehicle**:
   - Click "Delete" on any vehicle card
   - Confirm the deletion

6. **Filter and Sort**:
   - Use dropdown filters for status and type
   - Enter minimum battery level
   - Select sort order
   - Click "Clear Filters" to reset

7. **Toggle View**:
   - Click "Grid" or "Compact" button
   - Grid view: Large detailed cards
   - Compact view: Smaller cards in 3-column grid

## Telemetry Simulation Details

### Speed Calculation (IN_USE status)
- **Sedan**: 35-70 mph
- **SUV**: 40-70 mph
- **Van**: 35-60 mph
- **Truck**: 30-50 mph
- **Bus**: 30-50 mph
- **Bike**: 20-50 mph

### Battery/Fuel Consumption
- **IN_USE**: Decreases 0-2% per update cycle
- **AVAILABLE**: Electric vehicles charge 0-1% per cycle (if <95%)
- Minimum levels: 5% (prevents complete drain)

### Location Updates
- **IN_USE**: Coordinates change by ±0.005° per update
- **Other statuses**: No location change

### Health Score
- **MAINTENANCE**: Improves 0-4% per cycle (max 100%)
- **IN_USE**: Gradually decreases with use
- Reflects vehicle condition

## File Structure

```
backend/src/main/java/com/neurofleetx/
├── config/
│   └── WebSocketConfig.java         # WebSocket configuration
├── controller/
│   └── VehicleController.java       # REST API endpoints
├── model/
│   └── Vehicle.java                 # Vehicle entity with speed
├── service/
│   ├── VehicleService.java          # Business logic + telemetry
│   └── TelemetrySimulator.java      # Scheduled telemetry updates
├── repository/
│   └── VehicleRepository.java       # Database access
└── NeuroFleetXApplication.java      # Main app with @EnableScheduling

frontend/src/
├── pages/manager/
│   └── FleetInventory.js            # Main inventory page
├── components/
│   ├── VehicleCard.js               # Vehicle display card
│   └── VehicleModal.js              # Add/Edit modal
└── App.js                           # Updated with new routes
```

## API Examples

### Get All Vehicles
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" \
     http://localhost:8080/api/admin/vehicles
```

### Filter Vehicles
```bash
curl -H "Authorization: Bearer YOUR_TOKEN" \
     "http://localhost:8080/api/vehicles/filter?status=IN_USE&sortBy=speed"
```

### Create Vehicle
```bash
curl -X POST \
     -H "Authorization: Bearer YOUR_TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"vehicleNumber":"NF-100","model":"Model X","manufacturer":"Tesla","type":"SUV","capacity":7,"isElectric":true,"status":"AVAILABLE","batteryLevel":100}' \
     http://localhost:8080/api/admin/vehicles
```

### Update Vehicle
```bash
curl -X PUT \
     -H "Authorization: Bearer YOUR_TOKEN" \
     -H "Content-Type: application/json" \
     -d '{"status":"IN_USE"}' \
     http://localhost:8080/api/admin/vehicles/1
```

### Delete Vehicle
```bash
curl -X DELETE \
     -H "Authorization: Bearer YOUR_TOKEN" \
     http://localhost:8080/api/admin/vehicles/1
```

## Troubleshooting

### Telemetry Not Updating
- Check backend logs for scheduler errors
- Verify `@EnableScheduling` is present in main application class
- Ensure frontend polling interval is active

### Database Migration Issues
- Check SQLite database file exists
- Run migration script manually if needed
- Verify `speed` column was added successfully

### Frontend Not Connecting
- Verify backend is running on port 8080
- Check CORS configuration in `VehicleController`
- Ensure token is valid in localStorage

## Future Enhancements

- WebSocket support for true push notifications (SockJS/STOMP libraries not installed)
- Map view with vehicle locations
- Historical telemetry charts
- Predictive maintenance alerts
- Route replay and tracking
- Export telemetry data to CSV/JSON

## Credits

Built for NeuroFleetX - AI-Powered Urban Fleet Management System
