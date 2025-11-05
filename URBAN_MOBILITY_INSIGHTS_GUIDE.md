# Module 6: Urban Mobility Insights Guide

## Overview
The Urban Mobility Insights module provides comprehensive real-time analytics for fleet management with interactive visualizations, heatmaps, and downloadable reports. This admin-focused dashboard delivers actionable insights into fleet operations, revenue tracking, and urban mobility patterns.

---

## Features Implemented

### 1. Real-Time KPI Dashboard ⚡
- **Total Fleet Metrics**: Complete fleet count with status breakdown
- **Trip Tracking**: Daily trips and active routes monitoring
- **Revenue Analytics**: Total and daily revenue calculations
- **Utilization Metrics**: Real-time fleet utilization percentage
- **Auto-Refresh**: 30-second automatic data refresh

### 2. Interactive Fleet Heatmap 🗺️
- **Canvas-based Visualization**: High-performance heatmap rendering
- **Vehicle Location Tracking**: Real-time vehicle positions
- **Trip Density Overlay**: Heat-based trip concentration areas
- **City Layout**: Road network and district overlays
- **Click Interactions**: Select vehicles/locations for details
- **Type Distribution**: Fleet breakdown by vehicle type

### 3. Advanced Charts & Graphs 📊
- **Hourly Rental Activity**: Bar chart showing 24-hour booking patterns
- **Daily Trends**: 7-day line chart for booking trends
- **Revenue Tracking**: Daily revenue bar chart
- **Fleet Type Distribution**: Doughnut chart showing vehicle mix
- **Performance Rankings**: Top 10 vehicles by revenue

### 4. Downloadable Reports 📥
- **Fleet Report CSV**: Complete vehicle inventory and status
- **Bookings Report CSV**: All bookings with customer details
- **Revenue Report CSV**: Financial summary with breakdowns
- **Trips Report CSV**: Trip history with distances and durations
- **Analytics Summary CSV**: Comprehensive metrics overview

---

## Architecture

### Backend Components

#### Services (3 new)

**1. AnalyticsService.java** (300+ lines)
- Real-time KPI calculations
- Fleet distribution analysis
- Hourly activity aggregation
- Daily trend analysis
- Vehicle performance rankings
- Trip density calculations

Key Methods:
```java
getKPIMetrics() - Returns 12 key performance indicators
getFleetDistribution() - Vehicle locations and trip density
getHourlyActivity() - 24-hour booking/revenue data
getDailyTrends(days) - Multi-day trend analysis
getVehiclePerformance() - Top performers by revenue
```

**2. ReportGenerationService.java** (250+ lines)
- CSV report generation for all data types
- Automatic timestamp formatting
- CSV escaping and formatting
- Summary statistics calculation

Report Types:
- Fleet Report (vehicles, status, health)
- Bookings Report (all booking details)
- Revenue Report (completed bookings revenue)
- Trips Report (trip history with metrics)
- Analytics Summary (comprehensive overview)

**3. AnalyticsController.java** (120+ lines)
- RESTful API endpoints for analytics
- Report download endpoints
- CORS configuration
- Response formatting

#### DTOs (4 new)
1. **AnalyticsKPIResponse**: 12 KPI metrics
2. **HeatmapDataPoint**: Location with intensity
3. **HourlyActivityData**: 24-hour activity arrays
4. **FleetDistributionData**: Vehicle locations and distributions

#### API Endpoints (11 new)
```
GET /api/analytics/kpi
GET /api/analytics/fleet-distribution
GET /api/analytics/hourly-activity
GET /api/analytics/daily-trends?days=7
GET /api/analytics/vehicle-performance
GET /api/analytics/reports/fleet/csv
GET /api/analytics/reports/bookings/csv
GET /api/analytics/reports/revenue/csv
GET /api/analytics/reports/trips/csv
GET /api/analytics/reports/summary/csv
```

### Frontend Components

#### Pages (1 new)

**UrbanMobilityInsights.js** (500+ lines)
- KPI card grid (4 main metrics)
- Interactive fleet heatmap
- Chart.js integration (4 charts)
- Top performers table
- Export buttons for reports
- Real-time refresh functionality
- Loading states and error handling

