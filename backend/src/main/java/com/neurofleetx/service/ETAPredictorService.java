package com.neurofleetx.service;

import com.neurofleetx.model.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@Slf4j
public class ETAPredictorService {
    
    private final Random random = new Random();
    
    public int predictETA(double distance, Route.TrafficLevel trafficLevel, boolean isElectric) {
        double baseSpeed = 45.0;
        
        double trafficMultiplier = switch (trafficLevel) {
            case LOW -> 1.2;
            case MEDIUM -> 1.0;
            case HIGH -> 0.7;
            case SEVERE -> 0.5;
        };
        
        double vehicleMultiplier = isElectric ? 1.05 : 1.0;
        
        double timeOfDayMultiplier = getTimeOfDayMultiplier();
        
        double adjustedSpeed = baseSpeed * trafficMultiplier * vehicleMultiplier * timeOfDayMultiplier;
        
        double baseETA = (distance / adjustedSpeed) * 60;
        
        double mlAdjustment = applyMLPrediction(distance, trafficLevel, isElectric);
        
        int finalETA = (int) Math.ceil(baseETA * mlAdjustment);
        
        log.info("Predicted ETA: {} minutes for distance: {} km, traffic: {}, electric: {}", 
                 finalETA, distance, trafficLevel, isElectric);
        
        return Math.max(1, finalETA);
    }
    
    private double applyMLPrediction(double distance, Route.TrafficLevel trafficLevel, boolean isElectric) {
        double historicalFactor = 0.95 + (random.nextDouble() * 0.1);
        
        double weatherFactor = 0.98 + (random.nextDouble() * 0.04);
        
        double patternFactor = switch (trafficLevel) {
            case LOW -> 0.95;
            case MEDIUM -> 1.0;
            case HIGH -> 1.1;
            case SEVERE -> 1.25;
        };
        
        double distanceFactor = distance > 20 ? 1.05 : 1.0;
        
        return historicalFactor * weatherFactor * patternFactor * distanceFactor;
    }
    
    private double getTimeOfDayMultiplier() {
        int hour = LocalDateTime.now().getHour();
        
        if (hour >= 7 && hour <= 9) return 0.7;
        if (hour >= 17 && hour <= 19) return 0.75;
        if (hour >= 22 || hour <= 5) return 1.3;
        
        return 1.0;
    }
    
    public int recalculateETA(Route route, double currentProgress) {
        double remainingDistance = route.getDistanceKm() * (1 - currentProgress);
        
        return predictETA(remainingDistance, route.getTrafficLevel(), false);
    }
    
    public Route.TrafficLevel predictTrafficLevel(double distance, String startLocation, String endLocation) {
        int hour = LocalDateTime.now().getHour();
        
        if ((hour >= 7 && hour <= 9) || (hour >= 17 && hour <= 19)) {
            return random.nextDouble() > 0.5 ? Route.TrafficLevel.HIGH : Route.TrafficLevel.SEVERE;
        } else if (hour >= 22 || hour <= 5) {
            return Route.TrafficLevel.LOW;
        }
        
        return random.nextDouble() > 0.5 ? Route.TrafficLevel.MEDIUM : Route.TrafficLevel.HIGH;
    }
}
