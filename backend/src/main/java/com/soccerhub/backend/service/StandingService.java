package com.soccerhub.backend.service;

import com.soccerhub.backend.entity.Standing;
import com.soccerhub.backend.repository.StandingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StandingService {
    
    private final StandingRepository standingRepository;
    
    public List<Standing> getStandingsByDivisionId(Long divisionId) {
        return standingRepository.findByDivisionIdOrderByPointsDescGoalDifferenceDesc(divisionId);
    }
}
