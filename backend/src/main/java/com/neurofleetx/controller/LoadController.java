package com.neurofleetx.controller;

import com.neurofleetx.dto.VehicleAssignmentRequest;
import com.neurofleetx.model.Load;
import com.neurofleetx.service.LoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class LoadController {
    
    @Autowired
    private LoadService loadService;
    
    @PostMapping("/loads")
    public ResponseEntity<Load> createLoad(@RequestBody Load load) {
        return ResponseEntity.ok(loadService.createLoad(load));
    }
    
    @GetMapping("/loads")
    public ResponseEntity<List<Load>> getAllLoads() {
        return ResponseEntity.ok(loadService.getAllLoads());
    }
    
    @GetMapping("/loads/{id}")
    public ResponseEntity<Load> getLoadById(@PathVariable Long id) {
        return ResponseEntity.ok(loadService.getLoadById(id));
    }
    
    @GetMapping("/loads/pending")
    public ResponseEntity<List<Load>> getPendingLoads() {
        return ResponseEntity.ok(loadService.getPendingLoads());
    }
    
    @PostMapping("/loads/assign")
    public ResponseEntity<Load> assignVehicleToLoad(
            @RequestBody VehicleAssignmentRequest request) {
        return ResponseEntity.ok(loadService.assignVehicleToLoad(request));
    }
    
    @PostMapping("/loads/auto-assign")
    public ResponseEntity<List<Load>> autoAssignLoads() {
        return ResponseEntity.ok(loadService.autoAssignPendingLoads());
    }
    
    @PutMapping("/loads/{id}/status")
    public ResponseEntity<Load> updateLoadStatus(
            @PathVariable Long id,
            @RequestParam Load.LoadStatus status) {
        return ResponseEntity.ok(loadService.updateLoadStatus(id, status));
    }
    
    @DeleteMapping("/loads/{id}")
    public ResponseEntity<Void> deleteLoad(@PathVariable Long id) {
        loadService.deleteLoad(id);
        return ResponseEntity.ok().build();
    }
}
