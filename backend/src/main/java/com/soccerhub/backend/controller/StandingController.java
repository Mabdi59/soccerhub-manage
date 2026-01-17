package com.soccerhub.backend.controller;

import com.soccerhub.backend.entity.Standing;
import com.soccerhub.backend.service.StandingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standings")
@RequiredArgsConstructor
public class StandingController {
    
    private final StandingService standingService;
    
    @GetMapping
    public ResponseEntity<List<Standing>> getStandings(@RequestParam Long divisionId) {
        return ResponseEntity.ok(standingService.getStandingsByDivisionId(divisionId));
    }
}
