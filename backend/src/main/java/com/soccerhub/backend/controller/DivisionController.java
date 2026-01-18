package com.soccerhub.backend.controller;

import com.soccerhub.backend.entity.Division;
import com.soccerhub.backend.entity.Match;
import com.soccerhub.backend.service.DivisionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/divisions")
@RequiredArgsConstructor
public class DivisionController {
    
    private final DivisionService divisionService;
    
    @GetMapping
    public ResponseEntity<List<Division>> getAllDivisions(
            @RequestParam(required = false) Long tournamentId) {
        if (tournamentId != null) {
            return ResponseEntity.ok(divisionService.getDivisionsByTournamentId(tournamentId));
        }
        return ResponseEntity.ok(divisionService.getAllDivisions());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Division> getDivisionById(@PathVariable Long id) {
        return ResponseEntity.ok(divisionService.getDivisionById(id));
    }
    
    @PostMapping
    public ResponseEntity<Division> createDivision(@Valid @RequestBody Division division) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(divisionService.createDivision(division));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Division> updateDivision(@PathVariable Long id, 
                                                   @Valid @RequestBody Division division) {
        return ResponseEntity.ok(divisionService.updateDivision(id, division));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDivision(@PathVariable Long id) {
        divisionService.deleteDivision(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/generate-schedule")
    public ResponseEntity<List<Match>> generateSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(divisionService.generateSchedule(id));
    }

    @PostMapping("/{id}/generate-playoffs")
    public ResponseEntity<List<Match>> generatePlayoffs(@PathVariable Long id) {
        return ResponseEntity.ok(divisionService.generatePlayoffs(id));
    }
}
