package com.election.evm.controller;

import com.election.evm.dto.AnalystReportRequest;
import com.election.evm.dto.ApiResponse;
import com.election.evm.dto.DashboardStats;
import com.election.evm.dto.FraudReportRequest;
import com.election.evm.dto.IncidentRequest;
import com.election.evm.dto.UserRequest;
import com.election.evm.entity.AnalystReport;
import com.election.evm.entity.FraudReport;
import com.election.evm.entity.Incident;
import com.election.evm.entity.User;
import com.election.evm.service.EvmService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FIXED ADMIN CONTROLLER BASED ON YOUR ORIGINAL CODE
 * Your uploaded file currently allows backend URL in CORS, which is incorrect: :contentReference[oaicite:0]{index=0}
 *
 * REMOVE:
 * https://evmbackend-n3qk.onrender.com
 *
 * ADD:
 * https://ev-mfrontend-qbd1sz7c5-peddi-yeswanths-projects.vercel.app
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://127.0.0.1:5173",

                // YOUR LIVE FRONTEND (CORRECT)
                "https://ev-mfrontend-qbd1sz7c5-peddi-yeswanths-projects.vercel.app"
        },
        allowCredentials = "true"
)
public class AdminController {

    private final EvmService service;

    public AdminController(EvmService service) {
        this.service = service;
    }

    // ==================== USER MANAGEMENT ====================

    @GetMapping("/users")
    public ApiResponse<List<User>> getUsers() {
        return service.getUsers();
    }

    @PostMapping("/users")
    public ApiResponse<User> createUser(@Valid @RequestBody UserRequest request) {
        return service.createUser(request);
    }

    @PutMapping("/users/{id}")
    public ApiResponse<User> updateUser(
            @PathVariable("id") Long userId,
            @Valid @RequestBody UserRequest request) {
        return service.updateUser(userId, request);
    }

    @DeleteMapping("/users/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable("id") Long userId) {
        return service.deleteUser(userId);
    }

    // ==================== INCIDENT MANAGEMENT ====================

    @GetMapping("/incidents")
    public ApiResponse<List<Incident>> getIncidents() {
        return service.getIncidents();
    }

    @PostMapping("/incidents")
    public ApiResponse<Incident> createIncident(
            @Valid @RequestBody IncidentRequest request) {
        return service.createIncident(request);
    }

    @PutMapping("/incidents/{id}")
    public ApiResponse<Incident> updateIncident(
            @PathVariable("id") String incidentId,
            @Valid @RequestBody IncidentRequest request) {
        return service.updateIncident(incidentId, request);
    }

    @DeleteMapping("/incidents/{id}")
    public ApiResponse<Void> deleteIncident(
            @PathVariable("id") String incidentId) {
        return service.deleteIncident(incidentId);
    }

    // ==================== FRAUD REPORT MANAGEMENT ====================

    @GetMapping("/fraud-reports")
    public ApiResponse<List<FraudReport>> getFraudReports() {
        return service.getFraudReports();
    }

    @PostMapping("/fraud-reports")
    public ApiResponse<FraudReport> createFraudReport(
            @Valid @RequestBody FraudReportRequest request) {
        return service.createFraudReport(request);
    }

    @PutMapping("/fraud-reports/{id}")
    public ApiResponse<FraudReport> updateFraudReport(
            @PathVariable("id") String reportId,
            @Valid @RequestBody FraudReportRequest request) {
        return service.updateFraudReport(reportId, request);
    }

    @DeleteMapping("/fraud-reports/{id}")
    public ApiResponse<Void> deleteFraudReport(
            @PathVariable("id") String reportId) {
        return service.deleteFraudReport(reportId);
    }

    // ==================== ANALYST REPORT MANAGEMENT ====================

    @GetMapping("/analyst-reports")
    public ApiResponse<List<AnalystReport>> getAnalystReports() {
        return service.getAnalystReports();
    }

    @PostMapping("/analyst-reports")
    public ApiResponse<AnalystReport> createAnalystReport(
            @Valid @RequestBody AnalystReportRequest request) {
        return service.createAnalystReport(request);
    }

    @PutMapping("/analyst-reports/{id}")
    public ApiResponse<AnalystReport> updateAnalystReport(
            @PathVariable("id") String reportId,
            @Valid @RequestBody AnalystReportRequest request) {
        return service.updateAnalystReport(reportId, request);
    }

    @DeleteMapping("/analyst-reports/{id}")
    public ApiResponse<Void> deleteAnalystReport(
            @PathVariable("id") String reportId) {
        return service.deleteAnalystReport(reportId);
    }

    // ==================== DASHBOARD ====================

    @GetMapping("/dashboard/stats")
    public ApiResponse<DashboardStats> getDashboardStats() {
        return service.getDashboardStats();
    }
}