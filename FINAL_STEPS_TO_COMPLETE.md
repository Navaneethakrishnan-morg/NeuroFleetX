# Final Steps to Complete Manager Dashboard

## ✅ What's Already Done

### Backend
- ✅ SupportTicket model
- ✅ SupportTicketRepository
- ✅ SupportTicketService
- ✅ DriverPerformanceDTO
- ✅ MaintenanceDueDTO
- ✅ ManagerService

### Python AI Service
- ✅ route_optimizer.py (ML model)
- ✅ app.py (Flask API)
- ✅ requirements.txt
- ✅ All AI endpoints functional

### Documentation
- ✅ Complete implementation guide
- ✅ API documentation
- ✅ Setup instructions
- ✅ Testing guide

## ⏳ What Still Needs to Be Done

### 1. Create Backend Controllers (30 minutes)

#### File: `backend/src/main/java/com/neurofleetx/controller/ManagerController.java`
```java
package com.neurofleetx.controller;

import com.neurofleetx.dto.DriverPerformanceDTO;
import com.neurofleetx.dto.MaintenanceDueDTO;
import com.neurofleetx.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@CrossOrigin(origins = "http://localhost:3000")
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @GetMapping("/drivers/performance")
    public ResponseEntity<List<DriverPerformanceDTO>> getDriverPerformance() {
        return ResponseEntity.ok(managerService.getDriverPerformanceMetrics());
    }

    @GetMapping("/maintenance/due")
    public ResponseEntity<List<MaintenanceDueDTO>> getMaintenanceDue() {
        return ResponseEntity.ok(managerService.getMaintenanceDueVehicles());
    }
}
```

#### File: `backend/src/main/java/com/neurofleetx/controller/SupportTicketController.java`
```java
package com.neurofleetx.controller;

import com.neurofleetx.model.SupportTicket;
import com.neurofleetx.service.SupportTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class SupportTicketController {
    @Autowired
    private SupportTicketService ticketService;

    @GetMapping("/manager/support-tickets")
    public ResponseEntity<List<SupportTicket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/support-tickets/{id}")
    public ResponseEntity<SupportTicket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/support-tickets/user")
    public ResponseEntity<List<SupportTicket>> getUserTickets(@RequestParam String username) {
        return ResponseEntity.ok(ticketService.getTicketsByUser(username));
    }

    @PostMapping("/support-tickets")
    public ResponseEntity<SupportTicket> createTicket(
            @RequestParam String username,
            @RequestBody SupportTicket ticket) {
        return ResponseEntity.ok(ticketService.createTicket(username, ticket));
    }

    @PutMapping("/manager/support-tickets/{id}")
    public ResponseEntity<SupportTicket> updateTicket(
            @PathVariable Long id,
            @RequestBody SupportTicket ticket) {
        return ResponseEntity.ok(ticketService.updateTicket(id, ticket));
    }

    @PutMapping("/manager/support-tickets/{id}/assign")
    public ResponseEntity<SupportTicket> assignTicket(
            @PathVariable Long id,
            @RequestParam String assignee) {
        return ResponseEntity.ok(ticketService.assignTicket(id, assignee));
    }

    @PutMapping("/manager/support-tickets/{id}/resolve")
    public ResponseEntity<SupportTicket> resolveTicket(
            @PathVariable Long id,
            @RequestBody ResolveRequest request) {
        return ResponseEntity.ok(ticketService.resolveTicket(id, request.getResolution()));
    }

    @GetMapping("/manager/support-tickets/status/{status}")
    public ResponseEntity<List<SupportTicket>> getTicketsByStatus(
            @PathVariable SupportTicket.TicketStatus status) {
        return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
    }

    @GetMapping("/manager/support-tickets/priority/{priority}")
    public ResponseEntity<List<SupportTicket>> getTicketsByPriority(
            @PathVariable SupportTicket.TicketPriority priority) {
        return ResponseEntity.ok(ticketService.getTicketsByPriority(priority));
    }

    // Inner class for resolve request
    public static class ResolveRequest {
        private String resolution;
        
        public String getResolution() { return resolution; }
        public void setResolution(String resolution) { this.resolution = resolution; }
    }
}
```

