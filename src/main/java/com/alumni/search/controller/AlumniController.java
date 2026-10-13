package com.alumni.search.controller;

import com.alumni.search.dto.ApiResponse;
import com.alumni.search.dto.SearchRequest;
import com.alumni.search.model.AlumniProfile;
import com.alumni.search.service.AlumniService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alumni")
public class AlumniController {

    private final AlumniService alumniService;

    public AlumniController(AlumniService alumniService) {
        this.alumniService = alumniService;
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<AlumniProfile>>> search(@Valid @RequestBody SearchRequest request) {
        List<AlumniProfile> profiles = alumniService.searchAndSave(request);
        return ResponseEntity.ok(ApiResponse.success(profiles));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AlumniProfile>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(alumniService.getAllProfiles()));
    }
}
