package com.election.evm.controller;

import com.election.evm.dto.ApiResponse;
import com.election.evm.dto.BulkUploadResult;
import com.election.evm.dto.ElectionResultRequest;
import com.election.evm.entity.ElectionResult;
import com.election.evm.service.EvmService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * FIXED ELECTION CONTROLLER
 *
 * ISSUE:
 * You added backend URL in CORS:
 * "https://evmbackend-n3qk.onrender.com"
 *
 * WRONG:
 * CORS must allow FRONTEND origin, not backend.
 *
 * CORRECT FRONTEND:
 * https://ev-mfrontend-qbd1sz7c5-peddi-yeswanths-projects.vercel.app
 */
@RestController
@RequestMapping("/api/election-results")
@CrossOrigin(
        origins = {
                "http://localhost:5173",
                "http://127.0.0.1:5173",

                // YOUR LIVE FRONTEND URL
                "https://ev-mfrontend-qbd1sz7c5-peddi-yeswanths-projects.vercel.app"
        },
        allowCredentials = "true"
)
public class ElectionController {

    private final EvmService service;

    public ElectionController(EvmService service) {
        this.service = service;
    }

    /**
     * Get all election results
     */
    @GetMapping
    public ApiResponse<List<ElectionResult>> getElectionResults() {
        return service.getElectionResults();
    }

    /**
     * Create new election result
     */
    @PostMapping
    public ApiResponse<ElectionResult> createElectionResult(
            @Valid @RequestBody ElectionResultRequest request) {
        return service.createElectionResult(request);
    }

    /**
     * Bulk upload election data
     */
    @PostMapping("/bulk-upload")
    public ApiResponse<BulkUploadResult> bulkUploadElectionData(
            @RequestParam("file") MultipartFile file) {
        return service.bulkUploadElectionData(file);
    }

    /**
     * Update election result
     */
    @PutMapping("/{id}")
    public ApiResponse<ElectionResult> updateElectionResult(
            @PathVariable("id") String resultId,
            @Valid @RequestBody ElectionResultRequest request) {
        return service.updateElectionResult(resultId, request);
    }

    /**
     * Delete election result
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteElectionResult(
            @PathVariable("id") String resultId) {
        return service.deleteElectionResult(resultId);
    }
}