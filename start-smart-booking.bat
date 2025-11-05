@echo off
echo ========================================
echo    NeuroFleetX - Smart Booking Module
echo ========================================
echo.
echo This script will start the Smart Booking feature
echo.

echo [1/3] Starting Backend Server...
start "NeuroFleetX Backend" cmd /k "cd backend && mvn spring-boot:run"
echo Backend starting on http://localhost:8080
timeout /t 15 /nobreak > nul

echo.
echo [2/3] Starting Frontend Application...
start "NeuroFleetX Frontend" cmd /k "cd frontend && npm start"
echo Frontend starting on http://localhost:3000
timeout /t 10 /nobreak > nul

echo.
echo [3/3] Opening Browser...
timeout /t 5 /nobreak > nul
start http://localhost:3000

echo.
echo ========================================
echo    Smart Booking Module Started!
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Test Credentials:
echo   Customer: customer1 / admin123
echo   Customer: customer2 / admin123
echo.
echo Navigate to "Smart Booking" tab to test!
echo.
echo Press any key to exit this window...
pause > nul
