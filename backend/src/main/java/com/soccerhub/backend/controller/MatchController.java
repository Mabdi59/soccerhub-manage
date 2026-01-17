package com.soccerhub.backend.controller;

import com.soccerhub.backend.dto.MatchRequest;
import com.soccerhub.backend.dto.MatchResultRequest;
import com.soccerhub.backend.entity.Match;
import com.soccerhub.backend.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {
    
    private final MatchService matchService;
    
    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches(
            @RequestParam(required = false) Long divisionId,
            @RequestParam(required = false) Long teamId) {
        if (divisionId != null) {
            return ResponseEntity.ok(matchService.getMatchesByDivisionId(divisionId));
        }
        if (teamId != null) {
            return ResponseEntity.ok(matchService.getMatchesByTeamId(teamId));
        }
        return ResponseEntity.ok(matchService.getAllMatches());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }
    
    @PostMapping
    public ResponseEntity<Match> createMatch(@Valid @RequestBody MatchRequest matchRequest) {
        Match match = new Match();
        match.setDivisionId(matchRequest.getDivisionId());
        match.setHomeTeamId(matchRequest.getHomeTeamId());
        match.setAwayTeamId(matchRequest.getAwayTeamId());
        match.setVenueId(matchRequest.getVenueId());
        match.setMatchDate(matchRequest.getMatchDate());
        match.setHomeScore(matchRequest.getHomeScore());
        match.setAwayScore(matchRequest.getAwayScore());
        if (matchRequest.getStatus() != null) {
            match.setStatus(Match.Status.valueOf(matchRequest.getStatus()));
        }
        match.setRefereeId(matchRequest.getRefereeId());
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(matchService.createMatch(match));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long id, 
                                            @Valid @RequestBody MatchRequest matchRequest) {
        Match match = new Match();
        match.setDivisionId(matchRequest.getDivisionId());
        match.setHomeTeamId(matchRequest.getHomeTeamId());
        match.setAwayTeamId(matchRequest.getAwayTeamId());
        match.setVenueId(matchRequest.getVenueId());
        match.setMatchDate(matchRequest.getMatchDate());
        match.setHomeScore(matchRequest.getHomeScore());
        match.setAwayScore(matchRequest.getAwayScore());
        if (matchRequest.getStatus() != null) {
            match.setStatus(Match.Status.valueOf(matchRequest.getStatus()));
        }
        match.setRefereeId(matchRequest.getRefereeId());
        
        return ResponseEntity.ok(matchService.updateMatch(id, match));
    }
    
    @PatchMapping("/{id}/result")
    public ResponseEntity<Match> updateMatchResult(@PathVariable Long id, 
                                                   @Valid @RequestBody MatchResultRequest result) {
        return ResponseEntity.ok(matchService.updateMatchResult(id, result));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}
