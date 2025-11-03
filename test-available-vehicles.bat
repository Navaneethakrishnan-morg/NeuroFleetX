@echo off
echo ========================================
echo   Test AVAILABLE Vehicles on Map
echo ========================================
echo.
echo This script will:
echo 1. Initialize GPS for all vehicles
echo 2. Display vehicle status
echo 3. Open the Live Map
echo.
pause

echo.
echo [Step 1] Initializing GPS coordinates for all vehicles...
echo.
curl -X POST http://localhost:8080/api/admin/vehicles/initialize-gps
echo.
echo.

echo [Step 2] Fetching all vehicles...
echo.
curl -s http://localhost:8080/api/manager/vehicles | findstr /i "vehicleNumber status latitude longitude"
echo.
echo.

echo [Step 3] Vehicle Summary:
echo.
curl -s http://localhost:8080/api/manager/vehicles | findstr /i /c:"\"status\"" | findstr /c:"AVAILABLE" /c:"IN_USE" /c:"MAINTENANCE"
echo.
echo.

echo ========================================
echo   Setup Complete!
echo ========================================
echo.
echo AVAILABLE vehicles should now appear on the map.
echo.
echo Next Steps:
echo 1. Make sure backend is running (port 8080)
echo 2. Make sure frontend is running (port 3000)
echo 3. Navigate to: Driver Portal ^> Live Map
echo 4. You should see ALL vehicles including AVAILABLE ones
echo.
echo AVAILABLE vehicles will show:
echo  - Fixed location on map
echo  - Speed: 0 km/h
echo  - Status: AVAILABLE
echo  - Battery/Fuel level
echo.
echo IN_USE vehicles will show:
echo  - Moving location (updates every 3 seconds)
echo  - Speed: 30-70 km/h
echo  - Status: IN_USE
echo  - Depleting battery/fuel
echo.
echo Press any key to exit...
pause >nul
