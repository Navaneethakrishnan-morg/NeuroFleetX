# Troubleshooting Guide: Booking Workflow Issues

## Issue: Bookings Not Appearing in Manager Portal

### Possible Causes and Solutions

#### 1. **Backend Not Running**
**Symptoms:** API calls fail, network errors in browser console

**Solution:**
```bash
cd backend
mvn spring-boot:run
```

Verify backend is running at: `http://localhost:8080`

---

#### 2. **Frontend Not Properly Connected**
**Symptoms:** Booking created but not visible in manager dashboard

**Check:**
1. Open browser Developer Tools (F12)
2. Go to Network tab
3. Create a booking as customer
4. Look for POST request to `/api/customer/bookings`
5. Check response status and data

**Expected Response:**
```json
{
  "id": 1,
  "status": "PENDING_MANAGER_APPROVAL",
  "customer": {...},
  "vehicle": {...}
}
```

---

#### 3. **Database Not Updated**
**Symptoms:** Booking API returns success but not stored

**Check Database:**
```sql
-- Check if booking was created
SELECT * FROM bookings ORDER BY created_at DESC LIMIT 5;

-- Check booking status
SELECT id, status, customer_id, vehicle_id, created_at 
FROM bookings 
WHERE status = 'PENDING_MANAGER_APPROVAL';
```

---

#### 4. **Wrong Status Set on Creation**
**Symptoms:** Booking exists but not in pending list

**Verify:**
```bash
# Check what status is being set
curl http://localhost:8080/api/customer/bookings?username=customer1
```

Look for `"status"` field - it should be `"PENDING_MANAGER_APPROVAL"`

**Fix:** Ensure BookingService.createBooking() sets correct status:
```java
booking.setStatus(Booking.BookingStatus.PENDING_MANAGER_APPROVAL);
```

---

#### 5. **Manager API Not Fetching Correctly**
**Symptoms:** Database has bookings but manager dashboard shows empty

**Test Manager API:**
```bash
# Test pending bookings endpoint
curl http://localhost:8080/api/manager/bookings/pending

# Should return array of bookings with PENDING_MANAGER_APPROVAL status
```

---

#### 6. **Frontend State Not Refreshing**
**Symptoms:** Data exists but UI doesn't update

**Solution:**
1. Hard refresh browser (Ctrl+Shift+R / Cmd+Shift+R)
2. Clear browser cache
3. Check console for JavaScript errors

---

## Step-by-Step Debugging

### Step 1: Verify Backend is Running
```bash
curl http://localhost:8080/api/manager/drivers/available
```

Expected: List of drivers (should see driver1, driver2)

### Step 2: Create a Test Booking
```bash
curl -X POST "http://localhost:8080/api/customer/bookings?username=customer1" \
  -H "Content-Type: application/json" \
  -d '{
    "vehicle": {"id": 1},
    "startTime": "2025-11-16T10:00:00",
    "endTime": "2025-11-16T14:00:00",
    "pickupLocation": "Test Pickup Location",
    "dropoffLocation": "Test Dropoff Location"
  }'
```

Expected: Response with booking details and `"status": "PENDING_MANAGER_APPROVAL"`

### Step 3: Check Manager Pending List
```bash
curl http://localhost:8080/api/manager/bookings/pending
```

Expected: Array containing the booking you just created

### Step 4: Frontend Browser Console Check
1. Open browser Developer Tools (F12)
2. Go to Console tab
3. Look for errors or failed API calls
4. Check Network tab for API responses

### Step 5: Check Manager Dashboard Component
1. Login as manager
2. Open browser console
3. Look for console.log from PendingBookings component:
   - "Pending bookings loaded: [array]"
   - Should show your test booking

---

## Common Error Messages

### "Booking not found"
**Cause:** Invalid booking ID or booking doesn't exist
**Solution:** Verify booking ID in database

### "Vehicle is not available for the selected time period"
**Cause:** Vehicle already booked for that time
**Solution:** Choose different vehicle or time slot

### "User not found"
**Cause:** Invalid username
**Solution:** Verify customer username exists in users table

### "Booking is not in pending manager approval status"
**Cause:** Trying to approve booking with wrong status
**Solution:** Check current booking status first

### "Selected user is not a driver"
**Cause:** Trying to assign non-driver to booking
**Solution:** Ensure selected user has role='DRIVER'

---

## Database Queries for Debugging

### Check All Bookings
```sql
SELECT 
  b.id, 
  b.status, 
  u.full_name as customer, 
  v.vehicle_number as vehicle,
  b.created_at
FROM bookings b
LEFT JOIN users u ON b.customer_id = u.id
LEFT JOIN vehicles v ON b.vehicle_id = v.id
ORDER BY b.created_at DESC;
```

