package com.soccerhub.backend.controller;

import com.soccerhub.backend.entity.Team;
import com.soccerhub.backend.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    
    private final TeamService teamService;
    
    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams(
            @RequestParam(required = false) Long divisionId) {
        if (divisionId != null) {
            return ResponseEntity.ok(teamService.getTeamsByDivisionId(divisionId));
        }
        return ResponseEntity.ok(teamService.getAllTeams());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getTeamById(id));
    }
    
    @PostMapping
    public ResponseEntity<Team> createTeam(@Valid @RequestBody Team team) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(teamService.createTeam(team));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, 
                                          @Valid @RequestBody Team team) {
        return ResponseEntity.ok(teamService.updateTeam(id, team));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
