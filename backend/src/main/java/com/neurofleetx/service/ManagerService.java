package com.neurofleetx.service;

import com.neurofleetx.dto.DriverPerformanceDTO;
import com.neurofleetx.dto.MaintenanceDueDTO;
import com.neurofleetx.model.*;
import com.neurofleetx.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManagerService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    public List<DriverPerformanceDTO> getDriverPerformanceMetrics() {
        List<User> drivers = userRepository.findByRole(User.UserRole.DRIVER);
        List<DriverPerformanceDTO> performanceList = new ArrayList<>();

        for (User driver : drivers) {
            DriverPerformanceDTO performance = new DriverPerformanceDTO(
                    driver.getId(),
                    driver.getFullName(),
                    driver.getEmail(),
                    150 + (int)(Math.random() * 50),
                    4.5 + Math.random() * 0.5,
                    10000 + Math.random() * 5000,
                    85 + (int)(Math.random() * 15),
                    (int)(Math.random() * 5),
                    140 + (int)(Math.random() * 40),
                    (int)(Math.random() * 10),
                    40 + Math.random() * 20,
                    1000 + Math.random() * 500,
                    150 + (int)(Math.random() * 50),
                    driver.getActive() ? "ACTIVE" : "INACTIVE"
            );
            performanceList.add(performance);
        }

        return performanceList;
    }

    public List<MaintenanceDueDTO> getMaintenanceDueVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<MaintenanceDueDTO> maintenanceDueList = new ArrayList<>();

        for (Vehicle vehicle : vehicles) {
            LocalDateTime lastMaintenanceDate = vehicle.getLastMaintenanceDate();
            if (lastMaintenanceDate == null) {
                lastMaintenanceDate = LocalDateTime.now().minusMonths(3);
            }

            long daysSinceLast = ChronoUnit.DAYS.between(lastMaintenanceDate, LocalDateTime.now());
            String maintenanceType = "INSPECTION";
            String priority = "LOW";
            String status = "UPCOMING";
            LocalDateTime dueDate = lastMaintenanceDate.plusMonths(3);
            String notes = "";

            if (vehicle.getMileage() != null && vehicle.getMileage() > 5000) {
                maintenanceType = "OIL_CHANGE";
                priority = "HIGH";
                status = "DUE";
                notes = "High mileage - oil change required";
            } else if (daysSinceLast > 90) {
                maintenanceType = "INSPECTION";
                priority = "MEDIUM";
                status = "DUE";
                notes = "Regular inspection due";
            }

            if (daysSinceLast > 60) {
                MaintenanceDueDTO dto = new MaintenanceDueDTO(
                        vehicle.getId(),
                        vehicle.getVehicleNumber(),
                        vehicle.getManufacturer(),
                        vehicle.getModel(),
                        maintenanceType,
                        priority,
                        dueDate,
                        vehicle.getMileage(),
                        (int) daysSinceLast,
                        vehicle.getFuelLevel(),
                        vehicle.getBatteryLevel(),
                        status,
                        notes
                );
                maintenanceDueList.add(dto);
            }
        }

        return maintenanceDueList;
    }
}
