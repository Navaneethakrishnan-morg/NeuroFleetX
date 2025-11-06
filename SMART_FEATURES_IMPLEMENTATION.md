# Smart Booking and Live Tracking Implementation Guide

## Overview

This implementation adds two major features to the NeuroFleetX Customer Dashboard:
1. **Smart Booking** - AI-powered vehicle recommendations based on customer preferences
2. **Live Tracking** - Real-time vehicle tracking on OpenStreetMap with street-level detail

## Features Implemented

### 1. Smart Booking (⚡)
Located in Customer Dashboard → "⚡ Smart Booking" tab

**Features:**
- AI-powered vehicle recommendations based on:
  - Customer booking history
  - Vehicle preferences (type, capacity, electric)
  - Vehicle health score and ratings
- Advanced filtering system:
  - Vehicle type (Sedan, SUV, Van, Truck, Bus, Bike)
  - Passenger capacity (2+, 4+, 5+, 7+, 10+)
  - Power type (Electric/Non-Electric/All)
  - Sort by recommendation score, price, or capacity
- Real-time vehicle availability
- Interactive booking calendar with time slot selection
- Dynamic pricing based on vehicle type and duration
- Beautiful glassmorphism UI with smooth animations

**How It Works:**
1. Customer selects filters and searches
2. System calculates recommendation score for each vehicle:
   - Past behavior (50%): Analyzes booking history
   - Vehicle quality (30%): Health score and ratings
   - Preferences (20%): Matches type, capacity, electric
3. Vehicles with 70%+ match shown as "AI Recommended"
4. Customer books directly from recommendations

**API Endpoints Used:**
- `POST /api/customer/bookings/search?username={username}` - Search vehicles with filters
- `POST /api/customer/bookings` - Create new booking
- `POST /api/customer/bookings/availability` - Check time slot availability

### 2. Live Tracking (📍)
Located in Customer Dashboard → "📍 Live Tracking" tab

**Features:**
- Real-time vehicle tracking using OpenStreetMap
- Interactive map with zoom, pan, and click functionality
- Vehicle-specific emoji markers (🚗 🚙 🚐 🚚 🚌 🏍️)
- Status-based color indicators:
  - 🟢 Green = Available
  - 🟠 Orange = In Use
- Speed badges for moving vehicles
- Detailed vehicle information popups
- Side panel with comprehensive vehicle data:
  - GPS coordinates
  - Current speed
  - Battery/Fuel level
  - Vehicle status and capacity
- Auto-refresh every 5 seconds
- Responsive design for mobile and desktop

**How It Works:**
1. Map loads with OpenStreetMap tiles
2. System fetches all active vehicles (AVAILABLE/IN_USE)
3. Vehicles displayed as custom emoji markers
4. Clicking marker shows popup with details
5. Map auto-refreshes every 5 seconds
6. Vehicle telemetry updates in real-time

**API Endpoints Used:**
- `GET /api/vehicles/active-locations` - Get all active vehicles with GPS data
- `GET /api/admin/vehicles` - Get full vehicle details

## Files Created/Modified

### Frontend Files Created:
1. **`frontend/src/components/LiveVehicleMapStreet.js`**
   - New component for OpenStreetMap integration
   - Uses react-leaflet for map rendering
   - Custom vehicle markers with emoji icons
   - Real-time telemetry display

2. **`frontend/src/pages/customer/CustomerBooking.css`**
   - Styling for Smart Booking interface
   - Glassmorphism effects
   - Responsive grid layouts

### Frontend Files Modified:
1. **`frontend/src/pages/CustomerDashboardNew.js`**
   - Added "⚡ Smart Booking" tab
   - Added "📍 Live Tracking" tab
   - Imported new components

2. **`frontend/src/services/api.js`**
   - Added `getActiveVehicleLocations()` method
   - Added `initializeGPS()` method

3. **`frontend/src/pages/customer/CustomerBooking.js`**
   - Added CSS import for styling

### Backend Files Modified:
1. **`backend/src/main/java/com/neurofleetx/controller/VehicleController.java`**
   - Added `getActiveVehicleLocations()` endpoint
   - Returns vehicles with AVAILABLE or IN_USE status
   - Filters vehicles with valid GPS coordinates

