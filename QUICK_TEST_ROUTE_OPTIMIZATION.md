# Quick Test Guide - AI Route & Load Optimization

## 🚀 Quick Start (3 Steps)

### Step 1: Start the System
```bash
# Option A: Use the batch script (Windows)
start-route-optimization.bat

# Option B: Manual start
# Terminal 1 - Backend
cd backend
mvn spring-boot:run

# Terminal 2 - Frontend  
cd frontend
npm start
```

### Step 2: Access Route Optimization
Open in browser:
- **Manager Portal**: http://localhost:3000/manager/route-optimization
- **Admin Portal**: http://localhost:3000/admin/route-optimization

### Step 3: Test Route Generation
1. Click "🎯 Generate Routes" (uses default NYC coordinates)
2. View 3 color-coded routes on the map:
   - 🟢 Green = Fastest
   - 🔵 Blue = Energy Efficient  
   - 🟠 Orange = Balanced
3. Click routes to select and view details
4. Click "🤖 Auto-Assign Loads" to test vehicle assignment

---

## ✅ Quick Verification Checklist

### Backend Check
- [ ] Backend running on http://localhost:8080
- [ ] Test endpoint: http://localhost:8080/api/routes
- [ ] Should return empty array `[]` initially

### Frontend Check
- [ ] Frontend running on http://localhost:3000
- [ ] Route Optimization page loads
- [ ] Map displays (OpenStreetMap tiles)
- [ ] Form inputs visible
- [ ] Buttons clickable

### Route Generation Test
- [ ] Click "Generate Routes" button
- [ ] See 3 colored polylines on map
- [ ] Route cards appear below map
- [ ] Analytics dashboard shows metrics
- [ ] Can click routes to select them

### Load Management Test
- [ ] Load Management panel visible on right
- [ ] Shows sample loads (if database initialized)
- [ ] Click "Auto-Assign Loads" button
- [ ] See status updates in panel

---

## 🎯 Sample Test Data

### Test Route 1 (Default - NYC to Times Square)
```
Start Location: New York City
Start Coordinates: 40.7128, -74.0060
End Location: Times Square
End Coordinates: 40.7580, -73.9855
```

### Test Route 2 (Brooklyn to Manhattan)
```
Start Location: Brooklyn
Start Coordinates: 40.6782, -73.9442
End Location: Manhattan
End Coordinates: 40.7831, -73.9712
```

### Test Route 3 (Queens to Bronx)
```
Start Location: Queens
Start Coordinates: 40.7282, -73.7949
End Location: Bronx
End Coordinates: 40.8448, -73.8648
```

---

## 🔧 API Testing (Optional)

### Test Route Optimization Endpoint
```bash
curl -X POST http://localhost:8080/api/routes/optimize \
  -H "Content-Type: application/json" \
  -d '{
    "startLocation": "New York City",
    "endLocation": "Times Square",
    "startLatitude": 40.7128,
    "startLongitude": -74.0060,
    "endLatitude": 40.7580,
    "endLongitude": -73.9855,
    "includeTrafficData": true,
    "generateAlternatives": true
  }'
```

### Get All Routes
```bash
curl http://localhost:8080/api/routes
```

### Get All Loads
```bash
curl http://localhost:8080/api/loads
```

### Auto-Assign Loads
```bash
curl -X POST http://localhost:8080/api/loads/auto-assign
```

---

## 🎨 What You Should See

### On the Map
- 3 colored polylines showing different route options
- Start marker (pin) at beginning of routes
- End marker (pin) at destination
- Smooth route curves between waypoints

### In Route Cards
- Distance in kilometers
- ETA in minutes
- Energy cost in dollars
- Traffic level badge (color-coded)
- Optimization type badge (color-coded)
- Optimized path with waypoints

### In Analytics Dashboard
- "3" routes analyzed
- Time saved in minutes
- Energy saved percentage
- Algorithm name: "Dijkstra + ML ETA Predictor"

### In Load Management
- Load ID numbers
- Weight in kg
- Destination names
- Priority badges (Urgent/High/Medium/Low)
- Status badges (Pending/Assigned/In Transit/Delivered)

---

## 🐛 Troubleshooting

### Map Not Showing
- Check browser console for errors
- Verify internet connection (needed for OpenStreetMap tiles)
- Clear browser cache and reload

### No Routes Generated
- Check backend is running on port 8080
- Open browser console to see API errors
- Verify coordinates are valid numbers

### Backend Errors
- Check Java 17 is installed: `java -version`
- Verify Maven is installed: `mvn -version`
- Check port 8080 is not in use
- Review backend console logs

### Frontend Errors
- Check Node.js is installed: `node -version`
- Verify dependencies installed: `npm install`
- Check port 3000 is not in use
- Clear npm cache: `npm cache clean --force`

---

## 📊 Expected Performance

- **Route Generation Time**: 1-2 seconds
- **Map Load Time**: 2-3 seconds
- **Routes Displayed**: 3 alternatives
- **API Response Time**: < 500ms
- **Database Queries**: Indexed for fast retrieval

---

## 🎯 Key Features to Test

1. **Route Optimization**
   - [x] Generate multiple route options
   - [x] Color-coded by optimization type
   - [x] Interactive map selection
   - [x] Dynamic ETA calculation

2. **Load Management**
   - [x] View pending loads
   - [x] Auto-assign to vehicles
   - [x] Track assignment status
   - [x] Priority-based ordering

3. **Map Visualization**
   - [x] OpenStreetMap integration
   - [x] Polyline routes
   - [x] Markers for start/end
   - [x] Click to select routes

4. **Data Persistence**
   - [x] Routes saved to SQLite
   - [x] Loads saved to SQLite
   - [x] Data reloads after refresh
   - [x] History tracking

---

## ✨ Success Indicators

You'll know it's working when you see:
- ✅ Map loads with street view
- ✅ 3 colored routes appear after clicking "Generate Routes"
- ✅ Route cards show distance, ETA, and energy cost
- ✅ Clicking routes highlights them on map
- ✅ Analytics shows time/energy savings
- ✅ Load panel shows test data
- ✅ Auto-assign updates load statuses

---

## 📞 Support

If you encounter issues:
1. Check `ROUTE_OPTIMIZATION_GUIDE.md` for detailed documentation
2. Review `ROUTE_OPTIMIZATION_IMPLEMENTATION.md` for architecture details
3. Verify all services are running
4. Check browser console for JavaScript errors
5. Review backend console for Spring Boot errors

---

## 🎉 Next Steps

After successful testing:
1. Customize coordinates for your city
2. Add real vehicles to database
3. Create actual loads
4. Test auto-assignment with real data
5. Integrate with live GPS tracking
6. Add more optimization types
7. Connect to real traffic APIs
8. Export route reports

---

**Enjoy optimizing your fleet routes!** 🚀🗺️
