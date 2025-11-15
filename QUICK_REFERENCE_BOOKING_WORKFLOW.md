# Quick Reference: Booking Approval Workflow

## 🚀 Quick Start

### Start the Application
```bash
# Terminal 1 - Backend
cd backend
mvn spring-boot:run

# Terminal 2 - Frontend
cd frontend
npm start
```

---

## 👥 User Credentials

| Role | Email | Password |
|------|-------|----------|
| Customer | customer1@neurofleetx.com | admin123 |
| Manager | manager@neurofleetx.com | admin123 |
| Driver | driver1@neurofleetx.com | admin123 |

---

## 📊 Status Flow

```
PENDING_MANAGER_APPROVAL → CONFIRMED → DRIVER_ASSIGNED → TRIP_STARTED → COMPLETED
```

---

## 🔄 Workflow Steps

### 1️⃣ Customer Creates Booking
- **Dashboard:** Customer Portal → Booking Section
- **Status:** PENDING_MANAGER_APPROVAL
- **Action:** Fill form and submit

### 2️⃣ Manager Approves
- **Dashboard:** Manager Portal → Pending Bookings (Default Tab)
- **Status:** CONFIRMED
- **Action:** Click "✓ Approve" button

### 3️⃣ Manager Assigns Driver
- **Dashboard:** Driver Assignment Modal (Auto-opens)
- **Status:** DRIVER_ASSIGNED
- **Action:** Select driver → Click "Assign Driver"

### 4️⃣ Driver Starts Trip
- **Dashboard:** Driver Portal → My Trips
- **Status:** TRIP_STARTED
- **Action:** Click "🚀 Start Trip"

---

## 🔌 API Endpoints

### Manager APIs
```http
GET  /api/manager/bookings/pending
PUT  /api/manager/bookings/{id}/approve
PUT  /api/manager/bookings/{id}/assign-driver?driverId={id}
GET  /api/manager/drivers/available
```

### Driver APIs
```http
GET  /api/driver/bookings?username={username}
PUT  /api/driver/bookings/{id}/start-trip
```

### Customer APIs
```http
POST /api/customer/bookings?username={username}
GET  /api/customer/bookings?username={username}
```

---

## 🧪 Quick Test

### Using cURL
```bash
# 1. Create Booking
curl -X POST "http://localhost:8080/api/customer/bookings?username=customer1" \
  -H "Content-Type: application/json" \
  -d '{"vehicle":{"id":1},"startTime":"2025-11-15T10:00:00","endTime":"2025-11-15T14:00:00","pickupLocation":"Test Pickup","dropoffLocation":"Test Dropoff"}'

# 2. Get Pending Bookings
curl "http://localhost:8080/api/manager/bookings/pending"

# 3. Get Available Drivers
curl "http://localhost:8080/api/manager/drivers/available"

# 4. Approve Booking (replace {id})
curl -X PUT "http://localhost:8080/api/manager/bookings/{id}/approve"

# 5. Assign Driver (replace {bookingId} and {driverId})
curl -X PUT "http://localhost:8080/api/manager/bookings/{bookingId}/assign-driver?driverId={driverId}"

# 6. Start Trip
curl -X PUT "http://localhost:8080/api/driver/bookings/{id}/start-trip"
```

---

## 📁 Key Files Modified

### Backend
```
✓ backend/src/main/java/com/neurofleetx/model/Booking.java
✓ backend/src/main/java/com/neurofleetx/repository/BookingRepository.java
✓ backend/src/main/java/com/neurofleetx/service/BookingService.java
✓ backend/src/main/java/com/neurofleetx/controller/BookingController.java
```

### Frontend
```
✓ frontend/src/pages/manager/PendingBookings.js (NEW)
✓ frontend/src/pages/ManagerDashboardNew.js
✓ frontend/src/pages/driver/MyTrips.js
```

---

## 🎯 Features Implemented

✅ Manager approval required for all bookings  
✅ Real-time status tracking  
✅ Driver assignment with dropdown selection  
✅ Driver view of assigned bookings  
✅ Start trip button functionality  
✅ Status validation at each step  
✅ Role-based access control  
✅ Comprehensive error handling  

---

## 🐛 Troubleshooting

### Issue: Booking not showing in Pending Bookings
**Solution:** Check booking status is `PENDING_MANAGER_APPROVAL`

### Issue: Cannot assign driver
**Solution:** Ensure booking is in `CONFIRMED` status first

### Issue: Start Trip button not working
**Solution:** Verify booking status is `DRIVER_ASSIGNED`

### Issue: API returns 401 Unauthorized
**Solution:** Check JWT token is valid and user has correct role

---

## 📞 Support

For issues or questions:
1. Check logs in backend console
2. Check browser console for frontend errors
3. Review BOOKING_APPROVAL_WORKFLOW_GUIDE.md for detailed docs
4. Check IMPLEMENTATION_SUMMARY_BOOKING_WORKFLOW.md for technical details

---

## 🔧 Development Commands

```bash
# Compile Backend
cd backend && mvn clean compile

# Run Backend Tests
cd backend && mvn test

# Build Frontend
cd frontend && npm run build

# Run Frontend in Dev Mode
cd frontend && npm start
```

---

## 📈 Next Steps

1. Test the complete workflow end-to-end
2. Add WebSocket for real-time notifications
3. Implement smart driver assignment algorithm
4. Add GPS tracking during trip
5. Implement rating system
6. Add SMS/Email notifications

---

*Last Updated: November 14, 2025*
*NeuroFleetX v1.0*
