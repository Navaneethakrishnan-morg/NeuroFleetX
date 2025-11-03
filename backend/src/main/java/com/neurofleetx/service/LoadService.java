package com.neurofleetx.service;

import com.neurofleetx.dto.VehicleAssignmentRequest;
import com.neurofleetx.model.Load;
import com.neurofleetx.model.Vehicle;
import com.neurofleetx.repository.LoadRepository;
import com.neurofleetx.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class LoadService {
    
    @Autowired
    private LoadRepository loadRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private RouteOptimizationEngine optimizationEngine;
    
    public Load createLoad(Load load) {
        load.setCreatedAt(LocalDateTime.now());
        load.setStatus(Load.LoadStatus.PENDING);
        return loadRepository.save(load);
    }
    
    public List<Load> getAllLoads() {
        return loadRepository.findAll();
    }
    
    public Load getLoadById(Long id) {
        return loadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Load not found with id: " + id));
    }
    
    public List<Load> getPendingLoads() {
        return loadRepository.findPendingLoadsByPriority();
    }
    
    public Load assignVehicleToLoad(VehicleAssignmentRequest request) {
        Load load = getLoadById(request.getLoadId());
        
        Vehicle bestVehicle = findBestVehicle(
            load.getPickupLatitude(),
            load.getPickupLongitude(),
            load.getWeight()
        );
        
        if (bestVehicle == null) {
            throw new RuntimeException("No suitable vehicle available for this load");
        }
        
        load.setVehicleId(bestVehicle.getId());
        load.setStatus(Load.LoadStatus.ASSIGNED);
        load.setAssignedAt(LocalDateTime.now());
        
        bestVehicle.setStatus(Vehicle.VehicleStatus.IN_USE);
        vehicleRepository.save(bestVehicle);
        
        log.info("Assigned vehicle {} to load {}", bestVehicle.getVehicleNumber(), load.getLoadId());
        
        return loadRepository.save(load);
    }
    
    public Vehicle findBestVehicle(Double pickupLat, Double pickupLon, Double weight) {
        List<Vehicle> availableVehicles = vehicleRepository.findByStatus(Vehicle.VehicleStatus.AVAILABLE);
        
        if (availableVehicles.isEmpty()) {
            return null;
        }
        
        return availableVehicles.stream()
            .filter(v -> v.getCapacity() >= weight)
            .filter(v -> v.getLatitude() != null && v.getLongitude() != null)
            .filter(v -> {
                if (v.getIsElectric()) {
                    return v.getBatteryLevel() != null && v.getBatteryLevel() > 30;
                } else {
                    return v.getFuelLevel() != null && v.getFuelLevel() > 20;
                }
            })
            .min(Comparator.comparingDouble(v -> {
                double distance = optimizationEngine.calculateHaversineDistance(
                    v.getLatitude(), v.getLongitude(),
                    pickupLat, pickupLon
                );
                
                double capacityFactor = (double) v.getCapacity() / 1000.0;
                double energyFactor = v.getIsElectric() ? 0.8 : 1.0;
                
                return distance * capacityFactor * energyFactor;
            }))
            .orElse(null);
    }
    
    public Load updateLoadStatus(Long loadId, Load.LoadStatus status) {
        Load load = getLoadById(loadId);
        load.setStatus(status);
        
        if (status == Load.LoadStatus.DELIVERED) {
            load.setDeliveredAt(LocalDateTime.now());
            
            if (load.getVehicleId() != null) {
                Vehicle vehicle = vehicleRepository.findById(load.getVehicleId()).orElse(null);
                if (vehicle != null) {
                    vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
                    vehicleRepository.save(vehicle);
                }
            }
        }
        
        return loadRepository.save(load);
    }
    
    public List<Load> autoAssignPendingLoads() {
        List<Load> pendingLoads = loadRepository.findUnassignedLoads();
        
        for (Load load : pendingLoads) {
            try {
                Vehicle vehicle = findBestVehicle(
                    load.getPickupLatitude(),
                    load.getPickupLongitude(),
                    load.getWeight()
                );
                
                if (vehicle != null) {
                    load.setVehicleId(vehicle.getId());
                    load.setStatus(Load.LoadStatus.ASSIGNED);
                    load.setAssignedAt(LocalDateTime.now());
                    
                    vehicle.setStatus(Vehicle.VehicleStatus.IN_USE);
                    vehicleRepository.save(vehicle);
                    loadRepository.save(load);
                    
                    log.info("Auto-assigned vehicle {} to load {}", 
                             vehicle.getVehicleNumber(), load.getLoadId());
                }
            } catch (Exception e) {
                log.error("Failed to auto-assign load {}: {}", load.getLoadId(), e.getMessage());
            }
        }
        
        return loadRepository.findByStatus(Load.LoadStatus.ASSIGNED);
    }
    
    public void deleteLoad(Long id) {
        loadRepository.deleteById(id);
    }
}
