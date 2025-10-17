# Troubleshooting: Can't See Vehicles in Customer Portal

## Issue
Vehicles added in Admin Portal are not visible in Customer Portal.

## Root Cause
Backend needs to be restarted with the new `/api/customer/vehicles` endpoint.

## Solution Steps

### 1. Stop Current Backend
- Go to the terminal/command prompt running the backend
- Press `Ctrl + C` to stop it

### 2. Rebuild Backend
```bash
cd C:\Users\nk349\Documents\augment-projects\neuro\backend
mvn clean package -DskipTests
```

### 3. Restart Backend
```bash
cd C:\Users\nk349\Documents\augment-projects\neuro
start-backend.bat
```

### 4. Restart Frontend (if needed)
```bash
cd C:\Users\nk349\Documents\augment-projects\neuro
start-frontend.bat
```

### 5. Clear Browser Cache
- Press `Ctrl + Shift + Delete`
- Clear cached images and files
- Or just do a hard refresh: `Ctrl + F5`

### 6. Test Again
1. Login as **Admin**
2. Go to "Vehicle Management" tab
3. Verify your BMW M4 is listed there
4. Note the status (should be AVAILABLE)
5. Logout
6. Login as **Customer**
7. Go to "Browse Vehicles" tab
8. You should now see the BMW M4

## Additional Checks

### Check 1: Verify Vehicle in Database
If backend is running, visit in browser:
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (leave blank)
- Run query: `SELECT * FROM VEHICLES;`

### Check 2: Check Browser Console
1. Open browser Developer Tools (F12)
2. Go to Console tab
3. Look for any red errors
4. Common errors:
   - `403 Forbidden` → Backend not restarted
   - `401 Unauthorized` → Login again
   - `Network Error` → Backend not running

### Check 3: Verify API Endpoints
With backend running, you can test directly:

**Admin Endpoint** (after logging in as admin):
```
GET http://localhost:8080/api/admin/vehicles
Headers: Authorization: Bearer <your-token>
```

**Customer Endpoint** (after logging in as customer):
```
GET http://localhost:8080/api/customer/vehicles
Headers: Authorization: Bearer <your-token>
```

### Check 4: Verify JWT Token
Open Browser Console (F12) and run:
```javascript
console.log('Token:', localStorage.getItem('token'));
console.log('Role:', localStorage.getItem('role'));
console.log('Username:', localStorage.getItem('username'));
```

Make sure:
- Token exists
- Role is 'CUSTOMER'
- Username is correct

## Quick Verification Script

Open Browser Console (F12) on Customer Dashboard and paste:

```javascript
// Check current state
console.log('=== DEBUGGING CUSTOMER VEHICLES ===');
console.log('Token:', localStorage.getItem('token'));
console.log('Role:', localStorage.getItem('role'));

// Make direct API call
fetch('http://localhost:8080/api/customer/vehicles', {
  headers: {
    'Authorization': 'Bearer ' + localStorage.getItem('token'),
    'Content-Type': 'application/json'
  }
})
.then(res => {
  console.log('Response Status:', res.status);
  return res.json();
})
.then(data => {
  console.log('Vehicles Found:', data.length);
  console.log('Vehicles:', data);
})
.catch(err => console.error('Error:', err));
```

## Common Issues & Solutions

### Issue 1: 403 Forbidden Error
**Cause**: Backend running old code without customer endpoint
**Solution**: Stop backend, rebuild, and restart (Steps 1-3 above)

### Issue 2: Empty Array Returned
**Cause**: No vehicles in database
**Solution**: 
- Login as Admin
- Add vehicles through "Vehicle Management"
- Make sure Status = AVAILABLE

### Issue 3: Network Error
**Cause**: Backend not running
**Solution**: 
- Check if backend terminal shows errors
- Restart backend with `start-backend.bat`

### Issue 4: Token Expired
**Cause**: Login session expired
**Solution**: 
- Logout
- Login again
- Try viewing vehicles

## Still Not Working?

If vehicles still don't show after all these steps:

1. **Check Admin Dashboard First**
   - Login as Admin
   - Go to "Vehicle Management"
   - Take a screenshot of the vehicles list
   - Verify BMW M4 is there with Status = AVAILABLE

2. **Check Backend Console**
   - Look at the backend terminal
   - Check for any error messages
   - Should see: "Started NeuroFleetXApplication"

3. **Check Frontend Console**
   - Open Browser DevTools (F12)
   - Go to Network tab
   - Filter by "customer/vehicles"
   - Click on the request
   - Check Response

4. **Manual Database Check**
   - Visit: http://localhost:8080/h2-console
   - Login with credentials above
   - Run: `SELECT * FROM VEHICLES WHERE MODEL LIKE '%BMW%';`
   - Verify BMW M4 exists

## Expected Behavior

✅ **What Should Happen:**
1. Admin adds "BMW M4" with Status = AVAILABLE
2. Vehicle appears in Admin's "Vehicle Management"
3. Customer logs in and sees "BMW M4" in "Browse Vehicles"
4. BMW M4 has green "Book Now" button
5. Clicking "Book Now" opens booking modal

✅ **Customer Portal Should Show:**
- All vehicles (regardless of status)
- Status badge on each vehicle
- Green "Book Now" button for AVAILABLE vehicles
- Gray disabled button for other statuses

## Contact
If none of these steps work, provide:
1. Screenshot of Admin Vehicle Management showing BMW M4
2. Screenshot of Customer Browse Vehicles (empty or showing other vehicles)
3. Browser Console errors (F12 → Console tab)
4. Backend console output
