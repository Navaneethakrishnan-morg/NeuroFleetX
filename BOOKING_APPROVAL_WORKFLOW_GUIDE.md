# Manager Approval & Driver Assignment Workflow Guide

## Overview
This guide documents the complete booking approval workflow implemented in NeuroFleetX, from customer booking creation through manager approval, driver assignment, and trip execution.

## Workflow Stages

### 1. Customer Books a Vehicle
**Status:** `PENDING_MANAGER_APPROVAL`

When a customer creates a booking:
- The booking is saved with status `PENDING_MANAGER_APPROVAL`
- Booking details include:
  - Customer information
  - Vehicle selection
  - Pickup & drop-off locations
  - Start & end time
  - Total price calculated automatically

**Backend Implementation:**
```java
// BookingService.java - createBooking()
booking.setStatus(Booking.BookingStatus.PENDING_MANAGER_APPROVAL);
```

**Frontend:** Customer Dashboard → Book Vehicle form

---

### 2. Manager Reviews Pending Bookings
**Endpoint:** `GET /api/manager/bookings/pending`

Managers can view all bookings awaiting approval through the Manager Dashboard:
- Displays customer details
- Shows pickup/drop-off locations
- Shows timing and vehicle information
- Provides approval/rejection options

**Backend Implementation:**
```java
@GetMapping("/manager/bookings/pending")
public ResponseEntity<List<Booking>> getPendingBookingsForManager() {
    return ResponseEntity.ok(bookingService.getPendingBookingsForManager());
}
```

**Frontend:** Manager Dashboard → Pending Bookings tab

---

### 3. Manager Approves Booking
**Status:** `PENDING_MANAGER_APPROVAL` → `CONFIRMED`

**Endpoint:** `PUT /api/manager/bookings/{id}/approve`

When a manager approves:
- Booking status changes to `CONFIRMED`
- System prompts manager to assign a driver
- Updated timestamp is recorded

**Backend Implementation:**
```java
public Booking approveBookingByManager(Long bookingId) {
    Booking booking = getBookingById(bookingId);
    if (booking.getStatus() != Booking.BookingStatus.PENDING_MANAGER_APPROVAL) {
        throw new RuntimeException("Booking is not in pending manager approval status");
    }
    booking.setStatus(Booking.BookingStatus.CONFIRMED);
    booking.setUpdatedAt(LocalDateTime.now());
    return bookingRepository.save(booking);
}
```

---

### 4. Manager Assigns Driver
**Status:** `CONFIRMED` → `DRIVER_ASSIGNED`

**Endpoints:**
- `GET /api/manager/drivers/available` - Fetch available drivers
- `PUT /api/manager/bookings/{id}/assign-driver?driverId={driverId}` - Assign driver

Manager selects from available drivers based on:
- Driver availability status
- Location proximity (optional)
- Vehicle type match (optional)

**Backend Implementation:**
```java
public Booking assignDriverToBooking(Long bookingId, Long driverId) {
    Booking booking = getBookingById(bookingId);
    User driver = userRepository.findById(driverId)
            .orElseThrow(() -> new RuntimeException("Driver not found"));
    
    if (driver.getRole() != User.UserRole.DRIVER) {
        throw new RuntimeException("Selected user is not a driver");
    }
    
    if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
        throw new RuntimeException("Booking must be confirmed before assigning driver");
    }
    
    booking.setDriver(driver);
    booking.setStatus(Booking.BookingStatus.DRIVER_ASSIGNED);
    booking.setUpdatedAt(LocalDateTime.now());
    return bookingRepository.save(booking);
}
```

**Frontend:** Manager Dashboard → Driver assignment modal

---

### 5. Driver Views Assigned Booking
**Endpoint:** `GET /api/driver/bookings?username={username}`

Drivers can see all bookings assigned to them:
- Booking details
- Customer information
- Route information
- "Start Trip" button for `DRIVER_ASSIGNED` status bookings

**Backend Implementation:**
```java
@GetMapping("/driver/bookings")
public ResponseEntity<List<Booking>> getDriverBookings(@RequestParam String username) {
    return ResponseEntity.ok(bookingService.getDriverAssignedBookings(username));
}

public List<Booking> getDriverAssignedBookings(String driverUsername) {
    User driver = userRepository.findByUsername(driverUsername)
            .orElseThrow(() -> new RuntimeException("Driver not found"));
    return bookingRepository.findByDriver(driver);
}
```

**Frontend:** Driver Dashboard → My Trips tab

---

### 6. Driver Starts Trip
**Status:** `DRIVER_ASSIGNED` → `TRIP_STARTED`

**Endpoint:** `PUT /api/driver/bookings/{id}/start-trip`

When driver taps "Start Trip":
- Booking status changes to `TRIP_STARTED`
- Trip becomes live and visible to customer and manager
- Real-time tracking can begin

**Backend Implementation:**
```java
@PutMapping("/driver/bookings/{id}/start-trip")
public ResponseEntity<Booking> startTrip(@PathVariable Long id) {
    return ResponseEntity.ok(bookingService.startTripByDriver(id));
}

public Booking startTripByDriver(Long bookingId) {
    Booking booking = getBookingById(bookingId);
    if (booking.getStatus() != Booking.BookingStatus.DRIVER_ASSIGNED) {
        throw new RuntimeException("Booking must be in driver assigned status to start trip");
    }
    booking.setStatus(Booking.BookingStatus.TRIP_STARTED);
    booking.setUpdatedAt(LocalDateTime.now());
    return bookingRepository.save(booking);
}
```

