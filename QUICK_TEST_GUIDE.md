# Quick Test Guide - Booking Fix & GPS Tracking

## Prerequisites
1. Backend running on port 8080
2. Frontend running on port 3000
3. Database initialized with sample data

## Quick Start

### Start the Application

**Terminal 1 - Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm start
```

## Test 1: Customer Booking (Fixed)

### Steps:
1. Open browser: `http://localhost:3000`
2. Login as customer (or create new customer account)
3. Click on **"⚡ Smart Booking"** tab
4. You should see:
   - AI-powered vehicle recommendations
   - Search filters (vehicle type, seats, power type)
   - List of available vehicles
5. Click **"Book Now"** on any vehicle
6. In the booking calendar:
   - Select date range
   - Choose an available time slot (green)
   - Enter pickup location (e.g., "123 Main St, New York")
   - Enter dropoff location (e.g., "456 Park Ave, New York")
7. Click **"Confirm Booking"**
8. ✅ **Success**: You should see "Booking Confirmed!" message
9. Navigate to **"My Bookings"** tab to see your new booking

### Expected Result:
- No more booking errors
- Booking appears in "My Bookings" with PENDING status
- All booking details are correctly saved

## Test 2: Live GPS Vehicle Tracking (New Feature)

### Steps:
1. From customer dashboard, click **"📍 Live Tracking"** tab
2. You should see:
   - Purple gradient container with live map
   - Vehicle markers with emoji icons on the map
   - Legend showing Available (green) and In Use (orange)
   - Vehicle count at the top
3. Click on any vehicle marker
4. You should see:
   - Info card with vehicle details
   - Vehicle model and number
   - Current status
   - GPS coordinates
   - Speed (if moving)
   - Battery/Fuel level
5. Wait 5 seconds - markers should update automatically
6. Click X to close the info card

### Expected Result:
- All active vehicles appear on the map
- Vehicles show correct status colors
- Info card displays complete vehicle information
- Map refreshes every 5 seconds

## Test 3: GPS Location Updates via API

### Manual GPS Update:
```bash
# Update vehicle 1 location
curl -X PUT "http://localhost:8080/api/vehicles/1/location?latitude=40.7580&longitude=-73.9855"

# Update vehicle 2 location
curl -X PUT "http://localhost:8080/api/vehicles/2/location?latitude=40.7489&longitude=-73.9680"
```

### Expected Result:
- Vehicle markers move on the map
- New coordinates appear in info card
- Changes visible within 5 seconds

## Test 4: Complete Booking Flow

### Steps:
1. **Create Booking** (from Test 1)
2. Navigate to **"My Bookings"** tab
3. Find your PENDING booking
4. Admin should confirm the booking (or use API):
   ```bash
   curl -X PUT "http://localhost:8080/api/bookings/{booking-id}/confirm"
   ```
5. Refresh page - status should be CONFIRMED
6. Click **"Start Journey"** button
7. Status changes to IN_PROGRESS
8. Click **"Track Vehicle"** button
9. Should redirect to Live Tracking tab
10. Find your vehicle on the map (should be orange - IN_USE)
11. Click **"End Journey"**
12. Status changes to COMPLETED

### Expected Result:
- Smooth transition through all booking statuses
- Vehicle status updates accordingly
- Live tracking shows vehicle in use

## Test 5: Initialize GPS for All Vehicles

If vehicles don't have GPS coordinates:

```bash
curl -X POST "http://localhost:8080/api/admin/vehicles/initialize-gps"
```

### Expected Result:
- Response: "Initialized GPS coordinates for X vehicles"
- All vehicles now appear on Live Tracking map

## API Testing with Postman/cURL

### Get Active Vehicle Locations:
```bash
curl http://localhost:8080/api/customer/vehicles/active-locations
```

### Create Booking:
```bash
curl -X POST "http://localhost:8080/api/customer/bookings?username=customer1" \
  -H "Content-Type: application/json" \
  -d '{
    "vehicle": {"id": 1},
    "startTime": "2025-11-06T10:00:00",
    "endTime": "2025-11-06T14:00:00",
    "pickupLocation": "123 Main St",
    "dropoffLocation": "456 Park Ave",
    "totalPrice": 100.00
  }'
```

### Update Vehicle Location:
```bash
curl -X PUT "http://localhost:8080/api/vehicles/1/location?latitude=40.7580&longitude=-73.9855"
```

## Troubleshooting

### Issue: "User not found" error when booking
**Solution:** Make sure you're logged in and username is stored in localStorage

### Issue: No vehicles on Live Tracking map
**Solution:** 
1. Initialize GPS coordinates: `POST /api/admin/vehicles/initialize-gps`
2. Make sure vehicles have AVAILABLE or IN_USE status

### Issue: Booking shows old behavior
**Solution:**
1. Clear browser cache
2. Rebuild frontend: `npm run build`
3. Restart backend

### Issue: Map not updating
**Solution:**
1. Check console for errors
2. Verify API endpoint: `GET /api/customer/vehicles/active-locations`
3. Check network tab for 5-second interval requests

## Known Behaviors

1. **GPS Coordinates**: Center around New York City (40.7128, -74.0060)
2. **Auto-Update Interval**: 5 seconds for live tracking
3. **Vehicle Status Colors**:
   - 🟢 Green = AVAILABLE
   - 🟠 Orange = IN_USE
   - 🔴 Red = MAINTENANCE
4. **Speed Display**: Only shown for vehicles with speed > 0

## Success Criteria

✅ Customer can create bookings without errors
✅ Username is properly resolved to User entity
✅ Vehicle GPS locations are visible on map
✅ Map updates automatically every 5 seconds
✅ Vehicle info cards show complete details
✅ Status colors match vehicle states
✅ Navigation works between all tabs

## Next Steps

After successful testing:
1. Integrate with real GPS hardware/APIs
2. Add Google Maps or Mapbox integration
3. Implement WebSocket for instant updates
4. Add route visualization
5. Create admin dashboard for GPS monitoring
