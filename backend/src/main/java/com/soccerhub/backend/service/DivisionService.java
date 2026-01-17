package com.soccerhub.backend.service;

import com.soccerhub.backend.entity.Division;
import com.soccerhub.backend.exception.ResourceNotFoundException;
import com.soccerhub.backend.repository.DivisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DivisionService {
    
    private final DivisionRepository divisionRepository;
    
    public List<Division> getAllDivisions() {
        return divisionRepository.findAll();
    }
    
    public Division getDivisionById(Long id) {
        return divisionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Division not found with id: " + id));
    }
    
    public List<Division> getDivisionsByTournamentId(Long tournamentId) {
        return divisionRepository.findByTournamentId(tournamentId);
    }
    
    @Transactional
    public Division createDivision(Division division) {
        return divisionRepository.save(division);
    }
    
    @Transactional
    public Division updateDivision(Long id, Division divisionDetails) {
        Division division = getDivisionById(id);
        division.setName(divisionDetails.getName());
        division.setTournamentId(divisionDetails.getTournamentId());
        return divisionRepository.save(division);
    }
    
    @Transactional
    public void deleteDivision(Long id) {
        Division division = getDivisionById(id);
        divisionRepository.delete(division);
    }
}
