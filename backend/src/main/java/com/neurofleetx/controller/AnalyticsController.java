package com.neurofleetx.controller;

import com.neurofleetx.dto.*;
import com.neurofleetx.service.AnalyticsService;
import com.neurofleetx.service.ReportGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "http://localhost:3000")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private ReportGenerationService reportGenerationService;

    @GetMapping("/kpi")
    public ResponseEntity<AnalyticsKPIResponse> getKPIMetrics() {
        return ResponseEntity.ok(analyticsService.getKPIMetrics());
    }

    @GetMapping("/fleet-distribution")
    public ResponseEntity<FleetDistributionData> getFleetDistribution() {
        return ResponseEntity.ok(analyticsService.getFleetDistribution());
    }

    @GetMapping("/hourly-activity")
    public ResponseEntity<HourlyActivityData> getHourlyActivity() {
        return ResponseEntity.ok(analyticsService.getHourlyActivity());
    }

    @GetMapping("/daily-trends")
    public ResponseEntity<Map<String, Object>> getDailyTrends(
            @RequestParam(defaultValue = "7") int days) {
        return ResponseEntity.ok(analyticsService.getDailyTrends(days));
    }

    @GetMapping("/vehicle-performance")
    public ResponseEntity<Map<String, Object>> getVehiclePerformance() {
        return ResponseEntity.ok(analyticsService.getVehiclePerformance());
    }

    @GetMapping("/reports/fleet/csv")
    public ResponseEntity<byte[]> downloadFleetReportCSV() {
        byte[] csvData = reportGenerationService.generateFleetReportCSV();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", 
            "fleet-report-" + generateTimestamp() + ".csv");
        headers.setContentLength(csvData.length);

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    @GetMapping("/reports/bookings/csv")
    public ResponseEntity<byte[]> downloadBookingsReportCSV() {
        byte[] csvData = reportGenerationService.generateBookingsReportCSV();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", 
            "bookings-report-" + generateTimestamp() + ".csv");
        headers.setContentLength(csvData.length);

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    @GetMapping("/reports/revenue/csv")
    public ResponseEntity<byte[]> downloadRevenueReportCSV() {
        byte[] csvData = reportGenerationService.generateRevenueReportCSV();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", 
            "revenue-report-" + generateTimestamp() + ".csv");
        headers.setContentLength(csvData.length);

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    @GetMapping("/reports/trips/csv")
    public ResponseEntity<byte[]> downloadTripsReportCSV() {
        byte[] csvData = reportGenerationService.generateTripsReportCSV();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", 
            "trips-report-" + generateTimestamp() + ".csv");
        headers.setContentLength(csvData.length);

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    @GetMapping("/reports/summary/csv")
    public ResponseEntity<byte[]> downloadAnalyticsSummaryCSV() {
        byte[] csvData = reportGenerationService.generateAnalyticsSummaryCSV();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", 
            "analytics-summary-" + generateTimestamp() + ".csv");
        headers.setContentLength(csvData.length);

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    private String generateTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
    }
}
