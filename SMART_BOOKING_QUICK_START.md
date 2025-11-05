# Smart Booking - Quick Start Guide

## 🚀 Quick Start (5 Minutes)

### Step 1: Run Database Migration (One-time)
```bash
# Option 1: Using MySQL command line
mysql -u root -p neurofleetx < database-customer-preferences.sql

# Option 2: Using MySQL Workbench
# Open database-customer-preferences.sql and execute it
```

### Step 2: Start the Application
```bash
# Windows
start-smart-booking.bat

# Manual Start
# Terminal 1: Backend
cd backend
mvn spring-boot:run

# Terminal 2: Frontend
cd frontend
npm start
```

### Step 3: Login and Test
1. Open browser to `http://localhost:3000`
2. Login with test customer:
   - Username: `customer1`
   - Password: `admin123`
3. Click on **"⚡ Smart Booking"** tab
4. Explore the AI-powered recommendations!

## 🎯 Feature Walkthrough

### Test Flow 1: First-Time User Experience
1. Login as `customer2` (fresh account)
2. Go to Smart Booking
3. Notice vehicles with base recommendations
4. Apply filters (e.g., "SUV", "5+ seats", "Electric Only")
5. Click "Search Vehicles"
6. Select a vehicle and click "Book Now"
7. Choose a date range
8. Select an available time slot
9. Fill in pickup/dropoff locations
10. Confirm booking
11. Check "My Bookings" to see your reservation

### Test Flow 2: Personalized Recommendations
1. Login as `customer1` (has booking history)
2. Go to Smart Booking
3. Notice AI-recommended vehicles at the top with:
   - Green "AI Recommended" badge
   - Match score percentage
   - Personalized reasons
4. Higher scores for vehicles matching past preferences

### Test Flow 3: Availability & Pricing
1. Book a vehicle for 4 hours (e.g., 10 AM - 2 PM)
2. Try to book same vehicle for overlapping time
3. Notice that slot is unavailable
4. See pricing differences:
   - Sedan: $25/hr → $100 for 4 hours
   - SUV: $37.50/hr → $150 for 4 hours
   - Electric bonus: +10%

## 🧪 Testing Checklist

### Filters & Search
- [ ] Vehicle type filter (Sedan, SUV, Van, etc.)
- [ ] Capacity filter (2+, 4+, 5+, 7+, 10+)
- [ ] Power type filter (Electric/Non-Electric/All)
- [ ] Sort by recommendation/price/capacity
- [ ] Search updates results in real-time

### Recommendations
- [ ] AI badge shows for 70%+ match
- [ ] Match score displayed correctly
- [ ] Recommendation reasons are relevant
- [ ] Recommended section separated from others
- [ ] No recommendations message when score < 70%

### Booking Calendar
- [ ] Date range selector works
- [ ] Availability info shows correctly
- [ ] Time slots display with prices
- [ ] Booked slots marked unavailable
- [ ] Slot selection highlights correctly
- [ ] Booking form appears after slot selection

### Booking Flow
- [ ] All required fields validated
- [ ] Price calculation accurate
- [ ] Booking summary correct
- [ ] Success message appears
- [ ] Booking appears in "My Bookings"
- [ ] Vehicle marked as booked for time period

### Pricing Accuracy
- [ ] Sedan: $25/hr
- [ ] SUV: $37.50/hr
- [ ] Van: $50/hr
- [ ] Truck: $62.50/hr
- [ ] Bus: $75/hr
- [ ] Bike: $12.50/hr
- [ ] Electric: +10% premium
- [ ] Duration multiplier correct

## 📊 Sample Test Data

### Test Customers
```
customer1@neurofleetx.com / admin123
- Has booking history
- Should see personalized recommendations

customer2@neurofleetx.com / admin123
- Fresh account
- Will see base recommendations
```

### Test Vehicles
```
Tesla Model S (NF-001)
- Type: SEDAN
- Electric: Yes
- Capacity: 5 seats
- Price: $27.50/hr (25 + 10% electric)
- Status: AVAILABLE

Ford Explorer (NF-005)
- Type: SUV
- Electric: No
- Capacity: 7 seats
- Price: $37.50/hr
- Status: AVAILABLE
```

## 🔍 API Testing (Optional)

