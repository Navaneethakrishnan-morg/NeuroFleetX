# Module 5: Customer Booking & Smart Recommendations

## Overview
The Customer Booking & Smart Recommendations module provides an intelligent, AI-powered booking system that recommends vehicles based on customer preferences and booking history. The system includes advanced filtering, real-time availability checking, and a comprehensive booking calendar.

## Features

### 1. AI-Powered Recommendations
- **Intelligent Matching**: Vehicles are scored and ranked based on customer preferences
- **Recommendation Score**: Each vehicle receives a match score (0-100) indicating suitability
- **Personalized Reasons**: Explanations for why vehicles are recommended
- **Learning System**: Recommendations improve based on booking history

### 2. Advanced Search & Filtering
- **Vehicle Type**: Filter by SEDAN, SUV, VAN, TRUCK, BUS, BIKE
- **Capacity**: Filter by minimum number of seats
- **Power Type**: Filter by Electric or Non-Electric vehicles
- **Sort Options**: Sort by AI recommendation, price, or capacity

### 3. Booking Calendar
- **Real-time Availability**: Check available time slots for any vehicle
- **Dynamic Pricing**: Prices calculated based on vehicle type and duration
- **Time Slot Selection**: 4-hour time slots with flexible booking
- **Visual Calendar**: Easy-to-use interface showing available and booked slots

### 4. Booking Management
- **Complete Booking Flow**: From search to confirmation
- **Location Details**: Pickup and dropoff location specification
- **Price Calculation**: Automatic calculation based on duration and vehicle type
- **Booking Summary**: Clear summary before confirmation

## Architecture

### Backend Components

#### 1. Database Schema
**Customer Preferences Table** (`customer_preferences`)
- Tracks preferred vehicle types, electric preference, capacity
- Stores average booking duration and frequency
- Auto-updates based on completed bookings

**Vehicle Ratings Table** (`vehicle_ratings`)
- Customer ratings for vehicles
- Used to calculate average vehicle ratings
- Influences recommendation scores

**Enhanced Bookings Table**
- Recommendation score and reason fields
- Tracks whether booking was from recommendation

#### 2. Services

**RecommendationEngine.java**
- `getRecommendedVehicles()`: Returns scored and sorted vehicle recommendations
- `calculateRecommendationScore()`: Scores vehicles based on multiple factors
- `updateCustomerPreferences()`: Updates preferences after completed bookings

Scoring Algorithm:
- Base score: 50 points
- Preferred vehicle type match: +20 points
- Electric preference match: +15 points
- Capacity match: +10 points
- Vehicle rating: up to +15 points
- Health score: up to +10 points
- Electric bonus: +5 points

**BookingService.java**
- `getAvailableVehicles()`: Returns vehicles available for time period
- `getBookingAvailability()`: Returns detailed availability with time slots
- `calculatePricePerHour()`: Dynamic pricing based on vehicle type

Pricing Multipliers:
- SEDAN: 1.0x ($25/hr base)
- SUV: 1.5x ($37.50/hr)
- VAN: 2.0x ($50/hr)
- TRUCK: 2.5x ($62.50/hr)
- BUS: 3.0x ($75/hr)
- BIKE: 0.5x ($12.50/hr)
- Electric vehicles: +10% premium

#### 3. API Endpoints

**Search & Recommendations**
```
POST /api/customer/vehicles/search?username={username}
Body: VehicleSearchRequest
Response: List<VehicleRecommendationResponse>
```

**Availability Check**
```
POST /api/customer/bookings/availability
Body: BookingAvailabilityRequest
Response: BookingAvailabilityResponse
```

**Get Available Vehicles**
```
GET /api/customer/vehicles/available?startTime={time}&endTime={time}
Response: List<Vehicle>
```

**Booking Actions**
```
POST /api/customer/bookings - Create booking
PUT /api/bookings/{id}/confirm - Confirm booking
PUT /api/bookings/{id}/cancel - Cancel booking
```

### Frontend Components

#### 1. CustomerBooking Page
**Location**: `frontend/src/pages/customer/CustomerBooking.js`

Features:
- Search filters for vehicle type, capacity, power type
- AI-recommended vehicles section with special badges
- All available vehicles section
- Real-time search and filtering
- Integration with booking calendar

#### 2. VehicleRecommendationCard Component
**Location**: `frontend/src/components/VehicleRecommendationCard.js`

Features:
- AI recommendation badge for top matches
- Match score display (color-coded by score)
- Vehicle details (capacity, type, electric status, health)
- Recommendation reasoning
- Price per hour display
- Book now action button

#### 3. BookingCalendar Component
**Location**: `frontend/src/components/BookingCalendar.js`

Features:
- Date range selector
- Availability summary (available/booked slots, pricing)
- Time slot grid with prices
- Booking form with pickup/dropoff locations
- Booking summary with total price
- Success/error feedback

## Setup Instructions

### 1. Database Migration
```bash
# Run the customer preferences migration
cd backend
# Execute SQL file against your database
mysql -u root -p neurofleetx < ../database-customer-preferences.sql
```

### 2. Backend Setup
The backend components are automatically included with the main application.
No additional configuration needed.

### 3. Frontend Setup
```bash
cd frontend
npm install
npm start
```

### 4. Access the Feature
1. Start the backend server
2. Start the frontend server
3. Login as a customer
4. Click on "⚡ Smart Booking" in the customer dashboard

