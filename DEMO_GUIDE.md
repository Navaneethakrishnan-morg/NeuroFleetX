# NeuroFleetX Demo Guide

## 🎯 How to Access the Demo Dashboards

The frontend is running and you can now explore all dashboards **without needing the backend**!

### Method 1: Using Demo Mode (Easiest)

1. **Open your browser** and go to: `http://localhost:3001`

2. **Click the "Try Demo Mode" button** (yellow button on the welcome page)

3. **Choose any dashboard** to explore:
   - 🧠 **Admin Dashboard** - Full system control view
   - 🧩 **Manager Dashboard** - Fleet operations view
   - 🚗 **Driver Dashboard** - Trip management view
   - 👤 **Customer Dashboard** - Vehicle booking view

### Method 2: Direct URLs

You can also access dashboards directly by navigating to:

- **Demo Selection**: `http://localhost:3001/demo`
- **Admin Dashboard**: `http://localhost:3001/admin/dashboard`
- **Manager Dashboard**: `http://localhost:3001/manager/dashboard`
- **Driver Dashboard**: `http://localhost:3001/driver/dashboard`
- **Customer Dashboard**: `http://localhost:3001/customer/dashboard`

## 📸 What You'll See

### Admin Dashboard
- ✅ KPI Cards (Total Fleet, Active Trips, Revenue, Maintenance Due)
- ✅ Fleet Status Overview with 5 sample vehicles
- ✅ Recent Bookings List
- ✅ Predictive Maintenance Alerts
- ✅ Beautiful gradient navigation bar
- ✅ Smooth animations and responsive design

### Manager Dashboard
- ✅ Fleet Statistics (Total, Available, In Use, Maintenance)
- ✅ Fleet Inventory Grid with vehicle details
- ✅ Maintenance Schedule
- ✅ Update Telemetry buttons (visual feedback)

### Driver Dashboard
- ✅ Trip Statistics (Today's Trips, Distance, Current Trip)
- ✅ Current Route Visualization
- ✅ Available Vehicles List

### Customer Dashboard
- ✅ Vehicle Filters (Type, Electric)
- ✅ Available Vehicles with "Recommended" badges
- ✅ Booking List
- ✅ Clean booking interface

## 🎨 Features to Explore

1. **Futuristic Design**
   - Gradient backgrounds (navy → teal)
   - Glassmorphism effects
   - Smooth hover animations
   - Clean card layouts

2. **Responsive Layout**
   - Works on all screen sizes
   - Grid-based vehicle/booking displays
   - Mobile-friendly navigation

3. **Status Indicators**
   - Color-coded status chips
   - Available (green), In Use (blue), Maintenance (yellow)
   - Priority levels for maintenance alerts

4. **Interactive Elements**
   - Hover effects on cards
   - Clickable buttons with smooth transitions
   - Role-specific color schemes

## ⚠️ Demo Mode Limitations

Since the backend is not connected:

❌ **Login won't authenticate** - Use Demo Mode button instead  
❌ **Signup won't save data** - Just for UI preview  
❌ **Real-time updates won't work** - Static demo data  
❌ **CRUD operations won't persist** - Button clicks show UI only  

✅ **All UI/UX features work perfectly**  
✅ **Navigation between pages works**  
✅ **Visual design is fully functional**  
✅ **Mock data displays correctly**  

## 🚀 To Get Full Functionality

Install these prerequisites:

1. **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/)
2. **Maven 3.6+** - [Download](https://maven.apache.org/download.cgi)
3. **MySQL 8.0+** - [Download](https://dev.mysql.com/downloads/mysql/)

Then follow the **SETUP_GUIDE.md** to run the complete system with backend!

## 📝 Tips

- **Explore all 4 dashboards** - Each has unique features
- **Check the responsiveness** - Resize your browser window
- **Hover over cards** - See the smooth animations
- **Notice the color schemes** - Each role has its own theme
- **View status chips** - Color-coded for quick identification

## 💡 Quick Navigation

From any dashboard, click the **"Logout"** button to return to the welcome page and try a different role!

---

Enjoy exploring NeuroFleetX! 🚗✨
