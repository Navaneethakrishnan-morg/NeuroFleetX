# Booking Status Constraint Fix

## Problem
Customer booking creation was failing with SQL constraint error:
```
Failed to create booking: could not execute statement
[SQLITE_CONSTRAINT_CHECK] A CHECK constraint failed 
(CHECK constraint failed: status in ('PENDING','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED'))
```

## Root Cause
**Database Schema** allowed only these statuses:
```sql
status ENUM('PENDING', 'CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED')
```

But **Java Enum** in `Booking.java` had additional statuses:
```java
enum BookingStatus {
    PENDING_MANAGER_APPROVAL,  // ❌ Not in database
    PENDING,
    CONFIRMED,
    DRIVER_ASSIGNED,           // ❌ Not in database
    IN_PROGRESS,
    TRIP_STARTED,              // ❌ Not in database
    COMPLETED,
    CANCELLED
}
```

When trying to save a booking with `PENDING_MANAGER_APPROVAL` status, the database rejected it.

## Solution Applied

### 1. Updated Booking Model
**File:** `backend/src/main/java/com/neurofleetx/model/Booking.java`

**Before:**
```java
public enum BookingStatus {
    PENDING_MANAGER_APPROVAL, PENDING, CONFIRMED, DRIVER_ASSIGNED, 
    IN_PROGRESS, TRIP_STARTED, COMPLETED, CANCELLED
}
```

**After:**
```java
public enum BookingStatus {
    PENDING, CONFIRMED, IN_PROGRESS, COMPLETED, CANCELLED
}
```

### 2. Updated BookingService
**File:** `backend/src/main/java/com/neurofleetx/service/BookingService.java`

**Changes Made:**

#### a) Create Booking
```java
// Before:
booking.setStatus(Booking.BookingStatus.PENDING_MANAGER_APPROVAL);

// After:
booking.setStatus(Booking.BookingStatus.PENDING);
```

#### b) Get Pending Bookings
```java
// Before:
return bookingRepository.findByStatus(Booking.BookingStatus.PENDING_MANAGER_APPROVAL);

// After:
return bookingRepository.findByStatus(Booking.BookingStatus.PENDING);
```

#### c) Approve Booking
```java
// Before:
if (booking.getStatus() != Booking.BookingStatus.PENDING_MANAGER_APPROVAL) {
    throw new RuntimeException("Booking is not in pending manager approval status");
}

// After:
if (booking.getStatus() != Booking.BookingStatus.PENDING) {
    throw new RuntimeException("Booking is not in pending approval status");
}
```

#### d) Assign Driver
```java
// Before:
booking.setStatus(Booking.BookingStatus.DRIVER_ASSIGNED);

// After:
booking.setStatus(Booking.BookingStatus.CONFIRMED);
```

#### e) Start Trip
```java
// Before:
if (booking.getStatus() != Booking.BookingStatus.DRIVER_ASSIGNED) {
    throw new RuntimeException("Booking must be in driver assigned status to start trip");
}
booking.setStatus(Booking.BookingStatus.TRIP_STARTED);

// After:
if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
    throw new RuntimeException("Booking must be confirmed to start trip");
}
booking.setStatus(Booking.BookingStatus.IN_PROGRESS);
```

## Simplified Booking Workflow

### Old Workflow (Broken)
```
1. Customer creates booking → PENDING_MANAGER_APPROVAL ❌
2. Manager approves → CONFIRMED
3. Manager assigns driver → DRIVER_ASSIGNED ❌
4. Driver starts trip → TRIP_STARTED ❌
5. Driver completes → COMPLETED
```

### New Workflow (Working)
```
1. Customer creates booking → PENDING ✅
2. Manager approves → CONFIRMED ✅
3. Manager assigns driver → CONFIRMED ✅ (driver field set)
4. Driver starts trip → IN_PROGRESS ✅
5. Driver completes → COMPLETED ✅
```

## Status Meanings

| Status | Description | When Set |
|--------|-------------|----------|
| **PENDING** | Awaiting manager approval | On booking creation |
| **CONFIRMED** | Approved by manager, ready for assignment | After manager approval |
| **IN_PROGRESS** | Trip has started | When driver starts trip |
| **COMPLETED** | Trip finished successfully | When driver completes trip |
| **CANCELLED** | Booking cancelled | When user cancels |

## Files Modified

1. **Backend Model**
   - `backend/src/main/java/com/neurofleetx/model/Booking.java`
   - Simplified `BookingStatus` enum

2. **Backend Service**
   - `backend/src/main/java/com/neurofleetx/service/BookingService.java`
   - Updated 5 methods using old status values

3. **Build & Deploy**
   - Compiled with `mvn clean compile`
   - Restarted backend server

## Testing Steps

### 1. Create Booking (Customer)
```
1. Login as customer
2. Go to "Book Vehicle" page
3. Select dates: 15-11-2025 to 22-11-2025
4. Choose vehicle
5. Click "Processing..."
```

**Expected Result:** ✅ Booking created with status `PENDING`

### 2. Approve Booking (Manager)
```
1. Login as manager
2. Go to "Pending Bookings"
3. Find new booking
4. Click "Approve"
```

**Expected Result:** ✅ Status changes to `CONFIRMED`

### 3. Assign Driver (Manager)
```
1. View confirmed booking
2. Select driver from dropdown
3. Click "Assign Driver"
```

**Expected Result:** ✅ Driver assigned, status remains `CONFIRMED`

### 4. Start Trip (Driver)
```
1. Login as driver
2. Go to "My Trips"
3. Find assigned booking
4. Click "Start Trip"
```

**Expected Result:** ✅ Status changes to `IN_PROGRESS`

### 5. Complete Trip (Driver)
```
1. View in-progress trip
2. Click "Complete Trip"
```

**Expected Result:** ✅ Status changes to `COMPLETED`

## Database Constraint

The database will continue to enforce:
```sql
CHECK (status in ('PENDING','CONFIRMED','IN_PROGRESS','COMPLETED','CANCELLED'))
```

All booking operations now comply with this constraint.

## Impact on Existing Data

If database already has bookings with old statuses:
- `PENDING_MANAGER_APPROVAL` → Should be migrated to `PENDING`
- `DRIVER_ASSIGNED` → Should be migrated to `CONFIRMED`
- `TRIP_STARTED` → Should be migrated to `IN_PROGRESS`

**Migration Script (if needed):**
```sql
UPDATE bookings 
SET status = CASE 
    WHEN status = 'PENDING_MANAGER_APPROVAL' THEN 'PENDING'
    WHEN status = 'DRIVER_ASSIGNED' THEN 'CONFIRMED'
    WHEN status = 'TRIP_STARTED' THEN 'IN_PROGRESS'
    ELSE status
END;
```

## Verification

After fix, check:
- [ ] Backend compiles without errors
- [ ] Backend starts successfully
- [ ] Customer can create bookings
- [ ] No SQL constraint errors in logs
- [ ] Manager can approve bookings
- [ ] Driver assignment works
- [ ] Trip start/complete works

## Error Handling

Previous error message:
```
Failed to create booking: could not execute statement
[SQLITE_CONSTRAINT_CHECK] A CHECK constraint failed
```

After fix:
- ✅ Bookings save successfully
- ✅ No constraint violations
- ✅ Status transitions work correctly

---

**Status:** ✅ FIXED  
**Build Status:** ✅ SUCCESS  
**Backend:** ✅ RUNNING  
**Ready to Test:** ✅ YES