## Usage Guide

### For Customers

#### Step 1: Search for Vehicles
1. Navigate to the "Smart Booking" tab
2. Use filters to refine your search:
   - Select vehicle type (Sedan, SUV, etc.)
   - Choose minimum seats required
   - Filter by power type (Electric/Non-Electric)
3. Click "Search Vehicles"

#### Step 2: Review Recommendations
- **AI Recommended Section**: Vehicles with 70%+ match score
  - Green badge indicates AI recommendation
  - Match score shows how well the vehicle fits your preferences
  - Reasoning explains why it's recommended
- **Other Vehicles**: All other available vehicles

#### Step 3: Book a Vehicle
1. Click "Book Now" on your chosen vehicle
2. Select date range for availability check
3. Choose an available time slot
4. Enter pickup location
5. Enter dropoff location
6. Review booking summary
7. Click "Confirm Booking"

#### Step 4: Confirmation
- Success message appears
- Booking automatically added to "My Bookings"
- Vehicle marked as booked for selected time

### How AI Recommendations Work

The system learns from your behavior:

1. **First Booking**: Recommendations based on availability and vehicle health
2. **Subsequent Bookings**: System analyzes:
   - Most frequently booked vehicle types
   - Preference for electric vs non-electric
   - Average booking duration
   - Preferred capacity
3. **Continuous Learning**: Each completed booking updates your profile
4. **Personalized Results**: Higher match scores for vehicles similar to your history

## Testing the System

### Test Scenario 1: First-Time Customer
```
Username: customer1
Expected: All vehicles shown with base recommendations
Filters work correctly
Booking flow completes successfully
```

### Test Scenario 2: Repeat Customer
```
Username: customer1 (after multiple bookings)
Expected: 
- AI recommendations based on history
- Higher scores for previously booked vehicle types
- Personalized reasons displayed
```

### Test Scenario 3: Availability Check
```
1. Book a vehicle for specific time slot
2. Search for same vehicle
3. Expected: Booked time slot not available
4. Expected: Other time slots still available
```

### Test Scenario 4: Pricing
```
Different vehicle types show different pricing:
- Sedan: $25/hr
- SUV: $37.50/hr
- Electric vehicles: +10% premium
- Longer bookings: Proportional pricing
```

## API Examples

### Search Vehicles
```javascript
POST http://localhost:8080/api/customer/vehicles/search?username=customer1
Content-Type: application/json

{
  "vehicleType": "SUV",
  "isElectric": null,
  "minCapacity": 5,
  "maxCapacity": null,
  "startTime": null,
  "endTime": null,
  "sortBy": "recommendation"
}
```

### Check Availability
```javascript
POST http://localhost:8080/api/customer/bookings/availability
Content-Type: application/json

{
  "vehicleId": 1,
  "startDate": "2025-11-05",
  "endDate": "2025-11-12"
}
```

### Create Booking
```javascript
POST http://localhost:8080/api/customer/bookings
Content-Type: application/json

{
  "customer": { "username": "customer1" },
  "vehicle": { "id": 1 },
  "startTime": "2025-11-05T10:00:00",
  "endTime": "2025-11-05T14:00:00",
  "pickupLocation": "123 Main St, New York",
  "dropoffLocation": "456 Park Ave, New York",
  "totalPrice": 100.00
}
```

## Troubleshooting

### Issue: Recommendations not personalized
**Solution**: Complete at least one booking to generate preference data

### Issue: No available time slots
**Solution**: 
- Check if vehicle is in AVAILABLE status
- Try different date range
- Verify vehicle not fully booked

### Issue: Booking fails
**Solution**:
- Verify all required fields filled
- Check vehicle availability for selected time
- Ensure customer is logged in

### Issue: Prices seem incorrect
**Solution**:
- Verify vehicle type multipliers in BookingService
- Check for electric vehicle premium
- Confirm duration calculation

## Future Enhancements

### Planned Features
1. **Multi-day Bookings**: Support for bookings spanning multiple days
2. **Recurring Bookings**: Weekly/monthly booking patterns
3. **Favorite Vehicles**: Save preferred vehicles for quick booking
4. **Price Alerts**: Notifications when preferred vehicles drop in price
5. **Advanced AI**: Machine learning model for more accurate predictions
6. **Group Bookings**: Book multiple vehicles for events
7. **Loyalty Program**: Discounts based on booking frequency
8. **Real-time Notifications**: Push notifications for booking updates

### Potential Improvements
- Integration with payment gateway
- GPS integration for real-time vehicle location
- Vehicle comparison feature
- Customer reviews and ratings UI
- Booking modification support
- Cancellation policy enforcement
- Insurance options
- Mobile app version

## Support

For issues or questions:
1. Check this documentation
2. Review the API endpoint documentation
3. Check browser console for errors
4. Verify database connections
5. Ensure all services are running

## Summary

Module 5 provides a complete, production-ready booking system with:
- ✅ AI-powered vehicle recommendations
- ✅ Advanced search and filtering
- ✅ Real-time availability checking
- ✅ Dynamic pricing
- ✅ Complete booking flow
- ✅ Beautiful, responsive UI
- ✅ Learning system that improves over time

The system is designed to scale and can handle multiple customers, complex booking scenarios, and provides an excellent user experience with intelligent recommendations.
