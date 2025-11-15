@echo off
echo ============================================
echo File Cleanup Script
echo ============================================
echo.
echo This will delete 52 duplicate/outdated files
echo Press Ctrl+C to cancel, or
pause
echo.

cd /d "%~dp0"

set count=0

echo Deleting duplicate implementation summaries...
if exist "IMPLEMENTATION_SUMMARY.md" del "IMPLEMENTATION_SUMMARY.md" && set /a count+=1 && echo Deleted: IMPLEMENTATION_SUMMARY.md
if exist "IMPLEMENTATION_SUMMARY_BOOKING_WORKFLOW.md" del "IMPLEMENTATION_SUMMARY_BOOKING_WORKFLOW.md" && set /a count+=1 && echo Deleted: IMPLEMENTATION_SUMMARY_BOOKING_WORKFLOW.md
if exist "FINAL_IMPLEMENTATION_SUMMARY.md" del "FINAL_IMPLEMENTATION_SUMMARY.md" && set /a count+=1 && echo Deleted: FINAL_IMPLEMENTATION_SUMMARY.md
if exist "IMPLEMENTATION_COMPLETE.md" del "IMPLEMENTATION_COMPLETE.md" && set /a count+=1 && echo Deleted: IMPLEMENTATION_COMPLETE.md
if exist "MODULE_5_IMPLEMENTATION_SUMMARY.md" del "MODULE_5_IMPLEMENTATION_SUMMARY.md" && set /a count+=1 && echo Deleted: MODULE_5_IMPLEMENTATION_SUMMARY.md
if exist "MODULE_6_IMPLEMENTATION_SUMMARY.md" del "MODULE_6_IMPLEMENTATION_SUMMARY.md" && set /a count+=1 && echo Deleted: MODULE_6_IMPLEMENTATION_SUMMARY.md
if exist "MODULE_5_FILES_CREATED.md" del "MODULE_5_FILES_CREATED.md" && set /a count+=1 && echo Deleted: MODULE_5_FILES_CREATED.md
if exist "MODULE_6_FILES_CREATED.md" del "MODULE_6_FILES_CREATED.md" && set /a count+=1 && echo Deleted: MODULE_6_FILES_CREATED.md
if exist "COMPLETE_FEATURES.md" del "COMPLETE_FEATURES.md" && set /a count+=1 && echo Deleted: COMPLETE_FEATURES.md
if exist "SUMMARY.md" del "SUMMARY.md" && set /a count+=1 && echo Deleted: SUMMARY.md

echo Deleting duplicate dashboard guides...
if exist "CUSTOMER_DASHBOARD_IMPLEMENTATION.md" del "CUSTOMER_DASHBOARD_IMPLEMENTATION.md" && set /a count+=1 && echo Deleted: CUSTOMER_DASHBOARD_IMPLEMENTATION.md
if exist "CUSTOMER_DASHBOARD_COMPLETE_IMPLEMENTATION.md" del "CUSTOMER_DASHBOARD_COMPLETE_IMPLEMENTATION.md" && set /a count+=1 && echo Deleted: CUSTOMER_DASHBOARD_COMPLETE_IMPLEMENTATION.md
if exist "COMPLETE_CUSTOMER_DASHBOARD_IMPLEMENTATION.md" del "COMPLETE_CUSTOMER_DASHBOARD_IMPLEMENTATION.md" && set /a count+=1 && echo Deleted: COMPLETE_CUSTOMER_DASHBOARD_IMPLEMENTATION.md
if exist "CUSTOMER_DASHBOARD_QUICK_START.md" del "CUSTOMER_DASHBOARD_QUICK_START.md" && set /a count+=1 && echo Deleted: CUSTOMER_DASHBOARD_QUICK_START.md
if exist "MANAGER_DASHBOARD_IMPLEMENTATION.md" del "MANAGER_DASHBOARD_IMPLEMENTATION.md" && set /a count+=1 && echo Deleted: MANAGER_DASHBOARD_IMPLEMENTATION.md
if exist "COMPLETE_MANAGER_DASHBOARD_SUMMARY.md" del "COMPLETE_MANAGER_DASHBOARD_SUMMARY.md" && set /a count+=1 && echo Deleted: COMPLETE_MANAGER_DASHBOARD_SUMMARY.md

