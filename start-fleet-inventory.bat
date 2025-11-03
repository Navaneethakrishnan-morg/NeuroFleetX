@echo off
echo ========================================
echo Fleet Inventory System - Quick Start
echo ========================================
echo.

echo [1/3] Checking backend...
cd backend
if not exist "neurofleetx.db" (
    echo WARNING: Database not found. Will be created on first run.
)

echo.
echo [2/3] Starting Backend Server...
echo Backend will start on http://localhost:8080
echo Telemetry simulator will run every 3 seconds
echo.
start cmd /k "cd /d %cd% && mvn spring-boot:run"

timeout /t 5 /nobreak > nul

echo.
echo [3/3] Starting Frontend...
echo Frontend will open at http://localhost:3000
echo.
cd ..\frontend
start cmd /k "cd /d %cd% && npm start"

echo.
echo ========================================
echo System Starting...
echo ========================================
echo.
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo Fleet Inventory: http://localhost:3000/manager/fleet-inventory
echo.
echo Press any key to close this window...
pause > nul