### Check Pending Approvals
```sql
SELECT 
  b.id, 
  b.status, 
  u.full_name as customer, 
  u.email,
  v.model as vehicle_model,
  b.pickup_location,
  b.dropoff_location,
  b.start_time,
  b.total_price
FROM bookings b
LEFT JOIN users u ON b.customer_id = u.id
LEFT JOIN vehicles v ON b.vehicle_id = v.id
WHERE b.status = 'PENDING_MANAGER_APPROVAL';
```

### Check Driver Assignments
```sql
SELECT 
  b.id, 
  b.status,
  c.full_name as customer,
  d.full_name as driver,
  v.vehicle_number
FROM bookings b
LEFT JOIN users c ON b.customer_id = c.id
LEFT JOIN users d ON b.driver_id = d.id
LEFT JOIN vehicles v ON b.vehicle_id = v.id
WHERE b.driver_id IS NOT NULL;
```

---

## Frontend Debugging Tips

### Enable Detailed Logging
In `PendingBookings.js`, verify these console.log statements exist:
```javascript
console.log('Pending bookings loaded:', response.data);
console.log('Available drivers loaded:', response.data);
console.log('Booking approved:', approvedBooking);
console.log('Driver assigned:', response.data);
```

### Check React DevTools
1. Install React DevTools extension
2. Open component tree
3. Find PendingBookings component
4. Check state values:
   - pendingBookings array
   - availableDrivers array
   - loading state

---

## API Testing with Postman

### Import these requests:

**1. Create Booking**
```
POST http://localhost:8080/api/customer/bookings?username=customer1
Content-Type: application/json

{
  "vehicle": {"id": 1},
  "startTime": "2025-11-16T10:00:00",
  "endTime": "2025-11-16T14:00:00",
  "pickupLocation": "123 Main St",
  "dropoffLocation": "456 Park Ave"
}
```

**2. Get Pending Bookings**
```
GET http://localhost:8080/api/manager/bookings/pending
```

**3. Approve Booking**
```
PUT http://localhost:8080/api/manager/bookings/1/approve
```

**4. Assign Driver**
```
PUT http://localhost:8080/api/manager/bookings/1/assign-driver?driverId=3
```

**5. Get Driver Bookings**
```
GET http://localhost:8080/api/driver/bookings?username=driver1
```

**6. Start Trip**
```
PUT http://localhost:8080/api/driver/bookings/1/start-trip
```

---

## Reset and Clean Start

If nothing works, try a clean reset:

### 1. Stop All Servers
```bash
# Stop backend (Ctrl+C)
# Stop frontend (Ctrl+C)
```

### 2. Clean Build
```bash
# Backend
cd backend
mvn clean install

# Frontend
cd frontend
npm install
```

### 3. Reset Database (if using H2/SQLite)
```bash
# Delete database file
rm backend/neurofleetx.db
```

### 4. Restart Application
```bash
# Terminal 1 - Backend
cd backend
mvn spring-boot:run

# Terminal 2 - Frontend  
cd frontend
npm start
```

### 5. Test Again
Follow Step-by-Step Debugging section above

---

## Still Having Issues?

### Collect This Information:

1. **Backend logs** (last 50 lines)
2. **Browser console errors** (screenshot)
3. **Network tab** showing API calls and responses
4. **Database query results** for bookings table
5. **Frontend version** (check package.json)
6. **Backend version** (check pom.xml)

### Check These Files:

1. **Backend:**
   - `Booking.java` - Verify PENDING_MANAGER_APPROVAL status exists
   - `BookingService.java` - Verify createBooking sets correct status
   - `BookingRepository.java` - Verify findByDriver method exists

2. **Frontend:**
   - `api.js` - Verify managerService exported
   - `PendingBookings.js` - Verify imports and API calls
   - `BookingCalendar.js` - Verify booking creation payload

---

## Quick Verification Checklist

- [ ] Backend running on port 8080
- [ ] Frontend running on port 3000
- [ ] Database file exists and is not corrupted
- [ ] User accounts exist (customer1, manager, driver1)
- [ ] Vehicles exist in database
- [ ] No CORS errors in browser console
- [ ] No authentication errors (401)
- [ ] Booking status enum includes new statuses
- [ ] BookingService sets PENDING_MANAGER_APPROVAL
- [ ] Manager API endpoints exist and respond
- [ ] Driver API endpoints exist and respond
- [ ] All npm packages installed (node_modules exists)
- [ ] All Maven dependencies resolved

---

*Last Updated: November 14, 2025*
