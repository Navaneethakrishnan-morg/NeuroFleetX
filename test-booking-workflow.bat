@echo off
echo ============================================
echo Testing Booking Approval Workflow
echo ============================================
echo.
echo This script will test the complete booking approval workflow:
echo 1. Create a booking (PENDING_MANAGER_APPROVAL status)
echo 2. Manager views pending bookings
echo 3. Manager approves and assigns driver
echo 4. Driver views assigned booking
echo 5. Driver starts trip
echo.
echo ============================================
echo Prerequisites:
echo - Backend must be running on localhost:8080
echo - Frontend must be running on localhost:3000
echo ============================================
echo.

echo Step 1: Testing Booking Creation
echo ---------------------------------
curl -X POST "http://localhost:8080/api/customer/bookings?username=customer1" ^
  -H "Content-Type: application/json" ^
  -d "{\"vehicle\":{\"id\":1},\"startTime\":\"2025-11-15T10:00:00\",\"endTime\":\"2025-11-15T14:00:00\",\"pickupLocation\":\"123 Main St, New York\",\"dropoffLocation\":\"456 Park Ave, New York\"}"
echo.
echo.

echo Step 2: Viewing Pending Bookings (Manager)
echo -------------------------------------------
curl -X GET "http://localhost:8080/api/manager/bookings/pending"
echo.
echo.

echo Step 3: Get Available Drivers
echo ------------------------------
curl -X GET "http://localhost:8080/api/manager/drivers/available"
echo.
echo.

echo ============================================
echo Manual Testing Instructions:
echo ============================================
echo.
echo 1. Customer Login:
echo    - Email: customer1@neurofleetx.com
echo    - Password: admin123
echo    - Navigate to booking section
echo    - Create a new booking
echo    - Verify status shows "PENDING MANAGER APPROVAL"
echo.
echo 2. Manager Login:
echo    - Email: manager@neurofleetx.com
echo    - Password: admin123
echo    - Go to "Pending Bookings" tab
echo    - Click "Approve" on a booking
echo    - Select a driver and click "Assign Driver"
echo.
echo 3. Driver Login:
echo    - Email: driver1@neurofleetx.com
echo    - Password: admin123
echo    - Go to "My Trips" tab
echo    - View assigned booking
echo    - Click "Start Trip"
echo    - Verify status changes to "TRIP STARTED"
echo.
echo ============================================
echo.
pause
