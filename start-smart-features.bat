@echo off
echo ========================================
echo Starting Smart Booking and Live Tracking
echo ========================================
echo.

echo [1/2] Starting Backend Server...
start cmd /k "cd backend && mvn spring-boot:run"

echo Waiting 10 seconds for backend to initialize...
timeout /t 10 /nobreak > nul

echo [2/2] Starting Frontend Application...
start cmd /k "cd frontend && npm start"

echo.
echo ========================================
echo Both services are starting!
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Login as customer1/admin123 to test
echo.
echo Features available:
echo  - Smart Booking with AI recommendations
echo  - Live Vehicle Tracking with Street Maps
echo.
echo Press any key to exit this window...
pause > nul