echo Deleting duplicate booking guides...
if exist "CUSTOMER_BOOKING_GUIDE.md" del "CUSTOMER_BOOKING_GUIDE.md" && set /a count+=1 && echo Deleted: CUSTOMER_BOOKING_GUIDE.md
if exist "BOOKING_FIX_AND_GPS_TRACKING.md" del "BOOKING_FIX_AND_GPS_TRACKING.md" && set /a count+=1 && echo Deleted: BOOKING_FIX_AND_GPS_TRACKING.md
if exist "SMART_BOOKING_FIX.md" del "SMART_BOOKING_FIX.md" && set /a count+=1 && echo Deleted: SMART_BOOKING_FIX.md
if exist "SMART_BOOKING_UI_FIX.md" del "SMART_BOOKING_UI_FIX.md" && set /a count+=1 && echo Deleted: SMART_BOOKING_UI_FIX.md
if exist "SMART_BOOKING_QUICK_START.md" del "SMART_BOOKING_QUICK_START.md" && set /a count+=1 && echo Deleted: SMART_BOOKING_QUICK_START.md
if exist "FIXES_APPLIED_BOOKING_WORKFLOW.md" del "FIXES_APPLIED_BOOKING_WORKFLOW.md" && set /a count+=1 && echo Deleted: FIXES_APPLIED_BOOKING_WORKFLOW.md
if exist "FINAL_STEPS_TO_COMPLETE.md" del "FINAL_STEPS_TO_COMPLETE.md" && set /a count+=1 && echo Deleted: FINAL_STEPS_TO_COMPLETE.md

echo Deleting duplicate map/GPS guides...
if exist "AVAILABLE_VEHICLES_MAP_GUIDE.md" del "AVAILABLE_VEHICLES_MAP_GUIDE.md" && set /a count+=1 && echo Deleted: AVAILABLE_VEHICLES_MAP_GUIDE.md
if exist "AVAILABLE_VEHICLES_SUMMARY.md" del "AVAILABLE_VEHICLES_SUMMARY.md" && set /a count+=1 && echo Deleted: AVAILABLE_VEHICLES_SUMMARY.md
if exist "LIVE_MAP_SETUP.md" del "LIVE_MAP_SETUP.md" && set /a count+=1 && echo Deleted: LIVE_MAP_SETUP.md
if exist "MAP_FIX_SUMMARY.md" del "MAP_FIX_SUMMARY.md" && set /a count+=1 && echo Deleted: MAP_FIX_SUMMARY.md
if exist "STREET_MAP_IMPLEMENTATION.md" del "STREET_MAP_IMPLEMENTATION.md" && set /a count+=1 && echo Deleted: STREET_MAP_IMPLEMENTATION.md
if exist "STREET_MAP_QUICK_GUIDE.md" del "STREET_MAP_QUICK_GUIDE.md" && set /a count+=1 && echo Deleted: STREET_MAP_QUICK_GUIDE.md

echo Deleting duplicate route optimization...
if exist "ROUTE_OPTIMIZATION_IMPLEMENTATION.md" del "ROUTE_OPTIMIZATION_IMPLEMENTATION.md" && set /a count+=1 && echo Deleted: ROUTE_OPTIMIZATION_IMPLEMENTATION.md
if exist "AI_ROUTE_OPTIMIZATION_SUMMARY.md" del "AI_ROUTE_OPTIMIZATION_SUMMARY.md" && set /a count+=1 && echo Deleted: AI_ROUTE_OPTIMIZATION_SUMMARY.md
if exist "QUICK_TEST_ROUTE_OPTIMIZATION.md" del "QUICK_TEST_ROUTE_OPTIMIZATION.md" && set /a count+=1 && echo Deleted: QUICK_TEST_ROUTE_OPTIMIZATION.md
if exist "ROUTE_OPTIMIZATION_GUIDE.md" del "ROUTE_OPTIMIZATION_GUIDE.md" && set /a count+=1 && echo Deleted: ROUTE_OPTIMIZATION_GUIDE.md

