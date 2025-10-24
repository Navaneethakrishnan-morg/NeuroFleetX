package com.neurofleetx.controller;

import com.neurofleetx.model.Vehicle;
import com.neurofleetx.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/admin/vehicles")
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/manager/vehicles")
    public ResponseEntity<List<Vehicle>> getVehiclesForManager() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/customer/vehicles")
    public ResponseEntity<List<Vehicle>> getAvailableVehiclesForCustomer() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @GetMapping("/vehicles/status/{status}")
    public ResponseEntity<List<Vehicle>> getVehiclesByStatus(@PathVariable Vehicle.VehicleStatus status) {
        return ResponseEntity.ok(vehicleService.getVehiclesByStatus(status));
    }

    @GetMapping("/vehicles/type/{type}")
    public ResponseEntity<List<Vehicle>> getVehiclesByType(@PathVariable Vehicle.VehicleType type) {
        return ResponseEntity.ok(vehicleService.getVehiclesByType(type));
    }

    @PostMapping("/admin/vehicles")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.createVehicle(vehicle));
    }

    @PutMapping("/admin/vehicles/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) {
        return ResponseEntity.ok(vehicleService.updateVehicle(id, vehicle));
    }

    @DeleteMapping("/admin/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/vehicles/{id}/telemetry")
    public ResponseEntity<Vehicle> updateVehicleTelemetry(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.updateVehicleTelemetry(id));
    }
}