package com.soccerhub.backend.controller;

import com.soccerhub.backend.entity.Tournament;
import com.soccerhub.backend.service.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
public class TournamentController {
    
    private final TournamentService tournamentService;
    
    @GetMapping
    public ResponseEntity<List<Tournament>> getAllTournaments(
            @RequestParam(required = false) Long organizationId) {
        if (organizationId != null) {
            return ResponseEntity.ok(tournamentService.getTournamentsByOrganizationId(organizationId));
        }
        return ResponseEntity.ok(tournamentService.getAllTournaments());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Tournament> getTournamentById(@PathVariable Long id) {
        return ResponseEntity.ok(tournamentService.getTournamentById(id));
    }
    
    @PostMapping
    public ResponseEntity<Tournament> createTournament(@Valid @RequestBody Tournament tournament) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(tournamentService.createTournament(tournament));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Tournament> updateTournament(@PathVariable Long id, 
                                                      @Valid @RequestBody Tournament tournament) {
        return ResponseEntity.ok(tournamentService.updateTournament(id, tournament));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }
}