echo Deleting duplicate fleet/vehicle guides...
if exist "FLEET_INVENTORY_IMPLEMENTATION.md" del "FLEET_INVENTORY_IMPLEMENTATION.md" && set /a count+=1 && echo Deleted: FLEET_INVENTORY_IMPLEMENTATION.md
if exist "FLEET_INVENTORY_GUIDE.md" del "FLEET_INVENTORY_GUIDE.md" && set /a count+=1 && echo Deleted: FLEET_INVENTORY_GUIDE.md
if exist "VEHICLE_FIX_SUMMARY.md" del "VEHICLE_FIX_SUMMARY.md" && set /a count+=1 && echo Deleted: VEHICLE_FIX_SUMMARY.md
if exist "CSS_FIX_SUMMARY.md" del "CSS_FIX_SUMMARY.md" && set /a count+=1 && echo Deleted: CSS_FIX_SUMMARY.md

echo Deleting other duplicate docs...
if exist "SMART_FEATURES_IMPLEMENTATION.md" del "SMART_FEATURES_IMPLEMENTATION.md" && set /a count+=1 && echo Deleted: SMART_FEATURES_IMPLEMENTATION.md
if exist "QUICK_START_SMART_FEATURES.md" del "QUICK_START_SMART_FEATURES.md" && set /a count+=1 && echo Deleted: QUICK_START_SMART_FEATURES.md
if exist "URBAN_MOBILITY_INSIGHTS_GUIDE.md" del "URBAN_MOBILITY_INSIGHTS_GUIDE.md" && set /a count+=1 && echo Deleted: URBAN_MOBILITY_INSIGHTS_GUIDE.md

echo Deleting duplicate test scripts...
if exist "start-fleet-inventory.bat" del "start-fleet-inventory.bat" && set /a count+=1 && echo Deleted: start-fleet-inventory.bat
if exist "start-live-map-test.bat" del "start-live-map-test.bat" && set /a count+=1 && echo Deleted: start-live-map-test.bat
if exist "start-manager-features.bat" del "start-manager-features.bat" && set /a count+=1 && echo Deleted: start-manager-features.bat
if exist "start-route-optimization.bat" del "start-route-optimization.bat" && set /a count+=1 && echo Deleted: start-route-optimization.bat
if exist "start-smart-booking.bat" del "start-smart-booking.bat" && set /a count+=1 && echo Deleted: start-smart-booking.bat
if exist "start-smart-features.bat" del "start-smart-features.bat" && set /a count+=1 && echo Deleted: start-smart-features.bat
if exist "start-urban-insights.bat" del "start-urban-insights.bat" && set /a count+=1 && echo Deleted: start-urban-insights.bat
if exist "test-available-vehicles.bat" del "test-available-vehicles.bat" && set /a count+=1 && echo Deleted: test-available-vehicles.bat

echo Deleting other test files...
if exist "QUICK_TEST_GUIDE.md" del "QUICK_TEST_GUIDE.md" && set /a count+=1 && echo Deleted: QUICK_TEST_GUIDE.md
if exist "QUICK_TEST_NOW.md" del "QUICK_TEST_NOW.md" && set /a count+=1 && echo Deleted: QUICK_TEST_NOW.md

echo Deleting one-time migration files...
if exist "database-ensure-speed-column.sql" del "database-ensure-speed-column.sql" && set /a count+=1 && echo Deleted: database-ensure-speed-column.sql
if exist "database-migration-speed.sql" del "database-migration-speed.sql" && set /a count+=1 && echo Deleted: database-migration-speed.sql

echo.
echo ============================================
echo Cleanup Complete!
echo Files deleted: %count%
echo ============================================
echo.
echo Remaining essential files:
echo - README.md
echo - SETUP.md
echo - QUICK_START.md
echo - FINAL_RESOLUTION_SUMMARY.md
echo - BOOKING_APPROVAL_WORKFLOW_GUIDE.md
echo - QUICK_REFERENCE_BOOKING_WORKFLOW.md
echo - TROUBLESHOOTING_BOOKING_WORKFLOW.md
echo - SMART_BOOKING_MANAGER_APPROVAL_FIX.md
echo - database files (init, gps, preferences, route)
echo - start-backend.bat
echo - start-frontend.bat
echo - test-booking-workflow.bat
echo.
pause