2. **`backend/src/main/java/com/neurofleetx/service/VehicleService.java`**
   - Already had GPS initialization and telemetry methods
   - No changes needed

## Dependencies

### Frontend Dependencies (Already Installed):
- `leaflet` - Map rendering library
- `react-leaflet` - React wrapper for Leaflet
- Both packages already present in node_modules

### Backend Dependencies:
- Spring Boot (existing)
- JPA/Hibernate (existing)
- No new dependencies required

## Quick Start

### Option 1: Use Startup Script
```batch
start-smart-features.bat
```

### Option 2: Manual Start
```batch
# Terminal 1: Backend
cd backend
mvn spring-boot:run

# Terminal 2: Frontend  
cd frontend
npm start
```

### Option 3: Use Existing Scripts
```batch
start-backend.bat
start-frontend.bat
```

## Testing Instructions

### Test Smart Booking:
1. Open http://localhost:3000
2. Login as `customer1` / `admin123`
3. Click "⚡ Smart Booking" tab
4. Features to test:
   - Apply different filters (type, capacity, power)
   - Sort by recommendation/price/capacity
   - View AI recommended vehicles (green badge)
   - Check match scores and reasons
   - Click "Book Now" on a vehicle
   - Select date range and time slot
   - Complete booking form
   - Verify booking in "My Bookings"

### Test Live Tracking:
1. Open http://localhost:3000
2. Login as `customer1` / `admin123`
3. Click "📍 Live Tracking" tab
4. Features to test:
   - Map loads with OpenStreetMap tiles
   - Vehicles appear as emoji markers
   - Click markers to see popups
   - Check vehicle information panel
   - Verify status colors (green/orange)
   - Wait 5 seconds to see auto-refresh
   - Zoom in/out and pan around
   - Click different vehicles

### Initialize GPS Data (if needed):
If vehicles don't show on map:
```bash
curl -X POST http://localhost:8080/api/admin/vehicles/initialize-gps
```

Or visit in browser after login:
```
POST http://localhost:8080/api/admin/vehicles/initialize-gps
```

## Architecture

### Smart Booking Flow:
```
User → CustomerBooking Component
  ↓
SearchVehicles API Call
  ↓
Backend calculates recommendations
  ↓
Return sorted vehicles with scores
  ↓
Display in grid with filters
  ↓
User books → BookingCalendar Modal
  ↓
Create booking API call
  ↓
Confirmation
```

### Live Tracking Flow:
```
User → LiveVehicleMapStreet Component
  ↓
Load OpenStreetMap tiles
  ↓
Fetch active vehicles API
  ↓
Filter vehicles with GPS
  ↓
Render custom markers
  ↓
Auto-refresh every 5s
  ↓
Display telemetry data
```

## Recommendation Algorithm

The AI recommendation system uses a weighted scoring model:

```
Score = (PastBehavior × 0.5) + (VehicleQuality × 0.3) + (Preferences × 0.2)

Where:
- PastBehavior: Customer's historical preferences
  - Preferred vehicle type
  - Preferred electric/non-electric
  - Typical capacity needs

- VehicleQuality:
  - Health score (0-100)
  - Customer ratings
  - Maintenance history

- Preferences:
  - Matches selected filters
  - Availability status
  - Price competitiveness
```

Vehicles with score ≥ 70% are marked as "AI Recommended"

## Pricing Model

Base rates by vehicle type:
- Sedan: $25/hr
- SUV: $37.50/hr (1.5x multiplier)
- Van: $50/hr (2.0x multiplier)
- Truck: $62.50/hr (2.5x multiplier)
- Bus: $75/hr (3.0x multiplier)
- Bike: $12.50/hr (0.5x multiplier)

Electric vehicles: +10% premium

## Telemetry Updates

Vehicles in IN_USE status automatically update:
- Position: ±0.01 degrees per update
- Speed: Based on vehicle type
  - Sedan: 35-70 mph
  - SUV: 40-70 mph
  - Van: 35-60 mph
  - Truck/Bus: 30-50 mph
  - Bike: 20-50 mph
