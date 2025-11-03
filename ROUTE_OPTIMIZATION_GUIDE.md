# AI Route & Load Optimization Engine - Complete Guide

## 🎯 Overview

The NeuroFleetX AI Route & Load Optimization Engine is a comprehensive system that combines Dijkstra's shortest path algorithm with machine learning-based ETA prediction to generate optimal routes for fleet vehicles. The system includes automatic load balancing, real-time traffic analysis, and interactive map visualization.

## 🏗️ System Architecture

### Backend Components

1. **Entities**
   - `Route.java` - Route information with optimization metadata
   - `Load.java` - Load/cargo management with vehicle assignment

2. **Repositories**
   - `RouteRepository.java` - Route data persistence
   - `LoadRepository.java` - Load data persistence

3. **Services**
   - `RouteOptimizationEngine.java` - Dijkstra's algorithm implementation
   - `ETAPredictorService.java` - ML-based ETA prediction
   - `RouteService.java` - Route management and optimization orchestration
   - `LoadService.java` - Load management and vehicle assignment

4. **Controllers**
   - `RouteController.java` - REST API for route operations
   - `LoadController.java` - REST API for load management

5. **DTOs**
   - `RouteOptimizationRequest.java` - Route optimization input
   - `RouteOptimizationResponse.java` - Optimization results
   - `VehicleAssignmentRequest.java` - Vehicle assignment input

### Frontend Components

- **RouteOptimization.js** - Main optimization interface with:
  - OpenStreetMap integration (Leaflet)
  - Interactive route form
  - Color-coded polyline visualization
  - Dynamic ETA cards
  - Load management panel
  - Route history viewer

## 🚀 Features

### 1. AI Route Optimization
- **Dijkstra's Algorithm**: Finds optimal paths considering multiple factors
- **Multiple Optimization Types**:
  - 🟢 **FASTEST**: Minimizes travel time
  - 🔵 **ENERGY_EFFICIENT**: Minimizes energy consumption
  - 🟠 **BALANCED**: Optimal balance of time and energy
  - 🟣 **SHORTEST**: Minimizes distance

### 2. ML-Based ETA Prediction
- Considers traffic density (LOW/MEDIUM/HIGH/SEVERE)
- Time-of-day adjustments (rush hours, night hours)
- Vehicle type optimization (electric vs. fuel)
- Historical pattern analysis
- Weather factor simulation

### 3. Automatic Load Balancing
- Proximity-based vehicle assignment
- Capacity validation
- Energy level checks (battery/fuel)
- Priority-based load ordering
- Bulk auto-assignment capability

### 4. Interactive Map Visualization
- OpenStreetMap integration
- Color-coded route polylines
- Start/end markers
- Route selection on click
- Dynamic route updates

### 5. Real-Time Route Analysis
- Distance calculation
- Traffic level estimation
- Energy cost prediction
- Multiple alternative routes
- Time and energy savings metrics

## 📊 Database Schema

### Routes Table
```sql
- route_id (PK)
- vehicle_id (FK)
- start_location, end_location
- start_latitude, start_longitude
- end_latitude, end_longitude
- distance_km
- eta_minutes
- energy_cost
- traffic_level (LOW/MEDIUM/HIGH/SEVERE)
- optimization_type (FASTEST/ENERGY_EFFICIENT/BALANCED/SHORTEST)
- optimized_path (waypoints)
- status (PENDING/ACTIVE/COMPLETED/CANCELLED)
- priority
- timestamp, completed_at
```

### Loads Table
```sql
- load_id (PK)
- vehicle_id (FK)
- weight
- destination
- destination_latitude, destination_longitude
- priority (LOW/MEDIUM/HIGH/URGENT)
- assigned_route_id (FK)
- status (PENDING/ASSIGNED/IN_TRANSIT/DELIVERED/CANCELLED)
- pickup_location
- pickup_latitude, pickup_longitude
- created_at, assigned_at, delivered_at
- special_instructions
```

## 🔌 API Endpoints

### Route Management

