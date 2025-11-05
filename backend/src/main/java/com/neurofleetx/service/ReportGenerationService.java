package com.neurofleetx.service;

import com.neurofleetx.model.Booking;
import com.neurofleetx.model.Vehicle;
import com.neurofleetx.model.Trip;
import com.neurofleetx.repository.BookingRepository;
import com.neurofleetx.repository.VehicleRepository;
import com.neurofleetx.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportGenerationService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripRepository tripRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public byte[] generateFleetReportCSV() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);

            writer.println("Fleet Report - Generated: " + LocalDateTime.now().format(DATE_FORMATTER));
            writer.println();
            writer.println("Vehicle Number,Manufacturer,Model,Type,Capacity,Electric,Status,Health Score,Mileage,Battery Level,Fuel Level");

            List<Vehicle> vehicles = vehicleRepository.findAll();
            for (Vehicle vehicle : vehicles) {
                writer.printf("%s,%s,%s,%s,%d,%s,%s,%d,%d,%s,%s%n",
                    escapeCsv(vehicle.getVehicleNumber()),
                    escapeCsv(vehicle.getManufacturer()),
                    escapeCsv(vehicle.getModel()),
                    vehicle.getType().name(),
                    vehicle.getCapacity(),
                    vehicle.getIsElectric() ? "Yes" : "No",
                    vehicle.getStatus().name(),
                    vehicle.getHealthScore() != null ? vehicle.getHealthScore() : 0,
                    vehicle.getMileage() != null ? vehicle.getMileage() : 0,
                    vehicle.getBatteryLevel() != null ? vehicle.getBatteryLevel() : "N/A",
                    vehicle.getFuelLevel() != null ? vehicle.getFuelLevel() : "N/A"
                );
            }

            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating fleet report CSV", e);
        }
    }

    public byte[] generateBookingsReportCSV() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);

            writer.println("Bookings Report - Generated: " + LocalDateTime.now().format(DATE_FORMATTER));
            writer.println();
            writer.println("Booking ID,Customer,Vehicle,Start Time,End Time,Pickup Location,Dropoff Location,Status,Total Price");

            List<Booking> bookings = bookingRepository.findAll();
            for (Booking booking : bookings) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s,%.2f%n",
                    booking.getId(),
                    escapeCsv(booking.getCustomer().getFullName()),
                    escapeCsv(booking.getVehicle().getVehicleNumber()),
                    booking.getStartTime().format(DATE_FORMATTER),
                    booking.getEndTime().format(DATE_FORMATTER),
                    escapeCsv(booking.getPickupLocation()),
                    escapeCsv(booking.getDropoffLocation()),
                    booking.getStatus().name(),
                    booking.getTotalPrice() != null ? booking.getTotalPrice() : 0.0
                );
            }

            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating bookings report CSV", e);
        }
    }

    public byte[] generateRevenueReportCSV() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);

            writer.println("Revenue Report - Generated: " + LocalDateTime.now().format(DATE_FORMATTER));
            writer.println();

            List<Booking> bookings = bookingRepository.findAll().stream()
                    .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
                    .collect(Collectors.toList());

            double totalRevenue = bookings.stream()
                    .mapToDouble(b -> b.getTotalPrice() != null ? b.getTotalPrice() : 0.0)
                    .sum();

            writer.printf("Total Completed Bookings: %d%n", bookings.size());
            writer.printf("Total Revenue: $%.2f%n", totalRevenue);
            writer.println();
            writer.println("Date,Booking ID,Customer,Vehicle,Amount");

            for (Booking booking : bookings) {
                writer.printf("%s,%d,%s,%s,%.2f%n",
                    booking.getCreatedAt().format(DATE_FORMATTER),
                    booking.getId(),
                    escapeCsv(booking.getCustomer().getFullName()),
                    escapeCsv(booking.getVehicle().getVehicleNumber()),
                    booking.getTotalPrice() != null ? booking.getTotalPrice() : 0.0
                );
            }

            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating revenue report CSV", e);
        }
    }

    public byte[] generateTripsReportCSV() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);

            writer.println("Trips Report - Generated: " + LocalDateTime.now().format(DATE_FORMATTER));
            writer.println();
            writer.println("Trip ID,Vehicle,Driver,Start Time,End Time,Start Location,End Location,Distance,Duration,Status");

            List<Trip> trips = tripRepository.findAll();
            for (Trip trip : trips) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%.2f,%d,%s%n",
                    trip.getId(),
                    trip.getVehicle() != null ? escapeCsv(trip.getVehicle().getVehicleNumber()) : "N/A",
                    trip.getDriver() != null ? escapeCsv(trip.getDriver().getFullName()) : "N/A",
                    trip.getStartTime() != null ? trip.getStartTime().format(DATE_FORMATTER) : "N/A",
                    trip.getEndTime() != null ? trip.getEndTime().format(DATE_FORMATTER) : "N/A",
                    escapeCsv(trip.getStartLocation()),
                    escapeCsv(trip.getEndLocation()),
                    trip.getDistance() != null ? trip.getDistance() : 0.0,
                    trip.getDuration() != null ? trip.getDuration() : 0,
                    trip.getStatus().name()
                );
            }

            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating trips report CSV", e);
        }
    }

    public byte[] generateAnalyticsSummaryCSV() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);

            writer.println("Analytics Summary Report - Generated: " + LocalDateTime.now().format(DATE_FORMATTER));
            writer.println();

            List<Vehicle> vehicles = vehicleRepository.findAll();
            List<Booking> bookings = bookingRepository.findAll();
            List<Trip> trips = tripRepository.findAll();

            writer.println("=== Fleet Statistics ===");
            writer.printf("Total Fleet: %d%n", vehicles.size());
            writer.printf("Available: %d%n", vehicles.stream().filter(v -> v.getStatus() == Vehicle.VehicleStatus.AVAILABLE).count());
            writer.printf("In Use: %d%n", vehicles.stream().filter(v -> v.getStatus() == Vehicle.VehicleStatus.IN_USE).count());
            writer.printf("Maintenance: %d%n", vehicles.stream().filter(v -> v.getStatus() == Vehicle.VehicleStatus.MAINTENANCE).count());
            writer.println();

            writer.println("=== Booking Statistics ===");
            writer.printf("Total Bookings: %d%n", bookings.size());
            writer.printf("Completed: %d%n", bookings.stream().filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED).count());
            writer.printf("In Progress: %d%n", bookings.stream().filter(b -> b.getStatus() == Booking.BookingStatus.IN_PROGRESS).count());
            writer.printf("Pending: %d%n", bookings.stream().filter(b -> b.getStatus() == Booking.BookingStatus.PENDING).count());
            writer.println();

            writer.println("=== Revenue Statistics ===");
            double totalRevenue = bookings.stream()
                    .filter(b -> b.getStatus() == Booking.BookingStatus.COMPLETED)
                    .mapToDouble(b -> b.getTotalPrice() != null ? b.getTotalPrice() : 0.0)
                    .sum();
            writer.printf("Total Revenue: $%.2f%n", totalRevenue);
            writer.printf("Average Booking Value: $%.2f%n", 
                bookings.isEmpty() ? 0.0 : totalRevenue / bookings.size());
            writer.println();

            writer.println("=== Trip Statistics ===");
            writer.printf("Total Trips: %d%n", trips.size());
            writer.printf("Completed: %d%n", trips.stream().filter(t -> t.getStatus() == Trip.TripStatus.COMPLETED).count());
            writer.printf("In Progress: %d%n", trips.stream().filter(t -> t.getStatus() == Trip.TripStatus.IN_PROGRESS).count());
            double totalDistance = trips.stream()
                    .mapToDouble(t -> t.getDistance() != null ? t.getDistance() : 0.0)
                    .sum();
            writer.printf("Total Distance: %.2f miles%n", totalDistance);

            writer.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating analytics summary CSV", e);
        }
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
