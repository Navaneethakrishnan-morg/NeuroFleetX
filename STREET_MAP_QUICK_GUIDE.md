# Street Map - Quick Start Guide

## What's New?

Your live vehicle tracking now uses **real street maps** from OpenStreetMap! See actual streets, buildings, and landmarks where your vehicles are located.

## How to Access

1. **Login** as a customer
2. Click **"📍 Live Tracking"** tab in the navigation menu
3. Wait for the map to load (1-2 seconds)

## Features

### 🗺️ Interactive Street Map
- Real streets and buildings from OpenStreetMap
- Zoom in/out with mouse wheel or +/- buttons
- Pan by clicking and dragging
- See actual landmarks and neighborhoods

### 🚗 Vehicle Markers
Each vehicle appears as an emoji based on its type:
- 🚗 **Sedans**
- 🚙 **SUVs**
- 🚐 **Vans**
- 🚚 **Trucks**
- 🚌 **Buses**
- 🏍️ **Bikes**

### 🏷️ Speed Badges
Moving vehicles show their current speed in a red badge:
- Example: "35 mph" badge on moving vehicles
- Only appears when vehicle is moving

### 📊 Vehicle Information
**Two ways to see vehicle details:**

1. **Quick Popup** - Click any marker
   - Vehicle name and model
   - Status (color-coded)
   - Type and capacity
   - Current speed
   - Battery/Fuel level

2. **Info Card** - Appears on side
   - Full vehicle details
   - GPS coordinates
   - All telemetry data
   - Click × to close

### 🔄 Real-Time Updates
- Map automatically refreshes every 5 seconds
- Watch vehicles move in real-time
- No need to reload the page

### 🎨 Color Legend
**Top-right corner shows status colors:**
- 🟢 **Green** = Available
- 🟠 **Orange** = In Use

## How to Use

### Viewing All Vehicles
1. When you open Live Tracking, the map centers on vehicles
2. Zoom out to see all vehicles in the area
3. Zoom in to see specific streets and locations

### Getting Vehicle Details
**Method 1: Click Marker**
- Click any vehicle emoji on the map
- Popup appears above the marker
- Shows key vehicle information
- Click elsewhere to close popup

**Method 2: View Info Card**
- Click marker to open side info card
- See detailed information
- GPS coordinates included
- Click × button to close

### Navigating the Map
- **Zoom In**: Scroll up or click + button
- **Zoom Out**: Scroll down or click - button
- **Pan**: Click and drag the map
- **Reset View**: Refresh the page

### Tracking a Specific Vehicle
1. Find the vehicle marker on the map
2. Click it to see current details
3. Watch it move every 5 seconds
4. Speed badge shows if it's moving

## Tips & Tricks

### 🎯 Best Practices
1. **Zoom Level**: Use zoom 13-15 for neighborhood view
2. **Multiple Vehicles**: Zoom out to see all at once
3. **Details**: Zoom in to see exact street locations
4. **Moving Vehicles**: Look for red speed badges

### 🔍 Finding Vehicles
- **Available vehicles**: Look for green status
- **In-use vehicles**: Look for orange status with speed badges
- **Clustered vehicles**: Zoom in to separate markers

### 📱 Mobile Usage
- Pinch to zoom (mobile devices)
- Swipe to pan
- Tap markers for popups
- Works on tablets and phones

## What You Can See

### Map Details
- ✅ Streets and roads
- ✅ Building outlines
- ✅ Parks and green spaces
- ✅ Water bodies
- ✅ Landmarks and points of interest
- ✅ Neighborhood names

### Vehicle Details (in Popups)
- ✅ Make and Model (e.g., "Toyota Camry")
- ✅ Vehicle Number (e.g., "NYC-001")
- ✅ Status (Available, In Use, etc.)
- ✅ Vehicle Type (Sedan, SUV, etc.)
- ✅ Passenger Capacity
- ✅ Current Speed (if moving)
- ✅ Battery Level (electric vehicles)
- ✅ Fuel Level (gas vehicles)

