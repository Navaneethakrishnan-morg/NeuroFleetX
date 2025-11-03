# Live Map Setup and Testing Guide

## What Was Fixed

The driver portal's Live Map was showing only a placeholder. I've now implemented a **fully functional real-time GPS tracking map** with the following features:

### Features Implemented:

1. **Real Leaflet Map Integration**
   - Interactive OpenStreetMap tiles
   - Pan, zoom, and explore capabilities
   - Professional map interface

2. **WebSocket Real-Time Updates**
   - Live vehicle tracking via WebSocket connection
   - Updates every 3 seconds from TelemetrySimulator
   - Connection status indicator

3. **Vehicle Markers**
   - Custom animated vehicle icons on the map
   - Click on any vehicle to view detailed information
   - Popup with vehicle stats (speed, battery/fuel, status)
   - Auto-centering on selected vehicle

4. **Route Visualization**
   - Green polyline showing route path
   - Visual route from pickup to destination

5. **Vehicle List Panel**
   - All vehicles displayed with real-time stats
   - Click to select and track specific vehicles
   - Status indicators (IN_USE, AVAILABLE, etc.)

6. **Vehicle Details Panel**
   - Selected vehicle information
   - Real-time speed, mileage, battery/fuel levels
   - Status and type information

## How to Test

### Step 1: Start the Backend
```bash
cd backend
# Make sure you have vehicles in the database
mvnw spring-boot:run
# OR use the batch file
start-backend.bat
```

The backend will:
- Start the TelemetrySimulator (updates every 3 seconds)
- Enable WebSocket at `ws://localhost:8080/ws`
- Serve vehicle data via REST API

### Step 2: Start the Frontend
```bash
cd frontend
npm start
# OR use the batch file
start-frontend.bat
```

### Step 3: Navigate to Live Map
1. Open http://localhost:3000
2. Go to Driver Portal
3. Click on "Live Map" in the navigation

### Step 4: Observe Real-Time Tracking
You should see:
- ✅ Connection status showing "Connected" with green indicator
- ✅ Interactive map centered on New York
- ✅ Vehicle markers (🚗) appearing on the map
- ✅ Vehicle count displayed (e.g., "5 vehicles tracked")
- ✅ List of all vehicles on the right panel
- ✅ Vehicles moving in real-time (if status is IN_USE)

### Step 5: Interact with the Map
- Click on vehicle markers to see popup details
- Click on vehicles in the list to auto-center the map
- Watch vehicle positions update every 3 seconds
- See speed and battery/fuel levels change in real-time

## Troubleshooting

### Map Not Showing
- Check browser console for errors
- Verify Leaflet CSS is loaded
- Check if `leaflet` and `react-leaflet` packages are installed

### No Vehicles Appearing
- Ensure backend is running on port 8080
- Check backend console for telemetry simulator logs
- Verify database has vehicles with latitude/longitude data
- Check browser console for WebSocket connection errors

### WebSocket Connection Failed
- Ensure backend WebSocketConfig allows origin `http://localhost:3000`
- Check if port 8080 is accessible
- Look for CORS errors in browser console
- Verify `@EnableScheduling` is on NeuroFleetXApplication

### Vehicles Not Moving
- Vehicles only move if their status is `IN_USE`
- Check vehicle status in the database
- TelemetrySimulator updates every 3 seconds (check backend logs)
- Ensure Spring scheduler is enabled

## Technical Details

### New Components Added:
1. **WebSocketService.js** - Manages STOMP/SockJS WebSocket connections
2. **LiveMap.js** - Complete rewrite with Leaflet integration
3. **Dependencies** - Added `sockjs-client` and `@stomp/stompjs`

### Backend Services Used:
- **TelemetrySimulator** - Generates GPS updates every 3 seconds
- **VehicleService** - Updates vehicle locations and telemetry
- **WebSocketConfig** - STOMP WebSocket configuration

### Data Flow:
```
TelemetrySimulator (backend)
    ↓ (every 3 seconds)
VehicleService.updateAllVehiclesTelemetry()
    ↓
WebSocket: /topic/telemetry
    ↓
WebSocketService.js (frontend)
    ↓
LiveMap component
    ↓
Leaflet map updates with new positions
```

## Adding More Vehicles

To test with more vehicles, you can add them via:
1. Fleet Inventory page (Manager/Admin portal)
2. Direct API call to `/api/admin/vehicles`
3. SQL insert into the database

Make sure new vehicles have:
- Initial latitude/longitude coordinates
- Status set to `IN_USE` to see them move
- Battery/fuel levels set appropriately

## Next Steps

Potential enhancements:
- Add route planning integration
- Show traffic data overlay
- Display delivery waypoints
- Add geofencing alerts
- Historical route replay
- Driver photo on marker
- Multi-vehicle tracking toggle
