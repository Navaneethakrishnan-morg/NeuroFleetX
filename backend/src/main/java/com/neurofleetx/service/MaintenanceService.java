package com.neurofleetx.service;

import com.neurofleetx.model.Maintenance;
import com.neurofleetx.model.Vehicle;
import com.neurofleetx.repository.MaintenanceRepository;
import com.neurofleetx.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaintenanceService {
    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Maintenance> getAllMaintenance() {
        return maintenanceRepository.findAll();
    }

    public Maintenance getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance not found"));
    }

    public List<Maintenance> getMaintenanceByVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        return maintenanceRepository.findByVehicle(vehicle);
    }

    public List<Maintenance> getPredictiveMaintenance() {
        return maintenanceRepository.findByIsPredictive(true);
    }

    public Maintenance createMaintenance(Maintenance maintenance) {
        maintenance.setCreatedAt(LocalDateTime.now());
        
        if (maintenance.getVehicle().getHealthScore() < 70) {
            maintenance.setIsPredictive(true);
            maintenance.setPriority(Maintenance.Priority.HIGH);
        }
        
        return maintenanceRepository.save(maintenance);
    }

    public Maintenance updateMaintenance(Long id, Maintenance maintenanceDetails) {
        Maintenance maintenance = getMaintenanceById(id);
        maintenance.setStatus(maintenanceDetails.getStatus());
        maintenance.setPriority(maintenanceDetails.getPriority());
        maintenance.setScheduledDate(maintenanceDetails.getScheduledDate());
        maintenance.setCompletedDate(maintenanceDetails.getCompletedDate());
        maintenance.setCost(maintenanceDetails.getCost());
        maintenance.setUpdatedAt(LocalDateTime.now());
        
        if (maintenance.getStatus() == Maintenance.MaintenanceStatus.COMPLETED) {
            Vehicle vehicle = maintenance.getVehicle();
            vehicle.setHealthScore(100);
            vehicle.setLastMaintenanceDate(LocalDateTime.now());
            vehicleRepository.save(vehicle);
        }
        
        return maintenanceRepository.save(maintenance);
    }
}