### Using curl or Postman

**Search Vehicles:**
```bash
curl -X POST http://localhost:8080/api/customer/vehicles/search?username=customer1 \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleType": "ALL",
    "isElectric": null,
    "minCapacity": null,
    "maxCapacity": null,
    "sortBy": "recommendation"
  }'
```

**Check Availability:**
```bash
curl -X POST http://localhost:8080/api/customer/bookings/availability \
  -H "Content-Type: application/json" \
  -d '{
    "vehicleId": 1,
    "startDate": "2025-11-05",
    "endDate": "2025-11-12"
  }'
```

**Create Booking:**
```bash
curl -X POST http://localhost:8080/api/customer/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "customer": {"username": "customer1"},
    "vehicle": {"id": 1},
    "startTime": "2025-11-05T10:00:00",
    "endTime": "2025-11-05T14:00:00",
    "pickupLocation": "123 Main St, New York",
    "dropoffLocation": "456 Park Ave, New York",
    "totalPrice": 110.00
  }'
```

## 🐛 Common Issues & Solutions

### Backend won't start
```
Problem: Port 8080 already in use
Solution: Kill existing Java processes or change port in application.properties
```

### Frontend won't start
```
Problem: Port 3000 already in use
Solution: Kill existing Node processes or use different port (set PORT=3001)
```

### No recommendations showing
```
Problem: Fresh database without customer history
Solution: Complete 1-2 bookings to generate preferences
```

### Booking fails
```
Problem: Vehicle already booked or not available
Solution: 
1. Check vehicle status in database
2. Try different time slot
3. Verify dates are in future
```

### Prices seem wrong
```
Problem: Calculation error
Solution: Check BookingService TYPE_MULTIPLIERS map
```

## 📈 Expected Results

### Recommendation Scores
- **90-100**: Perfect match (rare, after many bookings)
- **80-89**: Excellent match
- **70-79**: Good match (shows in recommended section)
- **60-69**: Fair match
- **<60**: Low match

### Booking Success Rate
- First attempt: ~90% (if vehicle available)
- Overlapping bookings: 0% (intentional)
- Invalid data: 0% (validation works)

### Performance
- Search results: < 1 second
- Availability check: < 2 seconds
- Booking creation: < 1 second
- Page load: < 2 seconds

## ✅ Success Criteria

You've successfully set up the Smart Booking module if:

1. ✅ You can see the "⚡ Smart Booking" tab
2. ✅ Filters work and update results
3. ✅ AI recommendations appear with badges
4. ✅ Match scores are calculated and displayed
5. ✅ Booking calendar shows available slots
6. ✅ You can complete a full booking
7. ✅ Booking appears in "My Bookings"
8. ✅ Prices calculate correctly
9. ✅ Overlapping bookings are prevented
10. ✅ UI looks polished with glass effects

## 🎓 Learning the System

### Understanding Recommendations
The AI considers:
1. **Past behavior** (50% weight): Your booking history
2. **Vehicle quality** (30% weight): Health score, ratings
3. **Preferences** (20% weight): Type, electric, capacity

### How Scores Improve
- Book a sedan → Future sedan scores +20
- Book electric → Future electric scores +15
- Consistent capacity → Matching capacity +10
- Rate vehicles → Higher rated vehicles score better

### Pricing Logic
```
Base Rate = $25/hr
Vehicle Multiplier:
  - SEDAN: 1.0x
  - SUV: 1.5x
  - VAN: 2.0x
  - TRUCK: 2.5x
  - BUS: 3.0x
  - BIKE: 0.5x
Electric Premium: +10%

Example:
Electric SUV for 4 hours
= $25 * 1.5 * 1.1 * 4
= $165
```

## 📞 Support

If you encounter issues:
1. Check `CUSTOMER_BOOKING_GUIDE.md` for detailed docs
2. Review browser console for errors
3. Check backend logs for exceptions
4. Verify database connection
5. Ensure all migrations ran successfully

## 🎉 What's Next?

After testing the Smart Booking module:
1. Explore other dashboard features
2. Try booking multiple vehicles
3. Test with different customer accounts
4. Review the recommendation algorithm
5. Customize the UI to your needs

---

**Enjoy your intelligent booking experience!** 🚗⚡
