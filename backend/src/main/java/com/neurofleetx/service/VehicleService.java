package com.neurofleetx.service;

import com.neurofleetx.model.Vehicle;
import com.neurofleetx.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    private Random random = new Random();

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public List<Vehicle> getVehiclesByStatus(Vehicle.VehicleStatus status) {
        return vehicleRepository.findByStatus(status);
    }

    public List<Vehicle> getVehiclesByType(Vehicle.VehicleType type) {
        return vehicleRepository.findByType(type);
    }

    public Vehicle createVehicle(Vehicle vehicle) {
        vehicle.setCreatedAt(LocalDateTime.now());
        
        // Initialize GPS coordinates if not provided
        if (vehicle.getLatitude() == null) {
            vehicle.setLatitude(40.7128 + (random.nextDouble() - 0.5) * 0.1);
        }
        if (vehicle.getLongitude() == null) {
            vehicle.setLongitude(-74.0060 + (random.nextDouble() - 0.5) * 0.1);
        }
        
        // Initialize battery/fuel levels if not provided
        if (vehicle.getIsElectric() && vehicle.getBatteryLevel() == null) {
            vehicle.setBatteryLevel(80 + random.nextInt(20));
        } else if (!vehicle.getIsElectric() && vehicle.getFuelLevel() == null) {
            vehicle.setFuelLevel(70 + random.nextInt(30));
        }
        
        // Initialize speed to 0
        if (vehicle.getSpeed() == null) {
            vehicle.setSpeed(0.0);
        }
        
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setModel(vehicleDetails.getModel());
        vehicle.setManufacturer(vehicleDetails.getManufacturer());
        vehicle.setType(vehicleDetails.getType());
        vehicle.setCapacity(vehicleDetails.getCapacity());
        vehicle.setIsElectric(vehicleDetails.getIsElectric());
        vehicle.setStatus(vehicleDetails.getStatus());
        
        // Update GPS coordinates if provided
        if (vehicleDetails.getLatitude() != null) {
            vehicle.setLatitude(vehicleDetails.getLatitude());
        }
        if (vehicleDetails.getLongitude() != null) {
            vehicle.setLongitude(vehicleDetails.getLongitude());
        }
        
        // Initialize GPS coordinates if not set
        if (vehicle.getLatitude() == null) {
            vehicle.setLatitude(40.7128 + (random.nextDouble() - 0.5) * 0.1);
        }
        if (vehicle.getLongitude() == null) {
            vehicle.setLongitude(-74.0060 + (random.nextDouble() - 0.5) * 0.1);
        }
        
        vehicle.setUpdatedAt(LocalDateTime.now());
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    public Vehicle updateVehicleTelemetry(Long id) {
        Vehicle vehicle = getVehicleById(id);
        
        if (vehicle.getLatitude() == null) vehicle.setLatitude(40.7128);
        if (vehicle.getLongitude() == null) vehicle.setLongitude(-74.0060);
        
        if (vehicle.getStatus() == Vehicle.VehicleStatus.IN_USE) {
            vehicle.setLatitude(vehicle.getLatitude() + (random.nextDouble() - 0.5) * 0.01);
            vehicle.setLongitude(vehicle.getLongitude() + (random.nextDouble() - 0.5) * 0.01);
            
            double baseSpeed = switch (vehicle.getType()) {
                case BUS, TRUCK -> 30.0 + random.nextDouble() * 20.0;
                case VAN -> 35.0 + random.nextDouble() * 25.0;
                case SUV -> 40.0 + random.nextDouble() * 30.0;
                case SEDAN -> 35.0 + random.nextDouble() * 35.0;
                case BIKE -> 20.0 + random.nextDouble() * 30.0;
            };
            vehicle.setSpeed(Math.round(baseSpeed * 10.0) / 10.0);
            
            if (vehicle.getIsElectric()) {
                int currentBattery = vehicle.getBatteryLevel() != null ? vehicle.getBatteryLevel() : 85;
                vehicle.setBatteryLevel(Math.max(5, currentBattery - random.nextInt(3)));
            } else {
                int currentFuel = vehicle.getFuelLevel() != null ? vehicle.getFuelLevel() : 75;
                vehicle.setFuelLevel(Math.max(5, currentFuel - random.nextInt(3)));
            }
            
            vehicle.setMileage(vehicle.getMileage() + random.nextInt(2) + 1);
        } else if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
            vehicle.setSpeed(0.0);
            
            if (vehicle.getIsElectric() && vehicle.getBatteryLevel() != null && vehicle.getBatteryLevel() < 95) {
                vehicle.setBatteryLevel(Math.min(100, vehicle.getBatteryLevel() + random.nextInt(2)));
            }
        } else if (vehicle.getStatus() == Vehicle.VehicleStatus.MAINTENANCE) {
            vehicle.setSpeed(0.0);
            int healthScore = vehicle.getHealthScore() != null ? vehicle.getHealthScore() : 70;
            vehicle.setHealthScore(Math.min(100, healthScore + random.nextInt(5)));
        } else {
            vehicle.setSpeed(0.0);
        }
        
        vehicle.setUpdatedAt(LocalDateTime.now());
        return vehicleRepository.save(vehicle);
    }
    
    public List<Vehicle> updateAllVehiclesTelemetry() {
        List<Vehicle> vehicles = getAllVehicles();
        return vehicles.stream()
                .map(v -> updateVehicleTelemetry(v.getId()))
                .toList();
    }
    
    public int initializeAllVehicleGPS() {
        List<Vehicle> vehicles = getAllVehicles();
        int count = 0;
        
        for (Vehicle vehicle : vehicles) {
            boolean updated = false;
            
            // Initialize GPS coordinates if not set
            if (vehicle.getLatitude() == null) {
                vehicle.setLatitude(40.7128 + (random.nextDouble() - 0.5) * 0.1);
                updated = true;
            }
            if (vehicle.getLongitude() == null) {
                vehicle.setLongitude(-74.0060 + (random.nextDouble() - 0.5) * 0.1);
                updated = true;
            }
            
            // Initialize speed if not set
            if (vehicle.getSpeed() == null) {
                vehicle.setSpeed(0.0);
                updated = true;
            }
            
            // Initialize battery/fuel if not set
            if (vehicle.getIsElectric() && vehicle.getBatteryLevel() == null) {
                vehicle.setBatteryLevel(80 + random.nextInt(20));
                updated = true;
            } else if (!vehicle.getIsElectric() && vehicle.getFuelLevel() == null) {
                vehicle.setFuelLevel(70 + random.nextInt(30));
                updated = true;
            }
            
            if (updated) {
                vehicleRepository.save(vehicle);
                count++;
            }
        }
        
        return count;
    }
}