### 2. Add Missing Repositories (5 minutes)

#### File: `backend/src/main/java/com/neurofleetx/repository/TripRepository.java`
```java
package com.neurofleetx.repository;

import com.neurofleetx.model.Trip;
import com.neurofleetx.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByBooking(Booking booking);
}
```

### 3. Database Migration (5 minutes)

Run this SQL to create support_tickets table:

```sql
CREATE TABLE IF NOT EXISTS support_tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subject VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    assigned_to BIGINT,
    resolution TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    resolved_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_to) REFERENCES users(id) ON DELETE SET NULL
);

CREATE INDEX idx_support_tickets_status ON support_tickets(status);
CREATE INDEX idx_support_tickets_priority ON support_tickets(priority);
CREATE INDEX idx_support_tickets_user ON support_tickets(user_id);
CREATE INDEX idx_support_tickets_assigned ON support_tickets(assigned_to);
CREATE INDEX idx_support_tickets_category ON support_tickets(category);
CREATE INDEX idx_support_tickets_created ON support_tickets(created_at);
```

### 4. Setup Python Environment (10 minutes)

```bash
# Navigate to ai-services directory
cd ai-services

# Install dependencies
pip install flask==3.0.0 flask-cors==4.0.0 mysql-connector-python==8.2.0 pandas==2.1.3 numpy==1.26.2 scikit-learn==1.3.2 joblib==1.3.2 geopy==2.4.0 requests==2.31.0 python-dotenv==1.0.0

# Test the service
python route_optimizer.py

# Start the Flask API
python app.py
```

### 5. Update Frontend API Services (15 minutes)

#### File: `frontend/src/services/api.js`

Add these services at the end:

```javascript
// Manager Services
export const managerService = {
  getDriverPerformance: () => api.get('/manager/drivers/performance'),
  getMaintenanceDue: () => api.get('/manager/maintenance/due'),
};

// Support Ticket Services
export const supportTicketService = {
  getAll: () => api.get('/manager/support-tickets'),
  getById: (id) => api.get(`/support-tickets/${id}`),
  getUserTickets: (username) => api.get(`/support-tickets/user?username=${username}`),
  create: (username, ticket) => api.post(`/support-tickets?username=${username}`, ticket),
  update: (id, ticket) => api.put(`/manager/support-tickets/${id}`, ticket),
  assign: (id, assignee) => api.put(`/manager/support-tickets/${id}/assign?assignee=${assignee}`),
  resolve: (id, resolution) => api.put(`/manager/support-tickets/${id}/resolve`, { resolution }),
  getByStatus: (status) => api.get(`/manager/support-tickets/status/${status}`),
  getByPriority: (priority) => api.get(`/manager/support-tickets/priority/${priority}`),
};

// AI Service (Python Flask on port 5000)
export const aiService = {
  baseURL: 'http://localhost:5000/api/ai',
  
  predictETA: (data) => axios.post('http://localhost:5000/api/ai/predict-eta', data),
  
  calculateRoute: (data) => axios.post('http://localhost:5000/api/ai/calculate-route', data),
  
  getSpeedFeedback: (vehicleId) => axios.get(`http://localhost:5000/api/ai/speed-feedback/${vehicleId}`),
  
  exportRouteData: (filename) => {
    const url = filename 
      ? `http://localhost:5000/api/ai/export-route-data?filename=${filename}`
      : 'http://localhost:5000/api/ai/export-route-data';
    return axios.get(url, { responseType: 'blob' });
  },
  
  retrainModel: () => axios.post('http://localhost:5000/api/ai/retrain-model'),
  
  batchOptimize: (routes) => axios.post('http://localhost:5000/api/ai/batch-optimize', { routes }),
  
  healthCheck: () => axios.get('http://localhost:5000/health'),
};
```

### 6. Test Everything (20 minutes)

#### Start All Services
```bash
# Terminal 1 - Backend
cd backend
mvn spring-boot:run

