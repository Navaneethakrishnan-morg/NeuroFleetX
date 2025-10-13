# ✅ NeuroFleetX Quick Start Guide

## 🎉 Your System is Running!

Both backend and frontend are **LIVE and WORKING**!

---

## 🔧 How to Use the Full System

### Step 1: Open the Application
Open your browser and go to: **http://localhost:3001**

### Step 2: Clear Your Browser Cache
Press **Ctrl + Shift + R** (or **Ctrl + F5**) to hard refresh and clear cache.

### Step 3: Sign Up for Real

**⚠️ IMPORTANT: Don't use Demo Mode!**

1. On the welcome page, click **"Sign up now"** (at the bottom)
2. Fill in the form:
   - **Full Name**: Your Name
   - **Username**: yourname (any username you want)
   - **Email**: your@email.com
   - **Phone**: 1234567890 (optional)
   - **Password**: yourpassword
   - **Role**: Choose CUSTOMER, DRIVER, MANAGER, or ADMIN
3. Click **Sign Up**
4. You'll see "Registration successful! Redirecting to login..."

### Step 4: Login
1. You'll be redirected to the login page
2. Enter your **username** and **password**
3. Click **Sign In**
4. You'll be taken to your role-specific dashboard!

---

## ✅ Test Accounts Created

I already created a test account for you:

| Username | Password | Role |
|----------|----------|------|
| testuser | test123 | CUSTOMER |

**Try logging in with this account:**
1. Go to http://localhost:3001
2. Click the **Customer Portal** card (green)
3. Login with: `testuser` / `test123`
4. You'll see the Customer Dashboard with **real data**!

---

## 🐛 Troubleshooting

### Problem: "Backend not connected" message
**Solution:** 
- Hard refresh: **Ctrl + Shift + R**
- Close and reopen the browser tab
- Make sure you're at http://localhost:3001 (not 3000)

### Problem: Login button does nothing
**Solution:**
1. Press **F12** to open Developer Tools
2. Go to **Console** tab
3. Look for any red error messages
4. Hard refresh the page (**Ctrl + Shift + R**)

### Problem: Still seeing demo mode
**Solution:**
- Clear browser cache completely
- Or use incognito/private window: **Ctrl + Shift + N**
- Go to http://localhost:3001

---

## 🎯 What You Can Do Now

### As CUSTOMER:
- ✅ Browse available vehicles
- ✅ Filter by type and electric/petrol
- ✅ Create bookings
- ✅ View your booking history

### As DRIVER:
- ✅ View assigned trips
- ✅ See available vehicles
- ✅ Track routes

### As MANAGER:
- ✅ View entire fleet
- ✅ Update vehicle telemetry
- ✅ Manage maintenance schedule
- ✅ Monitor fleet status

### As ADMIN:
- ✅ Full system access
- ✅ View all KPIs and analytics
- ✅ Manage all users
- ✅ Control entire fleet
- ✅ View maintenance alerts
- ✅ Access all reports

---

## 📊 System Status

| Service | Status | URL |
|---------|--------|-----|
| Frontend | ✅ RUNNING | http://localhost:3001 |
| Backend API | ✅ RUNNING | http://localhost:8080 |
| H2 Database | ✅ CONNECTED | In-Memory |
| Authentication | ✅ WORKING | JWT Enabled |

---

## 🚀 Quick Test Right Now

**Try this in your browser:**

1. **Open a new incognito window** (Ctrl + Shift + N)
2. Go to: http://localhost:3001
3. Click **Customer Portal** (green card)
4. Login with:
   - Username: `testuser`
   - Password: `test123`
5. **Success!** You should see the Customer Dashboard with real data!

---

## 💡 Tips

- **Create multiple accounts** with different roles to test all features
- **Use different browsers** to login with different accounts simultaneously
- **Check H2 Console** at http://localhost:8080/h2-console to see your data
  - JDBC URL: `jdbc:h2:mem:neurofleetx`
  - Username: `sa`
  - Password: (leave empty)

---

## 🎉 You're All Set!

Your NeuroFleetX system is fully operational. Create an account and start exploring! 🚗✨

**Need help?** Open Developer Console (F12) and check for errors in the Console tab.