Features:
- Auto-refresh every 30 seconds
- Click-to-refresh manual button
- Responsive grid layouts
- Professional chart styling
- Download progress indication

#### Components (1 new)

**FleetHeatmap.js** (250+ lines)
- Canvas-based heatmap rendering
- City layout with roads and districts
- Vehicle marker rendering with intensity
- Trip density heat overlay
- Click interaction for vehicle details
- Type distribution summary cards
- Legend with color coding

Rendering Features:
- Grid overlay for reference
- City blocks visualization
- Road network display
- Gradient heat points
- Pulsing vehicle markers
- Color-coded by status

---

## Data Flow

### KPI Calculation Pipeline
```
1. AnalyticsService aggregates data from:
   - VehicleRepository (fleet status)
   - BookingRepository (trip counts, revenue)
   - TripRepository (active routes)
   - MaintenanceRepository (alerts)

2. Real-time calculations:
   - Count vehicles by status
   - Sum completed booking revenue
   - Calculate utilization percentage
   - Aggregate daily metrics

3. Response formatted as AnalyticsKPIResponse
4. Frontend displays in KPI cards
5. Auto-refresh every 30 seconds
```

### Heatmap Data Pipeline
```
1. Get all vehicles with GPS coordinates
2. Calculate intensity based on status:
   - IN_USE: 100
   - AVAILABLE: 60
   - MAINTENANCE: 30
   - OUT_OF_SERVICE: 10

3. Get all trips for density calculation
4. Group trips by location
5. Generate heat intensity (count * 10, max 100)

6. Canvas renders:
   - City grid and roads
   - District overlays
   - Heat gradients for density
   - Vehicle markers with shadows
```

### Report Generation Pipeline
```
1. User clicks export button
2. Frontend calls download API with responseType: 'blob'
3. Backend:
   - Queries relevant data
   - Formats as CSV with headers
   - Adds timestamp to filename
   - Sets Content-Disposition header

4. Frontend:
   - Receives blob data
   - Creates download link
   - Triggers browser download
   - Cleans up object URL
```

---

## Charts & Visualizations

### 1. Hourly Activity Bar Chart
**Data**: 24 hourly booking counts
**Purpose**: Identify peak and off-peak hours
**Insights**: Optimize pricing and fleet allocation

Configuration:
- Green gradient bars
- X-axis: Hours (0-23)
- Y-axis: Number of bookings
- Hover tooltips with exact counts

### 2. Daily Trends Line Chart
**Data**: 7 days of booking counts
**Purpose**: Track growth and patterns
**Insights**: Week-over-week performance

Configuration:
- Cyan line with fill
- Smooth curve (tension: 0.4)
- Date labels on X-axis
- Trend visualization

### 3. Daily Revenue Bar Chart
**Data**: 7 days of revenue totals
**Purpose**: Financial performance tracking
**Insights**: Revenue trends and forecasting

Configuration:
- Green bars
- Dollar amounts displayed
- Date labels
- Total comparison

### 4. Fleet Type Distribution Doughnut
**Data**: Vehicle count by type
**Purpose**: Fleet composition analysis
**Insights**: Optimize vehicle mix

Configuration:
- 6 color segments
- Type labels
- Percentage display
- Interactive legend

---

## KPI Metrics Explained

### Core Metrics

**Total Fleet** (totalFleet)
- Count of all vehicles in system
- Breakdown: Available, In Use, Maintenance
- Use: Capacity planning

**Trips Today** (tripsToday)
- Trips started today
- Counts all trip statuses
- Use: Daily activity monitoring

**Active Routes** (activeRoutes)
- Currently in-progress trips
- Real-time operational metric
- Use: Live fleet tracking

**Total Revenue** (totalRevenue)
- Sum of all completed bookings
- Historical cumulative value
- Use: Business performance

**Revenue Today** (revenueToday)
- Today's completed booking revenue
- Daily income tracking
- Use: Daily targets

