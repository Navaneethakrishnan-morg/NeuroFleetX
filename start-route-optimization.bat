@echo off
echo ============================================
echo   NeuroFleetX Route Optimization System
echo   Starting Backend and Frontend...
echo ============================================
echo.

echo Starting Backend Server (Port 8080)...
start "NeuroFleetX Backend" cmd /k "cd backend && mvn spring-boot:run"

timeout /t 10 /nobreak >nul

echo Starting Frontend Server (Port 3000)...
start "NeuroFleetX Frontend" cmd /k "cd frontend && npm start"

timeout /t 5 /nobreak >nul

echo.
echo ============================================
echo   Services Starting...
echo ============================================
echo   Backend:  http://localhost:8080
echo   Frontend: http://localhost:3000
echo   Route Optimization: 
echo     Manager: http://localhost:3000/manager/route-optimization
echo     Admin:   http://localhost:3000/admin/route-optimization
echo ============================================
echo.
echo Press any key to open browser...
pause >nul

start http://localhost:3000/manager/route-optimization

echo.
echo Services are running in separate windows.
echo Close those windows to stop the services.
echo.
pause
