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
        vehicle.setUpdatedAt(LocalDateTime.now());
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    public Vehicle updateVehicleTelemetry(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setLatitude(vehicle.getLatitude() != null ? vehicle.getLatitude() + (random.nextDouble() - 0.5) * 0.01 : 40.7128);
        vehicle.setLongitude(vehicle.getLongitude() != null ? vehicle.getLongitude() + (random.nextDouble() - 0.5) * 0.01 : -74.0060);
        
        if (vehicle.getIsElectric()) {
            vehicle.setBatteryLevel(Math.max(0, Math.min(100, vehicle.getBatteryLevel() != null ? vehicle.getBatteryLevel() - random.nextInt(5) : 80)));
        } else {
            vehicle.setFuelLevel(Math.max(0, Math.min(100, vehicle.getFuelLevel() != null ? vehicle.getFuelLevel() - random.nextInt(5) : 75)));
        }
        
        vehicle.setMileage(vehicle.getMileage() + random.nextInt(10));
        vehicle.setHealthScore(Math.max(60, Math.min(100, vehicle.getHealthScore() - random.nextInt(3))));
        vehicle.setUpdatedAt(LocalDateTime.now());
        
        return vehicleRepository.save(vehicle);
    }
}
