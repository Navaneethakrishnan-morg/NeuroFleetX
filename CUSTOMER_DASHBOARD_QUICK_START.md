# Customer Dashboard - Quick Start Guide

## Start the Application

### 1. Start Backend (Terminal 1)
```bash
cd C:\Users\nk349\Documents\augment-projects\neuro\backend
mvn spring-boot:run
```
Wait for: `Started NeuroFleetXApplication`

### 2. Start Frontend (Terminal 2)
```bash
cd C:\Users\nk349\Documents\augment-projects\neuro\frontend
npm start
```
Browser will open automatically at `http://localhost:3000`

## Quick Test Scenarios

### Scenario 1: Smart Booking
1. **Login** as customer (username: `customer`, password: as set)
2. **Navigate** to "Smart Booking" tab (default)
3. **Filter** vehicles:
   - Select "SUV" from Vehicle Type
   - Check "Electric Only"
   - Select "5+" seats
4. **View** AI recommendations (top section with green badges)
5. **Click** "Book Now" on any vehicle
6. **Select** start and end dates
7. **Fill** pickup/dropoff locations
8. **Confirm** booking

**Expected Result:** Booking created, redirected to Active Bookings

---

### Scenario 2: Track Active Booking
1. **Navigate** to "Active Bookings" tab
2. **View** your bookings with:
   - Status badge (PENDING/CONFIRMED/IN_PROGRESS)
   - Start/End times
   - Countdown timer for upcoming bookings
3. **For IN_PROGRESS bookings:**
   - Click "Track Vehicle Location"
   - View live map with vehicle position
   - Close map modal

**Expected Result:** See real-time vehicle location on map

---

### Scenario 3: Create Support Ticket
1. **Navigate** to "Support" tab
2. **Click** "Create Support Ticket" button
3. **Fill** the form:
   ```
   Subject: Vehicle not starting
   Description: The vehicle engine won't start after booking
   Category: Vehicle Issue
   Priority: High
   ```
4. **Submit** ticket
5. **View** ticket in "My Support Tickets" section
6. **Check** status badge (OPEN)

**Expected Result:** Ticket created and visible with OPEN status

---

### Scenario 4: View Trip History
1. **Navigate** to "Trip History" tab
2. **View** statistics:
   - Total Trips
   - Total Distance
   - Average Rating
3. **Review** completed trips with:
   - Trip date and vehicle
   - Pickup/dropoff locations
   - Duration and fare
   - Your ratings
4. **Click** "View Receipt" (optional)

**Expected Result:** See all past completed trips

---

### Scenario 5: Check Payments
1. **Navigate** to "Payments" tab
2. **View** statistics:
   - Total Spent
   - Total Transactions
   - Average Per Booking
3. **Review** transactions:
   - Payment method
   - Date and amount
   - Booking reference
4. **Click** "Download Invoice" (optional)

**Expected Result:** See complete payment history

---

### Scenario 6: Edit Profile
1. **Navigate** to "Profile" tab
2. **Update** information:
   - Change name
   - Update phone number
   - Modify address
3. **Click** "Save Changes"
4. **See** success notification

**Expected Result:** Profile updated, green notification appears

## Key Features to Test

### Smart Booking Filters
- [x] Vehicle Type dropdown works
- [x] Electric Only checkbox filters correctly
- [x] Seat capacity filter applies
- [x] Sort by options work (Recommendation, Price, Capacity)
- [x] Results count updates dynamically

### Active Bookings
- [x] Shows only PENDING, CONFIRMED, IN_PROGRESS bookings
- [x] Displays accurate Start/End times
- [x] Countdown timer for upcoming bookings
- [x] Track button only for IN_PROGRESS
- [x] Statistics cards show correct counts

### Support System
- [x] Create ticket form works
- [x] All categories available
- [x] Priority levels work
- [x] Ticket appears in list immediately
- [x] Status badges display correctly