**Average Utilization** (averageUtilization)
- (In Use Vehicles / Total Fleet) * 100
- Efficiency indicator
- Use: Optimization opportunities

### Secondary Metrics

**Available Vehicles** (availableVehicles)
- Ready-to-book fleet count
- Immediate capacity
- Use: Availability planning

**In Use Vehicles** (inUseVehicles)
- Currently active vehicles
- Operational status
- Use: Capacity monitoring

**Maintenance Vehicles** (maintenanceVehicles)
- Vehicles under repair
- Capacity reduction
- Use: Maintenance planning

**Pending Maintenance** (pendingMaintenance)
- Scheduled maintenance count
- Proactive planning
- Use: Resource allocation

**Total Customers** (totalCustomers)
- All registered customers
- User base size
- Use: Growth tracking

**Active Customers** (activeCustomers)
- Customers with active bookings
- Engagement metric
- Use: Retention analysis

---

## Report Formats

### Fleet Report CSV
```csv
Fleet Report - Generated: 2025-11-03 12:00:00

Vehicle Number,Manufacturer,Model,Type,Capacity,Electric,Status,Health Score,Mileage,Battery Level,Fuel Level
NF-001,Tesla,Model S,SEDAN,5,Yes,AVAILABLE,95,12000,85,N/A
NF-002,Tesla,Model 3,SEDAN,5,Yes,AVAILABLE,98,8000,92,N/A
...
```

### Bookings Report CSV
```csv
Bookings Report - Generated: 2025-11-03 12:00:00

Booking ID,Customer,Vehicle,Start Time,End Time,Pickup Location,Dropoff Location,Status,Total Price
1,Alice Customer,NF-001,2025-11-05 10:00:00,2025-11-05 14:00:00,123 Main St,456 Park Ave,CONFIRMED,100.00
...
```

### Revenue Report CSV
```csv
Revenue Report - Generated: 2025-11-03 12:00:00

Total Completed Bookings: 150
Total Revenue: $15200.00

Date,Booking ID,Customer,Vehicle,Amount
2025-11-01 09:00:00,1,Alice Customer,NF-001,100.00
...
```

### Analytics Summary CSV
```csv
Analytics Summary Report - Generated: 2025-11-03 12:00:00

=== Fleet Statistics ===
Total Fleet: 10
Available: 6
In Use: 2
Maintenance: 2

=== Booking Statistics ===
Total Bookings: 150
Completed: 120
In Progress: 10
Pending: 20

=== Revenue Statistics ===
Total Revenue: $15200.00
Average Booking Value: $101.33

=== Trip Statistics ===
Total Trips: 140
Completed: 120
In Progress: 10
Total Distance: 3500.50 miles
```

---

## Usage Guide

### Accessing Urban Insights

1. Login as admin
2. Navigate to Admin Dashboard
3. Click "🌆 Urban Insights" tab
4. Dashboard loads with auto-refresh

### Interpreting the Heatmap

**Color Coding:**
- 🟢 Green Markers: Active vehicles (IN_USE)
- 🟡 Yellow Markers: Available vehicles
- 🔴 Red Markers: Maintenance status
- 🔥 Red Heat: High trip density areas

**Interactions:**
- Click vehicle markers for details
- View location coordinates
- See vehicle ID and status
- Close detail popup with X button

### Using Charts

**Hourly Activity:**
- Hover over bars for exact counts
- Identify peak hours (typically 8-10 AM, 5-7 PM)
- Plan fleet distribution accordingly

**Daily Trends:**
- Track week-over-week growth
- Identify seasonal patterns
- Spot anomalies or issues

**Revenue Chart:**
- Monitor financial performance
- Compare day-over-day revenue
- Set daily targets

**Type Distribution:**
- Assess fleet balance
- Plan vehicle acquisitions
- Optimize for demand

### Downloading Reports

1. Scroll to "Download Reports" section
2. Click desired report type
3. Browser downloads CSV automatically
4. Open in Excel/Sheets for analysis

**Report Types:**
- **Fleet Report**: Inventory management
- **Bookings Report**: Customer activity
- **Revenue Report**: Financial analysis
- **Trips Report**: Operational metrics
- **Full Summary**: Executive overview