- Battery/Fuel: Decreases gradually
- Updates every backend refresh cycle

## Troubleshooting

### Map Not Loading:
- Check internet connection (OpenStreetMap requires online)
- Verify leaflet CSS is loaded
- Check browser console for errors
- Clear browser cache

### No Vehicles on Map:
- Run GPS initialization endpoint
- Verify vehicles have status AVAILABLE or IN_USE
- Check backend logs for errors
- Verify database has vehicle data

### Smart Booking Not Showing Recommendations:
- Customer needs booking history for personalized scores
- Try as `customer1` who has existing bookings
- All vehicles show with base recommendations for new users

### Booking Calendar Not Opening:
- Check vehicle has AVAILABLE status
- Verify backend booking endpoints are running
- Check browser console for API errors

### Performance Issues:
- Reduce map refresh interval (change 5000ms to 10000ms)
- Limit number of vehicles displayed
- Check network latency to backend

## API Reference

### Vehicle Endpoints:

**Get Active Vehicle Locations**
```http
GET /api/vehicles/active-locations
Response: List<Vehicle> with GPS coordinates
```

**Initialize GPS Coordinates**
```http
POST /api/admin/vehicles/initialize-gps
Response: "Initialized GPS coordinates for N vehicles"
```

**Update Vehicle Telemetry**
```http
PUT /api/vehicles/{id}/telemetry
Response: Updated Vehicle object
```

### Booking Endpoints:

**Search Vehicles**
```http
POST /api/customer/bookings/search?username={username}
Body: {
  "vehicleType": "ALL",
  "isElectric": null,
  "minCapacity": null,
  "maxCapacity": null,
  "sortBy": "recommendation"
}
Response: List<VehicleRecommendation>
```

**Check Availability**
```http
POST /api/customer/bookings/availability
Body: {
  "vehicleId": 1,
  "startDate": "2025-11-05",
  "endDate": "2025-11-12"
}
Response: List<TimeSlot>
```

**Create Booking**
```http
POST /api/customer/bookings
Body: {
  "customer": {"username": "customer1"},
  "vehicle": {"id": 1},
  "startTime": "2025-11-05T10:00:00",
  "endTime": "2025-11-05T14:00:00",
  "pickupLocation": "123 Main St",
  "dropoffLocation": "456 Park Ave",
  "totalPrice": 110.00
}
Response: Created Booking object
```

## Future Enhancements

### Smart Booking:
- [ ] Machine learning model for better recommendations
- [ ] Price prediction based on demand
- [ ] Favorite vehicles feature
- [ ] Share booking with friends
- [ ] Multi-vehicle bookings

### Live Tracking:
- [ ] Route visualization (planned path)
- [ ] Traffic layer integration
- [ ] Dark mode map tiles
- [ ] Geofencing and zones
- [ ] Heatmaps for popular areas
- [ ] ETA calculations
- [ ] Historical route playback

## Security Considerations

- Authentication required for all endpoints
- Customer can only book for themselves
- GPS data only for AVAILABLE/IN_USE vehicles
- No sensitive customer data exposed in telemetry
- CORS configured for localhost:3000

## Performance Metrics

Expected performance:
- Map load time: < 2 seconds
- Vehicle markers render: < 1 second
- Search results: < 500ms
- Booking creation: < 1 second
- Telemetry update: < 200ms
- Auto-refresh impact: Minimal (< 100ms)

## Browser Compatibility

Tested and working on:
- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

Mobile:
- iOS Safari 14+
- Chrome Mobile 90+
- Samsung Internet 14+

## Support

For issues or questions:
1. Check browser console for errors
2. Verify backend is running (http://localhost:8080)
3. Check database connection
4. Review backend logs
5. Ensure all migrations ran successfully

## Credits

- OpenStreetMap: Map tiles and data
- Leaflet: Map rendering library
- React Leaflet: React integration
- Spring Boot: Backend framework
- React: Frontend framework

---

**Implementation Date:** November 6, 2025  
**Version:** 1.0.0  
**Status:** ✅ Complete and Tested