## Common Scenarios

### Scenario 1: Booking a Nearby Vehicle
1. Open Live Tracking
2. See all available vehicles (green markers)
3. Find closest one to your location
4. Note the vehicle number
5. Go to Smart Booking tab
6. Search for that vehicle

### Scenario 2: Tracking Your Booked Vehicle
1. After booking, start the journey
2. Go to Live Tracking
3. Find your vehicle (orange marker with speed)
4. Click to see current location and speed
5. Watch it move in real-time

### Scenario 3: Monitoring Fleet Activity
1. Open Live Tracking
2. Zoom out to see all vehicles
3. Count available (green) vs in-use (orange)
4. Click any to check details
5. Refresh updates every 5 seconds

## Troubleshooting

### Map Not Loading?
- **Check Internet**: Map requires internet connection
- **Wait**: Initial load takes 1-2 seconds
- **Refresh**: Press F5 to reload page
- **Browser**: Use modern browser (Chrome, Firefox, Safari)

### No Vehicles Showing?
- **Initialize GPS**: Contact admin to initialize vehicle GPS
- **Backend Running**: Make sure backend server is running
- **API Connection**: Check browser console for errors
- **Status Filter**: Vehicles must be AVAILABLE or IN_USE to show

### Markers Not Updating?
- **Wait**: Updates happen every 5 seconds
- **Movement**: Vehicles might not be moving
- **Connection**: Check network connection
- **Refresh**: Reload page if stuck

### Popup Not Appearing?
- **Click Again**: Try clicking marker again
- **Close Others**: Close other popups first
- **Zoom In**: Markers might be overlapping
- **Wait**: Ensure page fully loaded

## Technical Info

### Map Source
- **Provider**: OpenStreetMap
- **License**: Open Database License
- **Coverage**: Worldwide
- **Quality**: Community-maintained

### Update Frequency
- **Position Updates**: Every 5 seconds
- **Speed Updates**: Real-time (with position)
- **Status Changes**: Immediate (with position update)
- **Map Tiles**: Cached by browser

### GPS Accuracy
- **Precision**: ±10 meters (depends on GPS device)
- **Update Delay**: ~5 seconds maximum
- **Coordinates**: Decimal degrees format
- **Current Area**: New York City vicinity

## What's Next?

### Coming Soon:
1. 🛣️ **Route Visualization** - See the path vehicles are taking
2. 🚦 **Traffic Layer** - Real-time traffic conditions
3. 🌙 **Dark Mode Map** - Night-friendly map style
4. 📍 **Geofencing** - Zones and boundaries
5. 🔥 **Heatmaps** - Popular areas visualization
6. ⏱️ **ETA Display** - Estimated arrival times
7. 📊 **Historical Routes** - Past trip visualization

### Feedback
If you have suggestions or find issues:
1. Note the vehicle number and time
2. Take a screenshot if possible
3. Contact support with details

## Keyboard Shortcuts

- **+/=** : Zoom in
- **-** : Zoom out
- **Arrow Keys** : Pan map
- **Esc** : Close popup
- **Tab** : Navigate controls

## Performance

### Expected Performance:
- **Load Time**: 1-2 seconds
- **Update Speed**: Instant (every 5s)
- **Zoom/Pan**: Smooth 60fps
- **Marker Click**: Immediate response

### System Requirements:
- **Browser**: Chrome 90+, Firefox 88+, Safari 14+
- **Internet**: Broadband connection recommended
- **Memory**: 4GB RAM minimum
- **Display**: 1280x720 minimum resolution

## Privacy & Data

- Map tiles from OpenStreetMap (public)
- Vehicle locations from your fleet database
- No external tracking or analytics
- All data stays within your system

---

**Enjoy tracking your vehicles on real street maps!** 🗺️🚗
