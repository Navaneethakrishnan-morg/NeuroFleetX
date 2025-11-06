# Quick Start: Smart Booking & Live Tracking

## 🚀 Start the Application

### Option 1: Use the Startup Script (Recommended)
```batch
start-smart-features.bat
```

### Option 2: Manual Start
```batch
# Terminal 1: Start Backend
cd backend
mvn spring-boot:run

# Terminal 2: Start Frontend (wait for backend to start first)
cd frontend
npm start
```

## 📱 Access the Features

1. **Open Browser**: http://localhost:3000
2. **Login**: 
   - Username: `customer1`
   - Password: `admin123`
3. **Navigate**: Look for these new tabs in the customer dashboard:
   - **⚡ Smart Booking** - AI-powered vehicle recommendations
   - **📍 Live Tracking** - Real-time vehicle tracking on street maps

## ⚡ Smart Booking

### What It Does:
- Recommends vehicles based on your booking history
- Shows AI match scores (70%+ = recommended)
- Filters by type, capacity, and power source
- Dynamic pricing and real-time availability

### How to Use:
1. Click "⚡ Smart Booking" tab
2. Apply filters (optional):
   - Vehicle Type (Sedan, SUV, Van, etc.)
   - Seats (2+, 4+, 5+, 7+, 10+)
   - Power Type (Electric/Non-Electric/All)
   - Sort by (Recommendation/Price/Capacity)
3. Click "Search Vehicles"
4. View AI-recommended vehicles with green badges
5. Click "Book Now" on any vehicle
6. Select date range and time slot
7. Fill in pickup/dropoff locations
8. Complete booking!

### Key Features:
- ✨ AI recommendations with match scores
- 🎯 Personalized suggestions based on history
- 💰 Dynamic pricing by vehicle type
- 📅 Interactive booking calendar
- ⚡ Electric vehicle premium options

## 📍 Live Tracking

### What It Does:
- Shows all vehicles on real OpenStreetMap
- Updates every 5 seconds automatically
- Displays speed, location, battery/fuel levels
- Interactive markers with detailed info

### How to Use:
1. Click "📍 Live Tracking" tab
2. Wait for map to load (1-2 seconds)
3. View vehicles as emoji markers:
   - 🚗 Sedans
   - 🚙 SUVs
   - 🚐 Vans
   - 🚚 Trucks
   - 🚌 Buses
   - 🏍️ Bikes
4. Click any marker to see details
5. Use mouse wheel or +/- buttons to zoom
6. Click and drag to pan around
7. Watch vehicles move in real-time!

### Key Features:
- 🗺️ Real street maps from OpenStreetMap
- 🚗 Live vehicle positions with GPS
- 🔄 Auto-refresh every 5 seconds
- 📊 Detailed vehicle information
- 🎯 Status-based color coding
- ⚡ Speed badges for moving vehicles

## 🎨 Status Colors

- **🟢 Green**: Available for booking
- **🟠 Orange**: Currently in use
- **🔴 Red**: Under maintenance (not shown on live map)

## 💡 Tips

### For Smart Booking:
- Book 2-3 vehicles to improve recommendations
- Higher match scores = better fit for your needs
- Electric vehicles have 10% price premium
- Time slots shown in hourly increments

### For Live Tracking:
- Zoom in to see exact street locations
- Click markers for quick info popup
- Selected vehicle shows in side panel
- Orange status means vehicle is in motion
- Look for speed badges on moving vehicles

## 🔧 Troubleshooting

### Map Not Loading?
- Check internet connection (needs online map tiles)
- Wait 2-3 seconds for initial load
- Refresh browser if stuck

### No Vehicles Showing on Map?
Run GPS initialization:
```bash
curl -X POST http://localhost:8080/api/admin/vehicles/initialize-gps
```

### No Recommendations?
- Customer needs booking history
- Try with `customer1` who has existing bookings
- New users see base recommendations

### Backend Won't Start?
- Check if port 8080 is free
- Look for Java 17+ installation
- Check database connection

### Frontend Won't Start?
- Check if port 3000 is free
- Run `npm install` if needed
- Clear browser cache

## 📊 What to Expect

### Smart Booking:
- Search results: < 1 second
- AI scores calculated instantly
- Smooth animations
- Responsive on all devices

### Live Tracking:
- Map loads in 1-2 seconds
- Vehicles update every 5 seconds
- Smooth marker animations
- Works on mobile browsers

## 🎯 Success Indicators

You'll know it's working when you see:
- ✅ "⚡ Smart Booking" tab in customer dashboard
- ✅ "📍 Live Tracking" tab in customer dashboard
- ✅ AI recommendation badges on vehicles
- ✅ Match scores (percentage) displayed
- ✅ Map with street names visible
- ✅ Vehicle markers with emoji icons
- ✅ Auto-refresh counter updating
- ✅ Popups show on marker click

## 📖 More Information

For detailed documentation, see:
- `SMART_FEATURES_IMPLEMENTATION.md` - Complete technical docs
- `SMART_BOOKING_QUICK_START.md` - Detailed booking guide
- `STREET_MAP_QUICK_GUIDE.md` - Map usage guide

## 🎉 Enjoy!

You now have a fully functional smart booking system with AI recommendations and live vehicle tracking on real street maps!

**Pro Tip**: Try booking a vehicle through Smart Booking, then track it in real-time on the Live Tracking map!

---

**Questions?** Check the implementation docs or review the console logs for any errors.
