@echo off
echo Starting NeuroFleetX Backend with SQLite...
cd backend
set JWT_SECRET=neurofleetx-secret-key-2025-change-this-in-production
java -jar target\neurofleetx-backend-1.0.0.jar
