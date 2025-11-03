package com.neurofleetx.controller;

import com.neurofleetx.dto.RouteOptimizationRequest;
import com.neurofleetx.dto.RouteOptimizationResponse;
import com.neurofleetx.model.Route;
import com.neurofleetx.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class RouteController {
    
    @Autowired
    private RouteService routeService;
    
    @PostMapping("/routes/optimize")
    public ResponseEntity<RouteOptimizationResponse> optimizeRoute(
            @RequestBody RouteOptimizationRequest request) {
        RouteOptimizationResponse response = routeService.optimizeRoute(request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/routes")
    public ResponseEntity<List<Route>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }
    
    @GetMapping("/routes/{id}")
    public ResponseEntity<Route> getRouteById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }
    
    @GetMapping("/routes/vehicle/{vehicleId}")
    public ResponseEntity<List<Route>> getRoutesByVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(routeService.getRoutesByVehicle(vehicleId));
    }
    
    @GetMapping("/routes/active")
    public ResponseEntity<List<Route>> getActiveRoutes() {
        return ResponseEntity.ok(routeService.getActiveRoutes());
    }
    
    @PutMapping("/routes/{id}/status")
    public ResponseEntity<Route> updateRouteStatus(
            @PathVariable Long id,
            @RequestParam Route.RouteStatus status) {
        return ResponseEntity.ok(routeService.updateRouteStatus(id, status));
    }
    
    @DeleteMapping("/routes/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routeService.deleteRoute(id);
        return ResponseEntity.ok().build();
    }
}
