package com.soccerhub.backend.service;

import com.soccerhub.backend.entity.Tournament;
import com.soccerhub.backend.exception.ResourceNotFoundException;
import com.soccerhub.backend.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TournamentService {
    
    private final TournamentRepository tournamentRepository;
    
    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }
    
    public Tournament getTournamentById(Long id) {
        return tournamentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + id));
    }
    
    public List<Tournament> getTournamentsByOrganizationId(Long organizationId) {
        return tournamentRepository.findByOrganizationId(organizationId);
    }
    
    @Transactional
    public Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }
    
    @Transactional
    public Tournament updateTournament(Long id, Tournament tournamentDetails) {
        Tournament tournament = getTournamentById(id);
        tournament.setName(tournamentDetails.getName());
        tournament.setOrganizationId(tournamentDetails.getOrganizationId());
        tournament.setStartDate(tournamentDetails.getStartDate());
        tournament.setEndDate(tournamentDetails.getEndDate());
        tournament.setStatus(tournamentDetails.getStatus());
        return tournamentRepository.save(tournament);
    }
    
    @Transactional
    public void deleteTournament(Long id) {
        Tournament tournament = getTournamentById(id);
        tournamentRepository.delete(tournament);
    }
}