#### POST `/api/routes/optimize`
Generate optimized routes for a given start and end location.

**Request Body**:
```json
{
  "vehicleId": 1,
  "startLocation": "New York City",
  "endLocation": "Times Square",
  "startLatitude": 40.7128,
  "startLongitude": -74.0060,
  "endLatitude": 40.7580,
  "endLongitude": -73.9855,
  "includeTrafficData": true,
  "generateAlternatives": true
}
```

**Response**:
```json
{
  "primaryRoute": {
    "routeId": 1,
    "vehicleId": 1,
    "startLocation": "New York City",
    "endLocation": "Times Square",
    "distanceKm": 5.2,
    "etaMinutes": 18,
    "energyCost": 0.78,
    "trafficLevel": "MEDIUM",
    "optimizationType": "BALANCED",
    "optimizedPath": "Start -> Highway-1 -> Downtown -> End",
    "status": "PENDING"
  },
  "alternativeRoutes": [...],
  "optimizationAlgorithm": "Dijkstra + ML ETA Predictor",
  "totalRoutesAnalyzed": 3,
  "timeSavedMinutes": 5.2,
  "energySavedPercent": 12.3
}
```

#### GET `/api/routes`
Get all routes (ordered by timestamp).

#### GET `/api/routes/{id}`
Get specific route by ID.

#### GET `/api/routes/vehicle/{vehicleId}`
Get all routes for a specific vehicle.

#### GET `/api/routes/active`
Get all active routes (ordered by priority).

#### PUT `/api/routes/{id}/status?status={status}`
Update route status (PENDING/ACTIVE/COMPLETED/CANCELLED).

### Load Management

#### POST `/api/loads`
Create a new load.

**Request Body**:
```json
{
  "weight": 150.5,
  "destination": "Manhattan, NY",
  "destinationLatitude": 40.7831,
  "destinationLongitude": -73.9712,
  "priority": "HIGH",
  "pickupLocation": "Brooklyn, NY",
  "pickupLatitude": 40.6782,
  "pickupLongitude": -73.9442,
  "specialInstructions": "Handle with care"
}
```

#### GET `/api/loads`
Get all loads.

#### GET `/api/loads/pending`
Get pending loads (ordered by priority).

#### POST `/api/loads/assign`
Manually assign a vehicle to a load.

**Request Body**:
```json
{
  "loadId": 1,
  "destination": "Manhattan, NY",
  "destinationLatitude": 40.7831,
  "destinationLongitude": -73.9712,
  "weight": 150.5,
  "priority": "HIGH"
}
```

#### POST `/api/loads/auto-assign`
Automatically assign vehicles to all pending loads based on proximity, capacity, and energy efficiency.

#### PUT `/api/loads/{id}/status?status={status}`
Update load status (PENDING/ASSIGNED/IN_TRANSIT/DELIVERED/CANCELLED).

## 🎨 UI Features

### Route Optimization Form
- Vehicle selection dropdown
- Start/End location inputs
- Latitude/Longitude coordinates
- Generate Routes button
- Auto-Assign Loads button

### Interactive Map
- OpenStreetMap tiles
- Color-coded route polylines:
  - 🟢 Green: Fastest
  - 🔵 Blue: Energy Efficient
  - 🟠 Orange: Balanced
  - 🟣 Purple: Shortest
- Start/End markers
- Click to select routes
- Popup info on hover

### Route Options Cards
- Distance, ETA, Energy Cost
- Traffic level indicators
- Optimization type badges
- Optimized path visualization
- Click to highlight on map

### Analytics Dashboard
- Routes analyzed count
- Time saved metrics
- Energy saved percentage
- Algorithm information

### Load Management Panel
- Load status badges
- Weight and destination info
- Priority indicators
- Scrollable list view

### Recent Routes Panel
- Route history
- Status tracking
- Quick route selection
- Distance and ETA summary

## 🧪 Testing the System

### 1. Start Backend
```bash
cd backend
mvn spring-boot:run
```
Backend runs on: http://localhost:8080

