package com.neurofleetx.service;

import com.neurofleetx.dto.RouteOptimizationRequest;
import com.neurofleetx.dto.RouteOptimizationResponse;
import com.neurofleetx.model.Route;
import com.neurofleetx.model.Vehicle;
import com.neurofleetx.repository.RouteRepository;
import com.neurofleetx.repository.VehicleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RouteService {
    
    @Autowired
    private RouteRepository routeRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private RouteOptimizationEngine optimizationEngine;
    
    @Autowired
    private ETAPredictorService etaPredictor;
    
    public RouteOptimizationResponse optimizeRoute(RouteOptimizationRequest request) {
        log.info("Optimizing route from {} to {}", request.getStartLocation(), request.getEndLocation());
        
        List<Route> optimizedRoutes = optimizationEngine.generateOptimizedRoutes(
            request.getStartLocation(),
            request.getEndLocation(),
            request.getStartLatitude(),
            request.getStartLongitude(),
            request.getEndLatitude(),
            request.getEndLongitude(),
            request.getVehicleId()
        );
        
        for (Route route : optimizedRoutes) {
            if (request.getIncludeTrafficData() != null && request.getIncludeTrafficData()) {
                Route.TrafficLevel predictedTraffic = etaPredictor.predictTrafficLevel(
                    route.getDistanceKm(), 
                    request.getStartLocation(), 
                    request.getEndLocation()
                );
                route.setTrafficLevel(predictedTraffic);
                
                Vehicle vehicle = null;
                if (request.getVehicleId() != null) {
                    vehicle = vehicleRepository.findById(request.getVehicleId()).orElse(null);
                }
                boolean isElectric = vehicle != null && vehicle.getIsElectric();
                
                int refinedETA = etaPredictor.predictETA(
                    route.getDistanceKm(), 
                    predictedTraffic, 
                    isElectric
                );
                route.setEtaMinutes(refinedETA);
            }
            
            routeRepository.save(route);
        }
        
        Route primaryRoute = optimizedRoutes.stream()
            .filter(r -> r.getOptimizationType() == Route.OptimizationType.BALANCED)
            .findFirst()
            .orElse(optimizedRoutes.get(0));
        
        List<Route> alternatives = optimizedRoutes.stream()
            .filter(r -> !r.equals(primaryRoute))
            .toList();
        
        RouteOptimizationResponse response = new RouteOptimizationResponse();
        response.setPrimaryRoute(primaryRoute);
        response.setAlternativeRoutes(alternatives);
        response.setOptimizationAlgorithm("Dijkstra + ML ETA Predictor");
        response.setTotalRoutesAnalyzed(optimizedRoutes.size());
        
        double avgTime = optimizedRoutes.stream()
            .mapToInt(Route::getEtaMinutes)
            .average()
            .orElse(0);
        response.setTimeSavedMinutes(avgTime - primaryRoute.getEtaMinutes());
        
        double avgEnergy = optimizedRoutes.stream()
            .mapToDouble(Route::getEnergyCost)
            .average()
            .orElse(0);
        response.setEnergySavedPercent(
            ((avgEnergy - primaryRoute.getEnergyCost()) / avgEnergy) * 100
        );
        
        return response;
    }
    
    public List<Route> getAllRoutes() {
        return routeRepository.findAllOrderByTimestampDesc();
    }
    
    public Route getRouteById(Long id) {
        return routeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));
    }
    
    public List<Route> getRoutesByVehicle(Long vehicleId) {
        return routeRepository.findByVehicleId(vehicleId);
    }
    
    public List<Route> getActiveRoutes() {
        return routeRepository.findActiveRoutesByPriority();
    }
    
    public Route updateRouteStatus(Long routeId, Route.RouteStatus status) {
        Route route = getRouteById(routeId);
        route.setStatus(status);
        if (status == Route.RouteStatus.COMPLETED) {
            route.setCompletedAt(LocalDateTime.now());
        }
        return routeRepository.save(route);
    }
    
    public void deleteRoute(Long id) {
        routeRepository.deleteById(id);
    }
}
