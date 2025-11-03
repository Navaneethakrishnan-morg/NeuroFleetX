# Guide: Showing AVAILABLE Vehicles on the Live Map

## Overview
This guide explains how to ensure all vehicles (including those with "AVAILABLE" status) appear on the Live Map with their GPS location.

## Changes Made

### 1. Backend Service Updates

#### VehicleService.java - Enhanced Methods:

**createVehicle()**: 
- Automatically initializes GPS coordinates for new vehicles (random location around NYC)
- Sets battery/fuel levels if not provided
- Initializes speed to 0

**updateVehicle()**: 
- Preserves or updates GPS coordinates
- Ensures all vehicles have location data

**initializeAllVehicleGPS()**: 
- NEW method to initialize GPS for existing vehicles
- Updates vehicles without coordinates
- Sets default battery/fuel and speed values

#### VehicleController.java - New Endpoint:
```
POST /api/admin/vehicles/initialize-gps
```
Initializes GPS coordinates for all vehicles that don't have them.

### 2. How Vehicles Appear on Map

#### Vehicle Status Behavior:

**AVAILABLE Status**:
- ✅ Shows on map at fixed location
- ✅ Speed = 0 (parked)
- ✅ Battery/fuel level displayed
- ✅ Battery recharges slowly over time
- ❌ Does NOT move (stays at parking location)

**IN_USE Status**:
- ✅ Shows on map
- ✅ Moves in real-time (GPS updates every 3 seconds)
- ✅ Shows current speed
- ✅ Battery/fuel depletes while moving

**MAINTENANCE Status**:
- ✅ Shows on map at maintenance facility
- ✅ Speed = 0
- ✅ Health score improves over time

**OUT_OF_SERVICE Status**:
- ✅ Shows on map
- ✅ Speed = 0

## Setup Instructions

### Option 1: Initialize via REST API (Recommended)

1. **Start the backend**:
```bash
cd backend
mvnw spring-boot:run
```

2. **Call the initialization endpoint**:

Using curl:
```bash
curl -X POST http://localhost:8080/api/admin/vehicles/initialize-gps
```

Using PowerShell:
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/admin/vehicles/initialize-gps" -Method POST
```

Using browser: Just visit http://localhost:8080/api/admin/vehicles/initialize-gps (change method to POST if using Postman/Insomnia)

3. **Response Example**:
```
Initialized GPS coordinates for 5 vehicles
```

### Option 2: Initialize via SQL Script

1. **Run the SQL script**:
```bash
cd backend
sqlite3 neurofleetx.db < ../database-add-gps-coordinates.sql
```

2. **Verify in database**:
```sql
SELECT id, vehicle_number, status, latitude, longitude, speed 
FROM vehicles;
```

### Option 3: Automatic Initialization (Happens Automatically)

- Any NEW vehicle added via Fleet Inventory will automatically get GPS coordinates
- Any vehicle updated will get GPS coordinates if missing
- TelemetrySimulator ensures all vehicles have coordinates on first update

## Testing the Map

### Step 1: Add Test Vehicles with Different Statuses

Via Fleet Inventory UI or REST API:

```json
// AVAILABLE Vehicle
POST /api/admin/vehicles
{
  "vehicleNumber": "NYC-AVAIL-001",
  "model": "Tesla Model 3",
  "manufacturer": "Tesla",
  "type": "SEDAN",
  "capacity": 4,
  "isElectric": true,
  "status": "AVAILABLE"
}

// IN_USE Vehicle
POST /api/admin/vehicles
{
  "vehicleNumber": "NYC-INUSE-001",
  "model": "Ford Transit",
  "manufacturer": "Ford",
  "type": "VAN",
  "capacity": 8,
  "isElectric": false,
  "status": "IN_USE"
}
```

### Step 2: Start Both Services

```bash
# Backend
cd backend
mvnw spring-boot:run