### 2. Start Frontend
```bash
cd frontend
npm start
```
Frontend runs on: http://localhost:3000

### 3. Access Route Optimization
Navigate to:
- Manager: http://localhost:3000/manager/route-optimization
- Admin: http://localhost:3000/admin/route-optimization

### 4. Test Route Generation
1. Select a vehicle (optional)
2. Enter start location and coordinates (default: NYC)
3. Enter end location and coordinates (default: Times Square)
4. Click "🎯 Generate Routes"
5. View 3 alternative routes on the map
6. Click routes to select and view details

### 5. Test Auto-Assignment
1. Click "🤖 Auto-Assign Loads"
2. System assigns available vehicles to pending loads
3. View assignments in Load Management panel

## 🔧 Configuration

### Coordinates Examples
- **New York City**: 40.7128, -74.0060
- **Times Square**: 40.7580, -73.9855
- **Brooklyn**: 40.6782, -73.9442
- **Manhattan**: 40.7831, -73.9712
- **Queens**: 40.7282, -73.7949

### Traffic Levels
- **LOW**: Green indicator, high speed
- **MEDIUM**: Orange indicator, moderate speed
- **HIGH**: Red indicator, slow speed
- **SEVERE**: Dark red indicator, very slow

### Load Priorities
- **LOW**: Standard delivery
- **MEDIUM**: Normal priority
- **HIGH**: Important delivery
- **URGENT**: Critical/time-sensitive

## 📈 Algorithm Details

### Dijkstra's Shortest Path
- Builds graph with waypoints
- Calculates edge costs based on optimization type
- Finds optimal path using priority queue
- Returns complete path with waypoints

### ETA Prediction Factors
1. **Base Speed**: 45 km/h average
2. **Traffic Multiplier**: 0.5x to 1.2x based on level
3. **Vehicle Type**: Electric gets 1.05x multiplier
4. **Time of Day**: Rush hour 0.7x, night 1.3x
5. **ML Adjustments**: Historical patterns, weather

### Vehicle Assignment Algorithm
1. Filter available vehicles
2. Check capacity >= load weight
3. Verify energy level (battery/fuel > threshold)
4. Calculate distance to pickup location
5. Apply capacity and energy factors
6. Select nearest suitable vehicle

## 🎯 Color Coding System

### Route Types
- 🟢 **#10b981** - Fastest Route
- 🔵 **#3b82f6** - Energy Efficient
- 🟠 **#f59e0b** - Balanced
- 🟣 **#8b5cf6** - Shortest

### Traffic Levels
- 🟢 **#10b981** - Low Traffic
- 🟠 **#f59e0b** - Medium Traffic
- 🔴 **#ef4444** - High Traffic
- 🔴 **#991b1b** - Severe Traffic

## 🔒 Data Persistence

- All routes stored in SQLite database
- Loads persist with assignment history
- Routes reload after page refresh
- Vehicle assignments tracked
- Historical data for analytics

## 🚀 Future Enhancements

- Real-time GPS tracking integration
- Google Maps API integration option
- Weather API integration
- Advanced ML models for ETA
- Route optimization based on real traffic data
- Multi-stop route planning
- Route scheduling and automation
- Mobile app integration
- Export reports (PDF/CSV)

## 📝 Notes

- Default coordinates set to NYC area
- Routes visualized with random variations for demonstration
- ETA includes time-of-day adjustments
- Energy cost calculated based on distance and optimization type
- Auto-assignment considers proximity, capacity, and energy levels
- Map uses OpenStreetMap (no API key required)

## 🎉 Conclusion

The AI Route & Load Optimization Engine provides a complete solution for fleet route planning with:
- ✅ Dijkstra's algorithm for optimal pathfinding
- ✅ ML-based ETA prediction
- ✅ Automatic load balancing
- ✅ Interactive map visualization
- ✅ Real-time traffic analysis
- ✅ Complete data persistence
- ✅ RESTful API integration
- ✅ Modern, responsive UI

Access the system at `/manager/route-optimization` or `/admin/route-optimization` and start optimizing your fleet routes!
