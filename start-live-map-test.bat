@echo off
echo ========================================
echo   NeuroFleetX - Live Map Test Startup
echo ========================================
echo.
echo This will start both backend and frontend for testing the Live Map
echo.

echo [1/2] Starting Backend Server...
start "NeuroFleetX Backend" cmd /k "cd backend && mvnw spring-boot:run"

echo Waiting 10 seconds for backend to initialize...
timeout /t 10 /nobreak

echo.
echo [2/2] Starting Frontend Development Server...
start "NeuroFleetX Frontend" cmd /k "cd frontend && npm start"

echo.
echo ========================================
echo   Startup Complete!
echo ========================================
echo.
echo Backend: http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Navigate to: Driver Portal ^> Live Map
echo.
echo Press any key to exit this window...
pause >nul