### Live Tracking
- [x] Map modal opens for IN_PROGRESS bookings
- [x] Vehicle marker shows on map
- [x] Close button works
- [x] Map is interactive

## Troubleshooting

### Backend Issues
**Problem:** Backend won't start
**Solution:**
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

**Problem:** Port 8080 already in use
**Solution:**
```bash
# Find and kill process on port 8080
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F
```

### Frontend Issues
**Problem:** npm start fails
**Solution:**
```bash
cd frontend
rm -rf node_modules package-lock.json
npm install
npm start
```

**Problem:** API calls failing (CORS errors)
**Solution:** Check backend is running on port 8080

### Data Issues
**Problem:** No bookings showing
**Solution:** Create a booking first from Smart Booking tab

**Problem:** No vehicles available
**Solution:** Backend should have seeded data. Check database.

## API Testing (Optional)

### Test Backend Endpoints Directly

1. **Get Available Vehicles:**
```bash
curl http://localhost:8080/api/customer/vehicles
```

2. **Search Vehicles:**
```bash
curl "http://localhost:8080/api/customer/bookings/search?username=customer&vehicleType=SUV&isElectric=true"
```

3. **Get Customer Bookings:**
```bash
curl http://localhost:8080/api/customer/bookings?username=customer
```

4. **Get Support Tickets:**
```bash
curl http://localhost:8080/api/customer/support/tickets?username=customer
```

## Manager Dashboard Integration

The customer support tickets are visible to managers:

1. **Login** as manager
2. **Navigate** to Support/Tickets section
3. **View** all customer tickets
4. **Assign** ticket to yourself
5. **Add** resolution
6. **Mark** as RESOLVED

Customer will see the resolution in their Support tab.

## Visual Verification

### Smart Booking Tab
- ✅ AI Recommended section at top (if electric vehicles available)
- ✅ Filter section with 4 dropdowns
- ✅ Vehicle cards with glass-morphism effect
- ✅ Green "Available" badges
- ✅ Blue "Book Now" buttons

### Active Bookings Tab
- ✅ 3 statistics cards at top (cyan, green, purple)
- ✅ Booking cards with vehicle image placeholder
- ✅ Color-coded status badges with pulse animation
- ✅ Formatted date/time display
- ✅ Track button for IN_PROGRESS bookings

### Support Tab
- ✅ Create button in top right
- ✅ My Tickets section (if tickets exist)
- ✅ Live Chat on left
- ✅ Contact Us and FAQ on right
- ✅ Modal form for creating tickets

## Success Criteria

Your customer dashboard is working correctly if:

1. ✅ All 6 tabs are accessible
2. ✅ Smart Booking shows vehicles with filters
3. ✅ Active Bookings displays with Start/End times
4. ✅ Trip History shows past bookings
5. ✅ Payments shows transactions
6. ✅ Profile can be edited
7. ✅ Support tickets can be created
8. ✅ Live tracking opens for IN_PROGRESS bookings
9. ✅ All status badges show correct colors
10. ✅ No console errors in browser

## Performance Notes

- **Auto-refresh:** Active Bookings refreshes every 30 seconds
- **Real-time:** Live map updates vehicle position
- **Optimized:** Components only re-render when data changes
- **Responsive:** Works on mobile, tablet, desktop

## Next Steps

After testing the customer dashboard:

1. Test manager dashboard to see support tickets
2. Test driver dashboard for trip management
3. Test admin dashboard for system overview
4. Review analytics and reports

## Support

If you encounter issues:
1. Check browser console for errors
2. Check backend terminal for API errors
3. Verify database has seeded data
4. Ensure ports 3000 and 8080 are not blocked

## Summary

The customer dashboard is production-ready with:
- ✅ Smart AI-powered booking system
- ✅ Real-time tracking for active bookings
- ✅ Complete trip and payment history
- ✅ Profile management
- ✅ Integrated support ticket system
- ✅ Modern, responsive UI with animations
- ✅ Full backend API integration

Enjoy testing! 🚗✨
