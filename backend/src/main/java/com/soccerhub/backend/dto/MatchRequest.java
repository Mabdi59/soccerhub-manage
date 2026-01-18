package com.soccerhub.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequest {
    
    @NotNull(message = "Division ID is required")
    private Long divisionId;
    
    @NotNull(message = "Home team ID is required")
    private Long homeTeamId;
    
    @NotNull(message = "Away team ID is required")
    private Long awayTeamId;
    
    private Long venueId;
    
    @NotNull(message = "Match date is required")
    private LocalDateTime matchDate;
    
    private Integer homeScore;
    private Integer awayScore;
    private String playoffRound;
    private String status;
    private Long refereeId;
}