# Frontend (in new terminal)
cd frontend
npm start
```

### Step 3: Navigate to Live Map

1. Open http://localhost:3000
2. Go to **Driver Portal**
3. Click on **Live Map**

### Step 4: Observe Vehicles

You should see:

**AVAILABLE Vehicles**:
- 🟢 Green marker on the map (parked)
- Speed: 0 km/h
- Status badge: "AVAILABLE"
- Appears in the vehicle list
- Can click to view details

**IN_USE Vehicles**:
- 🟢 Green animated marker (moving)
- Speed: 30-70 km/h (varies by type)
- Status badge: "IN_USE" (highlighted in green)
- Position updates every 3 seconds
- Moves across the map

**Map Features**:
- ✅ Connection status: "Connected" (green)
- ✅ Vehicle count: "X vehicles tracked"
- ✅ Click markers to see popup details
- ✅ Click vehicles in list to center map
- ✅ All vehicles visible regardless of status

## Troubleshooting

### AVAILABLE Vehicles Not Showing

**Check 1**: Verify vehicles have GPS coordinates
```bash
# Connect to database
cd backend
sqlite3 neurofleetx.db

# Check coordinates
SELECT vehicle_number, status, latitude, longitude FROM vehicles WHERE status = 'AVAILABLE';
```

**Check 2**: Initialize GPS if missing
```bash
curl -X POST http://localhost:8080/api/admin/vehicles/initialize-gps
```

**Check 3**: Check backend logs
Look for: "Error simulating telemetry" or similar errors

**Check 4**: Verify WebSocket connection
- Open browser console (F12)
- Look for "WebSocket Connected" message
- Check for any connection errors

### Vehicles Appearing but Not in Correct Location

**Solution**: Update vehicle location via Fleet Inventory
1. Go to Manager/Admin Portal
2. Open Fleet Inventory
3. Edit the vehicle
4. The system will auto-assign GPS coordinates on next telemetry update

### AVAILABLE Vehicles Moving (Should Be Stationary)

**Check**: Verify vehicle status
```sql
SELECT vehicle_number, status FROM vehicles WHERE vehicle_number = 'YOUR-VEHICLE-NUMBER';
```

Should be 'AVAILABLE', not 'IN_USE'.

## Key Features Summary

| Feature | AVAILABLE Vehicles | IN_USE Vehicles |
|---------|-------------------|-----------------|
| Shows on Map | ✅ Yes | ✅ Yes |
| Has GPS Location | ✅ Yes | ✅ Yes |
| Moves on Map | ❌ No (Stationary) | ✅ Yes |
| Speed Display | 0 km/h | 30-70 km/h |
| Battery/Fuel | Recharges | Depletes |
| Real-time Updates | ✅ Yes (3 sec) | ✅ Yes (3 sec) |
| Clickable | ✅ Yes | ✅ Yes |
| In Vehicle List | ✅ Yes | ✅ Yes |

## Default GPS Locations

New vehicles are placed randomly around NYC:
- **Center**: 40.7128°N, 74.0060°W (New York City)
- **Radius**: ±0.1 degrees (approximately 11 km)

To customize default locations, modify:
```java
// VehicleService.java
vehicle.setLatitude(40.7128 + (random.nextDouble() - 0.5) * 0.1);
vehicle.setLongitude(-74.0060 + (random.nextDouble() - 0.5) * 0.1);
```

## Advanced: Set Custom Locations

### Via REST API:
```bash
curl -X PUT http://localhost:8080/api/admin/vehicles/1 \
  -H "Content-Type: application/json" \
  -d '{
    "latitude": 40.7589,
    "longitude": -73.9851,
    "status": "AVAILABLE"
  }'
```

### Via Database:
```sql
UPDATE vehicles 
SET 
    latitude = 40.7589,
    longitude = -73.9851
WHERE vehicle_number = 'NYC-001';
```

## Next Steps

1. ✅ All vehicles now have GPS coordinates
2. ✅ AVAILABLE vehicles appear on map
3. ✅ IN_USE vehicles move in real-time
4. 🔄 Consider adding parking zones for AVAILABLE vehicles
5. 🔄 Consider adding depot locations
6. 🔄 Consider adding geofencing for service areas
