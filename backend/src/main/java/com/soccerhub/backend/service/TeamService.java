package com.soccerhub.backend.service;

import com.soccerhub.backend.entity.Team;
import com.soccerhub.backend.exception.ResourceNotFoundException;
import com.soccerhub.backend.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    
    private final TeamRepository teamRepository;
    
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }
    
    public Team getTeamById(Long id) {
        return teamRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
    }
    
    public List<Team> getTeamsByDivisionId(Long divisionId) {
        return teamRepository.findByDivisionId(divisionId);
    }
    
    @Transactional
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }
    
    @Transactional
    public Team updateTeam(Long id, Team teamDetails) {
        Team team = getTeamById(id);
        team.setName(teamDetails.getName());
        team.setDivisionId(teamDetails.getDivisionId());
        team.setLogo(teamDetails.getLogo());
        return teamRepository.save(team);
    }
    
    @Transactional
    public void deleteTeam(Long id) {
        Team team = getTeamById(id);
        teamRepository.delete(team);
    }
}