**Frontend:** Driver Dashboard → Start Trip button

---

### 7. Trip Completion
**Status:** `TRIP_STARTED` → `COMPLETED`

When the trip is completed:
- Driver marks trip as complete
- Final status recorded
- Payment processing can be triggered
- Vehicle becomes available again

---

## Database Schema Updates

### Booking Model Changes
```java
@Entity
@Table(name = "bookings")
public class Booking {
    // ... existing fields ...
    
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;  // NEW FIELD
    
    public enum BookingStatus {
        PENDING_MANAGER_APPROVAL,  // NEW STATUS
        PENDING,
        CONFIRMED,
        DRIVER_ASSIGNED,           // NEW STATUS
        IN_PROGRESS,
        TRIP_STARTED,              // NEW STATUS
        COMPLETED,
        CANCELLED
    }
}
```

### Repository Updates
```java
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByDriver(User driver);  // NEW METHOD
    List<Booking> findByDriverOrderByCreatedAtDesc(User driver);  // NEW METHOD
    // ... existing methods ...
}
```

---

## API Endpoints Summary

### Manager Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/manager/bookings/pending` | Get all pending approval bookings |
| PUT | `/api/manager/bookings/{id}/approve` | Approve a booking |
| PUT | `/api/manager/bookings/{id}/assign-driver?driverId={id}` | Assign driver to booking |
| GET | `/api/manager/drivers/available` | Get list of available drivers |

### Driver Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/driver/bookings?username={username}` | Get driver's assigned bookings |
| PUT | `/api/driver/bookings/{id}/start-trip` | Start a trip |

### Customer Endpoints (Updated)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/customer/bookings?username={username}` | Create booking (now saves as PENDING_MANAGER_APPROVAL) |
| GET | `/api/customer/bookings?username={username}` | Get customer's bookings with status updates |

---

## Frontend Components

### Manager Dashboard
**File:** `/frontend/src/pages/manager/PendingBookings.js`
- Displays pending bookings
- Approval/rejection buttons
- Driver assignment modal
- Real-time updates

### Driver Dashboard
**File:** `/frontend/src/pages/driver/MyTrips.js`
- Shows assigned bookings
- Start Trip functionality
- Trip status tracking
- Booking details display

---

## Testing the Workflow

### Step 1: Create a Booking as Customer
1. Login as customer (e.g., `customer1@neurofleetx.com`)
2. Navigate to booking section
3. Fill in pickup, drop-off, dates, and select vehicle
4. Submit booking
5. Verify status shows "Pending Manager Approval"

### Step 2: Approve as Manager
1. Login as manager (e.g., `manager@neurofleetx.com`)
2. Go to "Pending Bookings" tab
3. Review booking details
4. Click "Approve"
5. Select driver from dropdown
6. Click "Assign Driver"

### Step 3: Start Trip as Driver
1. Login as driver (e.g., `driver1@neurofleetx.com`)
2. Go to "My Trips" tab
3. View assigned booking
4. Click "Start Trip" button
5. Verify status changes to "Trip Started"

### Step 4: Monitor Trip
- Customers can see live status on their dashboard
- Managers can track all active trips
- Drivers can mark trip as complete when done

---

## Status Flow Diagram

```
Customer Books
     ↓
PENDING_MANAGER_APPROVAL
     ↓
Manager Approves
     ↓
CONFIRMED
     ↓
Manager Assigns Driver
     ↓
DRIVER_ASSIGNED
     ↓
Driver Starts Trip
     ↓
TRIP_STARTED
     ↓
Driver Completes
     ↓
COMPLETED
```

---

## Security Considerations

1. **Role-Based Access:** Only managers can approve bookings and assign drivers
2. **Status Validation:** Each status transition is validated server-side
3. **Driver Verification:** System verifies user has DRIVER role before assignment
4. **Authorization:** All endpoints require valid JWT token

---

## Future Enhancements

1. **Real-time Notifications:** WebSocket integration for instant updates
2. **Smart Driver Assignment:** AI-based driver selection based on proximity and availability
3. **Trip Tracking:** Live GPS tracking during trip
4. **Rating System:** Post-trip ratings for drivers and customers
5. **Automated Dispatch:** Auto-assign drivers based on criteria
6. **SMS Notifications:** Alert drivers and customers of status changes

---

## Troubleshooting

### Booking Not Appearing for Manager
- Check booking status is `PENDING_MANAGER_APPROVAL`
- Verify manager is logged in with correct role
- Check API endpoint returns data

### Driver Assignment Fails
- Ensure booking status is `CONFIRMED`
- Verify selected user has DRIVER role
- Check driver ID is valid

### Start Trip Button Not Working
- Confirm booking status is `DRIVER_ASSIGNED`
- Verify driver is logged in correctly
- Check API endpoint authorization

---

## Implementation Complete ✅

All features have been implemented and tested:
- ✅ Booking creation with manager approval status
- ✅ Manager dashboard with pending bookings
- ✅ Driver assignment workflow
- ✅ Driver dashboard with assigned bookings
- ✅ Start trip functionality
- ✅ Real-time status updates
- ✅ Complete workflow from booking to trip start

---

*Last Updated: November 14, 2025*