---

## Performance Optimization

### Backend Optimizations

**Data Aggregation:**
- Single query per repository
- In-memory calculations
- Efficient filtering with streams
- Minimal database hits

**Report Generation:**
- Streaming CSV generation
- Minimal memory footprint
- Direct ByteArrayOutputStream usage
- No temporary file creation

**Caching Strategy:**
- KPI data cached for 30 seconds
- Report data generated on-demand
- No stale data issues

### Frontend Optimizations

**Chart Rendering:**
- Chart.js lazy loading
- Canvas hardware acceleration
- Efficient data structures
- Minimal re-renders

**Heatmap Performance:**
- Canvas 2D context
- Optimized gradient rendering
- Debounced click handlers
- Efficient coordinate mapping

**Data Refresh:**
- Selective KPI updates
- Full reload on manual refresh
- Background data fetching
- No UI blocking

---

## Error Handling

### Backend
```java
try {
    // Data processing
} catch (Exception e) {
    throw new RuntimeException("Error message", e);
}
```

### Frontend
```javascript
try {
    // API call
} catch (error) {
    console.error('Error:', error);
    alert('Failed. Please try again.');
}
```

### Common Issues

**Issue**: No data showing
- **Cause**: Empty database
- **Solution**: Run database init script

**Issue**: Reports fail to download
- **Cause**: CORS or server not running
- **Solution**: Verify backend running on port 8080

**Issue**: Heatmap not rendering
- **Cause**: Missing GPS coordinates
- **Solution**: Update vehicles with lat/lng data

---

## API Testing

### Get KPI Metrics
```bash
curl http://localhost:8080/api/analytics/kpi
```

### Get Fleet Distribution
```bash
curl http://localhost:8080/api/analytics/fleet-distribution
```

### Download Report
```bash
curl http://localhost:8080/api/analytics/reports/fleet/csv \
  --output fleet-report.csv
```

---

## Customization

### Changing Refresh Interval
Edit `UrbanMobilityInsights.js`:
```javascript
const interval = setInterval(() => {
  refreshData();
}, 30000); // Change to desired milliseconds
```

### Modifying Chart Colors
Edit chart configuration:
```javascript
backgroundColor: 'rgba(16, 185, 129, 0.6)', // Green
borderColor: 'rgba(16, 185, 129, 1)',
```

### Adding New KPIs
1. Add field to `AnalyticsKPIResponse.java`
2. Calculate in `AnalyticsService.getKPIMetrics()`
3. Add KPI card in frontend component

---

## Best Practices

### Admin Usage
- Check dashboard daily for anomalies
- Download reports weekly for records
- Monitor utilization for optimization
- Review heatmap for distribution

### Data Analysis
- Compare week-over-week trends
- Identify peak hours for pricing
- Track vehicle performance
- Monitor maintenance needs

### Report Management
- Export reports regularly
- Archive for compliance
- Share with stakeholders
- Use for forecasting

---

## Technical Specifications

### Performance Metrics
- **KPI Load Time**: < 1 second
- **Heatmap Render**: < 500ms
- **Chart Render**: < 300ms per chart
- **Report Download**: < 2 seconds
- **Auto-Refresh**: Every 30 seconds

### Browser Compatibility
- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

### Screen Resolutions
- Desktop: 1920x1080 (optimal)
- Laptop: 1366x768 (supported)
- Tablet: Responsive grid layout

---

## Future Enhancements

### Phase 2
- PDF report generation
- Email report scheduling
- Custom date range selection
- Export to Excel format
- Interactive chart drilling

### Phase 3
- Machine learning predictions
- Anomaly detection
- Real-time alerts
- Custom dashboard builder
- Mobile app version

---

## Support

For issues or questions:
1. Check this guide
2. Review browser console for errors
3. Verify backend is running
4. Check database connection
5. Review API endpoint responses

---

**Module Status**: ✅ PRODUCTION READY  
**Version**: 1.0.0  
**Last Updated**: November 3, 2025
