package com.neurofleetx.service;

import com.neurofleetx.model.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelemetrySimulator {
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Scheduled(fixedRate = 3000)
    public void simulateTelemetry() {
        try {
            List<Vehicle> vehicles = vehicleService.updateAllVehiclesTelemetry();
            messagingTemplate.convertAndSend("/topic/telemetry", vehicles);
        } catch (Exception e) {
            System.err.println("Error simulating telemetry: " + e.getMessage());
        }
    }
}
