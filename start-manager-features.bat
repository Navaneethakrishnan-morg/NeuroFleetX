@echo off
echo ========================================
echo Manager Dashboard Features Startup
echo ========================================
echo.

echo Starting Backend (Java Spring Boot)...
start "NeuroFleetX Backend" cmd /k "cd backend && mvn spring-boot:run"
timeout /t 5

echo.
echo Starting AI Service (Python)...
start "AI Route Optimizer" cmd /k "cd ai-services && python app.py"
timeout /t 3

echo.
echo Starting Frontend (React)...
start "NeuroFleetX Frontend" cmd /k "cd frontend && npm start"

echo.
echo ========================================
echo All services started!
echo ========================================
echo Backend: http://localhost:8080
echo AI Service: http://localhost:5000
echo Frontend: http://localhost:3000
echo ========================================
echo.
echo Press any key to exit...
pause > nul
