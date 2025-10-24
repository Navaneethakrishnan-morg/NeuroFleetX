# NeuroFleetX Setup Guide

## 🚀 Quick Start with SQLite

### Prerequisites
- Java 17 or higher
- Node.js 16+ and npm
- Maven 3.6+

### Backend Setup (SQLite)

1. **Build the backend:**
   ```bash
   cd backend
   mvn clean package -DskipTests
   ```

2. **Run the backend:**
   ```bash
   # Windows
   ..\start-backend.bat
   
   # Or manually:
   set JWT_SECRET=your-secret-key
   java -jar target\neurofleetx-backend-1.0.0.jar
   ```

The backend will:
- Start on `http://localhost:8080`
- Create `neurofleetx.db` SQLite database file automatically
- Initialize database schema on first run

### Frontend Setup

1. **Install dependencies:**
   ```bash
   cd frontend
   npm install
   ```

2. **Run the frontend:**
   ```bash
   # Windows
   ..\start-frontend.bat
   
   # Or manually:
   npm start
   ```

The frontend will start on `http://localhost:3000`

## 📱 Portal Access

### Admin Portal
- **Features:** Vehicle Management, User Management, Analytics, Settings
- **Navigation:** Overview → Vehicles → Users → Analytics → Settings
- **Emerald Theme:** Dark background with neon cyan/emerald accents

### Driver Portal
- **Features:** My Trips, Live Map View, Earnings Dashboard, Support Chat
- **Navigation:** My Trips → Live Map → Earnings → Support
- **Trip Management:** Start/Complete trips with glowing action buttons

### Customer Portal
- **Features:** Browse Vehicles, Book Rides, View Bookings, Trip History
- **Booking Flow:** Filter vehicles → Select → Book with emerald glow effects

## 🎨 Design Features

All portals feature:
- ✨ Emerald dark futuristic theme (#00A86B primary, #0B0E14 background)
- 🌟 Glowing hover effects and neon accent highlights
- 📊 Interactive charts and real-time status indicators
- 🔄 Smooth transitions and animations
- 💳 Card-based layouts with glass morphism effects

## 🗄️ Database

**SQLite Configuration:**
- Database file: `neurofleetx.db` (created automatically)
- Location: Backend root directory
- Auto-schema generation on startup
- Persistent storage (data survives restarts)

## 🔐 Default Login Credentials

```
Admin:
- Username: admin
- Password: admin123

Driver:
- Username: driver1
- Password: admin123

Customer:
- Username: customer1
- Password: admin123
```

## 🛠️ Development

**Backend (Port 8080):**
- Spring Boot 3.2.0
- SQLite with Hibernate
- JWT Authentication
- RESTful APIs

**Frontend (Port 3000):**
- React 18
- Tailwind CSS with custom emerald theme
- React Router for navigation
- Axios for API calls

## 📝 Notes

- SQLite database file will be created in the backend directory on first run
- All data persists between application restarts
- JWT tokens expire after 24 hours
- CORS configured for localhost:3000

## 🐛 Troubleshooting

**Backend won't start:**
- Ensure Java 17+ is installed
- Check if port 8080 is available
- Verify SQLite driver is included in dependencies

**Frontend errors:**
- Run `npm install` to ensure all dependencies are installed
- Check if port 3000 is available
- Verify backend is running on port 8080

**Database issues:**
- Delete `neurofleetx.db` file to reset database
- Backend will recreate schema on next startup