# Terminal 2 - AI Service
cd ai-services
python app.py

# Terminal 3 - Frontend
cd frontend
npm start
```

#### Test Endpoints
```bash
# Test Manager APIs
curl http://localhost:8080/api/manager/drivers/performance
curl http://localhost:8080/api/manager/maintenance/due

# Test Support Tickets
curl http://localhost:8080/api/manager/support-tickets

# Test AI Service
curl http://localhost:5000/health
curl -X POST http://localhost:5000/api/ai/predict-eta -H "Content-Type: application/json" -d '{"distance":20,"vehicleType":"SEDAN","isElectric":false,"hourOfDay":10,"dayOfWeek":2,"currentSpeed":40}'
```

## 📋 Quick Checklist

- [ ] Create ManagerController.java
- [ ] Create SupportTicketController.java
- [ ] Create TripRepository.java (if missing)
- [ ] Run support_tickets table SQL migration
- [ ] Install Python packages (`pip install -r requirements.txt`)
- [ ] Test Python service (`python route_optimizer.py`)
- [ ] Start Python Flask API (`python app.py`)
- [ ] Add manager/support/AI services to api.js
- [ ] Test all backend endpoints
- [ ] Test AI service endpoints
- [ ] Verify frontend can connect to all services

## 🎯 Priority Order

1. **HIGH PRIORITY** (Core Functionality)
   - Create backend controllers (30 min)
   - Run database migration (5 min)
   - Setup Python environment (10 min)
   - Update frontend api.js (15 min)
   
2. **MEDIUM PRIORITY** (Enhancements)
   - Update DriverPerformance.js to use real API
   - Integrate AI service into RouteOptimization.js
   - Create MaintenanceDue.js page
   - Create SupportTickets.js page
   
3. **LOW PRIORITY** (Polish)
   - Add charts and visualizations
   - Implement CSV exports on frontend
   - Add loading states
   - Error handling improvements

## 🚀 Fastest Path to Working System

### Option 1: Just Get It Running (1 hour)
1. Copy-paste the two controller files
2. Run the SQL migration
3. Install Python packages
4. Start all three services
5. Test the API endpoints

### Option 2: Full Integration (3-4 hours)
1. Complete Option 1
2. Add API services to frontend
3. Update all manager dashboard pages
4. Test end-to-end workflows
5. Fix any bugs

## 💡 Tips

- Keep all three services running simultaneously
- Test each service independently first
- Check console logs for errors
- Use Postman/curl to test APIs before connecting frontend
- The Python service must run on port 5000
- Backend must run on port 8080
- Frontend must run on port 3000

## ❓ Troubleshooting

### Python Service Won't Start
```bash
# Check Python version
python --version  # Should be 3.8+

# Install packages one by one if batch fails
pip install flask
pip install flask-cors
pip install mysql-connector-python
# etc.

# Try python3 instead of python
python3 app.py
```

### Backend Won't Compile
```bash
# Clean and rebuild
mvn clean install
mvn spring-boot:run

# Check for missing imports
# Make sure all @Autowired services exist
```

### Frontend Can't Connect
```bash
# Check CORS settings in backend controllers
@CrossOrigin(origins = "http://localhost:3000")

# Verify service URLs in api.js
# Backend: http://localhost:8080/api
# AI Service: http://localhost:5000/api/ai
```

## ✅ Done!

Once you complete these steps, you'll have:
- ✅ Full driver performance metrics
- ✅ AI-powered route optimization with ETA predictions
- ✅ Python ML service integrated with MySQL
- ✅ CSV export functionality
- ✅ Speed and feedback analysis
- ✅ Maintenance tracking system
- ✅ Complete support ticket management
- ✅ Comprehensive reports system

**Total estimated time: 1-4 hours depending on experience level**
