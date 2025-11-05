@echo off
echo ========================================
echo    NeuroFleetX - Urban Mobility Insights
echo ========================================
echo.
echo Starting Urban Mobility Analytics Dashboard
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
echo    Urban Insights Dashboard Started!
echo ========================================
echo.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Admin Login Credentials:
echo   Username: admin
echo   Password: admin123
echo.
echo Navigate to: Admin Dashboard ^> Urban Insights
echo.
echo Features:
echo   - Real-time KPI dashboard
echo   - Interactive fleet heatmap
echo   - Hourly activity charts
echo   - Downloadable CSV reports
echo   - Auto-refresh every 30 seconds
echo.
echo Press any key to exit this window...
pause > nul
