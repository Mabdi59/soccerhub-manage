package com.soccerhub.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultRequest {
    
    @NotNull(message = "Home score is required")
    private Integer homeScore;
    
    @NotNull(message = "Away score is required")
    private Integer awayScore;
}
