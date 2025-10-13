# NeuroFleetX - AI-Powered Urban Fleet Management System

## 🚀 Overview

NeuroFleetX is a comprehensive AI-driven urban mobility platform that integrates AI, IoT, and geospatial data to optimize fleet operations, route planning, and predictive maintenance for modern urban transport systems.

## 🎯 Features

### Multi-Role Portal System
- **Admin Portal** - Full system access with KPIs, analytics, and user management
- **Manager Portal** - Fleet operations, route allocation, and maintenance scheduling
- **Driver Portal** - Trip management and real-time route updates
- **Customer Portal** - Vehicle booking with AI-powered recommendations

### Core Modules
- **Fleet Inventory Management** - CRUD operations with real-time telemetry
- **Route Optimization** - AI-powered route planning with traffic analysis
- **Predictive Maintenance** - Health analytics and proactive alerts
- **Smart Booking System** - AI recommendations based on user preferences
- **Real-time Analytics** - Comprehensive dashboards with charts and KPIs

## 🛠️ Tech Stack

### Backend
- Java Spring Boot 3.2.0
- Spring Security + JWT Authentication
- Spring Data JPA
- MySQL Database
- WebSocket for real-time updates

### Frontend
- React 18
- Tailwind CSS v2.x
- React Router v6
- Axios for API calls
- Chart.js for analytics visualization
- Leaflet.js for maps

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+
- Node.js 16+ and npm
- MySQL 8.0+

## 🚀 Quick Start

### 1. Database Setup

```sql
CREATE DATABASE neurofleetx;
```

Update database credentials in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/neurofleetx
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

### 2. Backend Setup

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### 3. Frontend Setup

```bash
cd frontend
npm install
npm start
```

The frontend will start on `http://localhost:3000`

## 👤 Default Test Users

After running the application, you can create users via the signup page or use these test credentials (after manual database insertion):

| Role     | Username | Password |
|----------|----------|----------|
| Admin    | admin    | admin123 |
| Manager  | manager  | manager123 |
| Driver   | driver   | driver123 |
| Customer | customer | customer123 |

### Creating Test Users (SQL)

```sql
-- Password: admin123 (BCrypt encoded)
INSERT INTO users (username, email, password, full_name, phone, role, active, created_at) VALUES
('admin', 'admin@neurofleetx.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Admin User', '555-0001', 'ADMIN', true, NOW()),
('manager', 'manager@neurofleetx.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Manager User', '555-0002', 'MANAGER', true, NOW()),
('driver', 'driver@neurofleetx.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Driver User', '555-0003', 'DRIVER', true, NOW()),
('customer', 'customer@neurofleetx.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Customer User', '555-0004', 'CUSTOMER', true, NOW());
```

### Sample Vehicle Data

```sql
INSERT INTO vehicles (vehicle_number, model, manufacturer, type, capacity, is_electric, status, latitude, longitude, battery_level, fuel_level, mileage, health_score, created_at) VALUES
('NF-001', 'Model S', 'Tesla', 'SEDAN', 5, true, 'AVAILABLE', 40.7128, -74.0060, 85, null, 12000, 95, NOW()),
('NF-002', 'Camry', 'Toyota', 'SEDAN', 5, false, 'AVAILABLE', 40.7580, -73.9855, null, 70, 25000, 88, NOW()),
('NF-003', 'Explorer', 'Ford', 'SUV', 7, false, 'IN_USE', 40.7489, -73.9680, null, 55, 35000, 82, NOW()),
('NF-004', 'Transit', 'Ford', 'VAN', 12, false, 'AVAILABLE', 40.7614, -73.9776, null, 90, 18000, 92, NOW()),
('NF-005', 'Leaf', 'Nissan', 'SEDAN', 5, true, 'MAINTENANCE', 40.7306, -73.9352, 45, null, 45000, 65, NOW());
```

## 🎨 UI/UX Design

The application features a modern, futuristic design with:
- Gradient backgrounds (navy → teal)
- Glassmorphism effects
- Smooth transitions and animations
- Responsive grid layouts
- Clean typography (Inter font family)
- Role-specific color schemes

## 📱 Application Flow

1. **Welcome Page** - Landing page with 4 role cards
2. **Role Selection** - Choose Admin, Manager, Driver, or Customer
3. **Login/Signup** - Authenticate or create new account
4. **Dashboard** - Role-specific dashboard with relevant features
5. **Feature Access** - Access modules based on role permissions

## 🔐 Security

- JWT-based authentication
- Role-based access control (RBAC)
- Password encryption with BCrypt
- CORS configuration for frontend-backend communication
- Secure API endpoints with Spring Security

## 📊 API Endpoints

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/signup` - User registration

### Vehicles
- `GET /api/admin/vehicles` - Get all vehicles (Admin)
- `GET /api/manager/vehicles` - Get vehicles (Manager)
- `POST /api/admin/vehicles` - Create vehicle
- `PUT /api/admin/vehicles/{id}` - Update vehicle
- `DELETE /api/admin/vehicles/{id}` - Delete vehicle
- `PUT /api/vehicles/{id}/telemetry` - Update telemetry

### Bookings
- `GET /api/admin/bookings` - Get all bookings (Admin)
- `GET /api/customer/bookings` - Get customer bookings
- `POST /api/customer/bookings` - Create booking
- `PUT /api/bookings/{id}` - Update booking

### Maintenance
- `GET /api/admin/maintenance` - Get all maintenance (Admin)
- `GET /api/manager/maintenance` - Get maintenance (Manager)
- `GET /api/maintenance/predictive` - Get predictive maintenance
- `POST /api/admin/maintenance` - Create maintenance record
- `PUT /api/admin/maintenance/{id}` - Update maintenance

## 🔄 Future Enhancements

- WebSocket integration for real-time updates
- Advanced route optimization with Google Maps API
- Machine learning models for predictive analytics
- Mobile app (React Native)
- IoT device integration
- Advanced analytics with Chart.js/ApexCharts
- Email notifications
- Report generation (PDF/CSV)

## 📝 License

This project is part of a demonstration/educational system.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests.

## 📞 Support

For support, please contact the development team.

---

Built with ❤️ using Spring Boot and React
