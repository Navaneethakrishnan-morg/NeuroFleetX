package com.neurofleetx.service;

import com.neurofleetx.dto.*;
import com.neurofleetx.model.*;
import com.neurofleetx.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private TripRepository tripRepository;
    
    @Autowired
    private MaintenanceRepository maintenanceRepository;
    
    @Autowired
    private UserRepository userRepository;

    public AnalyticsKPIResponse getKPIMetrics() {
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<Booking> allBookings = bookingRepository.findAll();
        List<Trip> allTrips = tripRepository.findAll();
        List<Maintenance> allMaintenance = maintenanceRepository.findAll();
        List<User> allUsers = userRepository.findAll();

        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime todayEnd = LocalDate.now().atTime(LocalTime.MAX);

        int totalFleet = allVehicles.size();
        int availableVehicles = (int) allVehicles.stream()
                .filter(v -> v.getStatus() == Vehicle.VehicleStatus.AVAILABLE)
                .count();
        int inUseVehicles = (int) allVehicles.stream()
                .filter(v -> v.getStatus() == Vehicle.VehicleStatus.IN_USE)
                .count();
        int maintenanceVehicles = (int) allVehicles.stream()
                .filter(v -> v.getStatus() == Vehicle.VehicleStatus.MAINTENANCE)
                .count();

        int tripsToday = (int) allTrips.stream()
                .filter(t -> t.getStartTime() != null && 
                           t.getStartTime().isAfter(todayStart) && 
                           t.getStartTime().isBefore(todayEnd))
                .count();

        int activeRoutes = (int) allTrips.stream()
                .filter(t -> t.getStatus() == Trip.TripStatus.IN_PROGRESS)
                .count();

        double totalRevenue = allBookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
                .mapToDouble(b -> b.getTotalPrice() != null ? b.getTotalPrice() : 0.0)
                .sum();

        double revenueToday = allBookings.stream()
                .filter(b -> b.getCreatedAt().isAfter(todayStart) && 
                           b.getCreatedAt().isBefore(todayEnd))
                .mapToDouble(b -> b.getTotalPrice() != null ? b.getTotalPrice() : 0.0)
                .sum();

        double averageUtilization = totalFleet > 0 
            ? (double) inUseVehicles / totalFleet * 100 
            : 0.0;

        int pendingMaintenance = (int) allMaintenance.stream()
                .filter(m -> m.getStatus() == Maintenance.MaintenanceStatus.PENDING)
                .count();

        int totalCustomers = (int) allUsers.stream()
                .filter(u -> u.getRole() == User.UserRole.CUSTOMER)
                .count();

        int activeCustomers = (int) allBookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.IN_PROGRESS)
                .map(Booking::getCustomer)
                .distinct()
                .count();

        return new AnalyticsKPIResponse(
            totalFleet,
            availableVehicles,
            inUseVehicles,
            maintenanceVehicles,
            tripsToday,
            activeRoutes,
            Math.round(totalRevenue * 100.0) / 100.0,
            Math.round(revenueToday * 100.0) / 100.0,
            Math.round(averageUtilization * 10.0) / 10.0,
            pendingMaintenance,
            totalCustomers,
            activeCustomers
        );
    }

    public FleetDistributionData getFleetDistribution() {
        List<Vehicle> allVehicles = vehicleRepository.findAll();
        List<Trip> allTrips = tripRepository.findAll();

        List<HeatmapDataPoint> vehicleLocations = allVehicles.stream()
                .filter(v -> v.getLatitude() != null && v.getLongitude() != null)
                .map(v -> new HeatmapDataPoint(
                    v.getLatitude(),
                    v.getLongitude(),
                    calculateVehicleIntensity(v),
                    "vehicle",
                    v.getVehicleNumber(),
                    getLocationName(v.getLatitude(), v.getLongitude())
                ))
                .collect(Collectors.toList());

        List<HeatmapDataPoint> tripDensity = calculateTripDensity(allTrips);

        Map<String, Integer> typeDistribution = allVehicles.stream()
                .collect(Collectors.groupingBy(
                    v -> v.getType().name(),
                    Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));

        Map<String, Integer> statusDistribution = allVehicles.stream()
                .collect(Collectors.groupingBy(
                    v -> v.getStatus().name(),
                    Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));

        return new FleetDistributionData(
            vehicleLocations,
            tripDensity,
            typeDistribution,
            statusDistribution
        );
    }

    public HourlyActivityData getHourlyActivity() {
        List<Booking> allBookings = bookingRepository.findAll();

        List<Integer> hours = new ArrayList<>();
        List<Integer> bookingCounts = new ArrayList<>();
        List<Double> revenues = new ArrayList<>();
        List<Integer> vehicleUsage = new ArrayList<>();

        for (int hour = 0; hour < 24; hour++) {
            hours.add(hour);
            
            final int currentHour = hour;
            int bookingsInHour = (int) allBookings.stream()
                    .filter(b -> b.getStartTime() != null && 
                               b.getStartTime().getHour() == currentHour)
                    .count();
            bookingCounts.add(bookingsInHour);

            double revenueInHour = allBookings.stream()
                    .filter(b -> b.getStartTime() != null && 
                               b.getStartTime().getHour() == currentHour)
                    .mapToDouble(b -> b.getTotalPrice() != null ? b.getTotalPrice() : 0.0)
                    .sum();
            revenues.add(Math.round(revenueInHour * 100.0) / 100.0);

            Set<Long> uniqueVehicles = allBookings.stream()
                    .filter(b -> b.getStartTime() != null && 
                               b.getStartTime().getHour() == currentHour)
                    .map(b -> b.getVehicle().getId())
                    .collect(Collectors.toSet());
            vehicleUsage.add(uniqueVehicles.size());
        }

        return new HourlyActivityData(hours, bookingCounts, revenues, vehicleUsage);
    }

    private int calculateVehicleIntensity(Vehicle vehicle) {
        if (vehicle.getStatus() == Vehicle.VehicleStatus.IN_USE) {
            return 100;
        } else if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            return 60;
        } else if (vehicle.getStatus() == Vehicle.VehicleStatus.MAINTENANCE) {
            return 30;
        }
        return 10;
    }

    private List<HeatmapDataPoint> calculateTripDensity(List<Trip> trips) {
        Map<String, Integer> locationCounts = new HashMap<>();
        Map<String, Double[]> locationCoords = new HashMap<>();

        for (Trip trip : trips) {
            if (trip.getStartLocation() != null) {
                String location = trip.getStartLocation();
                locationCounts.put(location, locationCounts.getOrDefault(location, 0) + 1);
                
                if (!locationCoords.containsKey(location)) {
                    Double[] coords = generateCoordinatesForLocation(location);
                    locationCoords.put(location, coords);
                }
            }
        }

        return locationCounts.entrySet().stream()
                .map(entry -> {
                    Double[] coords = locationCoords.get(entry.getKey());
                    return new HeatmapDataPoint(
                        coords[0],
                        coords[1],
                        Math.min(entry.getValue() * 10, 100),
                        "trip",
                        null,
                        entry.getKey()
                    );
                })
                .collect(Collectors.toList());
    }

    private Double[] generateCoordinatesForLocation(String location) {
        int hash = location.hashCode();
        double baseLat = 40.7128;
        double baseLng = -74.0060;
        
        double latOffset = ((hash % 1000) / 10000.0) - 0.05;
        double lngOffset = (((hash / 1000) % 1000) / 10000.0) - 0.05;
        
        return new Double[]{baseLat + latOffset, baseLng + lngOffset};
    }

    private String getLocationName(Double lat, Double lng) {
        if (lat == null || lng == null) return "Unknown";
        
        String[] areas = {
            "Downtown", "Midtown", "Uptown", "East Side", "West Side",
            "Financial District", "Tech District", "Shopping District"
        };
        
        int index = (int) ((lat + lng) * 1000) % areas.length;
        return areas[Math.abs(index)];
    }

    public Map<String, Object> getDailyTrends(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        List<Booking> bookings = bookingRepository.findAll().stream()
                .filter(b -> b.getCreatedAt().isAfter(startDate))
                .collect(Collectors.toList());

        Map<String, Object> trends = new HashMap<>();
        List<String> dates = new ArrayList<>();
        List<Integer> dailyBookings = new ArrayList<>();
        List<Double> dailyRevenue = new ArrayList<>();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            dates.add(date.toString());

            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

            long bookingCount = bookings.stream()
                    .filter(b -> b.getCreatedAt().isAfter(dayStart) && 
                               b.getCreatedAt().isBefore(dayEnd))
                    .count();
            dailyBookings.add((int) bookingCount);

            double revenue = bookings.stream()
                    .filter(b -> b.getCreatedAt().isAfter(dayStart) && 
                               b.getCreatedAt().isBefore(dayEnd))
                    .mapToDouble(b -> b.getTotalPrice() != null ? b.getTotalPrice() : 0.0)
                    .sum();
            dailyRevenue.add(Math.round(revenue * 100.0) / 100.0);
        }

        trends.put("dates", dates);
        trends.put("bookings", dailyBookings);
        trends.put("revenue", dailyRevenue);

        return trends;
    }

    public Map<String, Object> getVehiclePerformance() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Booking> bookings = bookingRepository.findAll();

        List<Map<String, Object>> performanceData = vehicles.stream()
                .map(vehicle -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("vehicleId", vehicle.getVehicleNumber());
                    data.put("model", vehicle.getManufacturer() + " " + vehicle.getModel());
                    data.put("type", vehicle.getType().name());
                    data.put("status", vehicle.getStatus().name());

                    List<Booking> vehicleBookings = bookings.stream()
                            .filter(b -> b.getVehicle().getId().equals(vehicle.getId()))
                            .collect(Collectors.toList());

                    data.put("totalTrips", vehicleBookings.size());
                    
                    double totalRevenue = vehicleBookings.stream()
                            .mapToDouble(b -> b.getTotalPrice() != null ? b.getTotalPrice() : 0.0)
                            .sum();
                    data.put("totalRevenue", Math.round(totalRevenue * 100.0) / 100.0);

                    data.put("healthScore", vehicle.getHealthScore() != null ? vehicle.getHealthScore() : 100);
                    data.put("utilization", calculateUtilizationRate(vehicleBookings));

                    return data;
                })
                .sorted((a, b) -> Double.compare(
                    (Double) b.get("totalRevenue"), 
                    (Double) a.get("totalRevenue")
                ))
                .limit(10)
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("topPerformers", performanceData);
        return result;
    }

    private double calculateUtilizationRate(List<Booking> bookings) {
        long completedBookings = bookings.stream()
                .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
                .count();
        
        if (bookings.isEmpty()) return 0.0;
        
        return Math.round((double) completedBookings / bookings.size() * 1000.0) / 10.0;
    }
}
