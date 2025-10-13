package com.neurofleetx.controller;

import com.neurofleetx.model.Maintenance;
import com.neurofleetx.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class MaintenanceController {
    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping("/admin/maintenance")
    public ResponseEntity<List<Maintenance>> getAllMaintenance() {
        return ResponseEntity.ok(maintenanceService.getAllMaintenance());
    }

    @GetMapping("/manager/maintenance")
    public ResponseEntity<List<Maintenance>> getManagerMaintenance() {
        return ResponseEntity.ok(maintenanceService.getAllMaintenance());
    }

    @GetMapping("/maintenance/{id}")
    public ResponseEntity<Maintenance> getMaintenanceById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceById(id));
    }

    @GetMapping("/maintenance/vehicle/{vehicleId}")
    public ResponseEntity<List<Maintenance>> getMaintenanceByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceByVehicle(vehicleId));
    }

    @GetMapping("/maintenance/predictive")
    public ResponseEntity<List<Maintenance>> getPredictiveMaintenance() {
        return ResponseEntity.ok(maintenanceService.getPredictiveMaintenance());
    }

    @PostMapping("/admin/maintenance")
    public ResponseEntity<Maintenance> createMaintenance(@RequestBody Maintenance maintenance) {
        return ResponseEntity.ok(maintenanceService.createMaintenance(maintenance));
    }

    @PutMapping("/admin/maintenance/{id}")
    public ResponseEntity<Maintenance> updateMaintenance(@PathVariable Long id, @RequestBody Maintenance maintenance) {
        return ResponseEntity.ok(maintenanceService.updateMaintenance(id, maintenance));
    }
}
