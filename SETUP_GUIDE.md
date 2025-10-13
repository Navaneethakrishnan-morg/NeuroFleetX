# NeuroFleetX Setup Guide

## Step-by-Step Installation

### Step 1: Prerequisites Check

Ensure you have the following installed:

- **Java 17+**: `java -version`
- **Maven 3.6+**: `mvn -version`
- **Node.js 16+**: `node -version`
- **MySQL 8.0+**: `mysql --version`

### Step 2: Database Setup

1. Start MySQL server
2. Open MySQL command line or workbench
3. Run the database initialization script:

```bash
mysql -u root -p < database-init.sql
```

Or manually:

```sql
source database-init.sql
```

This will:
- Create the `neurofleetx` database
- Create all required tables
- Insert sample data (users, vehicles, bookings, maintenance)

### Step 3: Backend Configuration

1. Navigate to `backend/src/main/resources/application.properties`
2. Update MySQL credentials if different from defaults:

```properties
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 4: Install Dependencies

#### Backend:
```bash
cd backend
mvn clean install
```

#### Frontend:
```bash
cd frontend
npm install
```

### Step 5: Start the Application

#### Option A: Using Scripts (Windows)

**Terminal 1 - Backend:**
```bash
start-backend.bat
```

**Terminal 2 - Frontend:**
```bash
start-frontend.bat
```

#### Option B: Manual Start

**Terminal 1 - Backend:**
```bash
cd backend
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd frontend
npm start
```

### Step 6: Access the Application

1. Open browser and navigate to: `http://localhost:3000`
2. You'll see the NeuroFleetX Welcome Page with 4 portal options

### Step 7: Test Login

Use these default credentials (password: admin123 for all):

| Role     | Username  | Password  |
|----------|-----------|-----------|
| Admin    | admin     | admin123  |
| Manager  | manager1  | admin123  |
| Driver   | driver1   | admin123  |
| Customer | customer1 | admin123  |

## Troubleshooting

### Backend won't start
- Check MySQL is running
- Verify database credentials in `application.properties`
- Ensure port 8080 is available

### Frontend won't start
- Run `npm install` again
- Delete `node_modules` and `package-lock.json`, then reinstall
- Ensure port 3000 is available

### Database connection error
- Verify MySQL service is running
- Check database name: `neurofleetx`
- Verify user has proper permissions

### Login fails
- Ensure database is initialized with sample data
- Check backend console for errors
- Verify JWT secret is set in `application.properties`

## Project Structure

```
neuro/
├── backend/                    # Spring Boot Backend
│   ├── src/main/java/com/neurofleetx/
│   │   ├── config/            # Security & CORS config
│   │   ├── controller/        # REST API endpoints
│   │   ├── dto/               # Data Transfer Objects
│   │   ├── model/             # JPA Entities
│   │   ├── repository/        # Database repositories
│   │   ├── security/          # JWT & Security
│   │   └── service/           # Business logic
│   └── src/main/resources/
│       └── application.properties
├── frontend/                   # React Frontend
│   ├── src/
│   │   ├── components/        # Reusable components
│   │   ├── pages/             # Page components
│   │   ├── services/          # API services
│   │   └── utils/             # Utility functions
│   └── package.json
├── database-init.sql          # Database initialization
├── README.md                  # Main documentation
└── SETUP_GUIDE.md            # This file
```

## Next Steps

1. Explore different portal dashboards
2. Test CRUD operations on vehicles
3. Create bookings as a customer
4. Check maintenance alerts as admin
5. Update vehicle telemetry as manager

## Support

For issues or questions, please refer to the main README.md or check the application logs.

---

Happy Fleet Managing! 🚗✨
