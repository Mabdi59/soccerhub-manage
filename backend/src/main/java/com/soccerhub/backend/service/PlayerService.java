package com.soccerhub.backend.service;

import com.soccerhub.backend.entity.Player;
import com.soccerhub.backend.exception.ResourceNotFoundException;
import com.soccerhub.backend.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {
    
    private final PlayerRepository playerRepository;
    
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
    
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + id));
    }
    
    public List<Player> getPlayersByTeamId(Long teamId) {
        return playerRepository.findByTeamId(teamId);
    }
    
    @Transactional
    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }
    
    @Transactional
    public Player updatePlayer(Long id, Player playerDetails) {
        Player player = getPlayerById(id);
        player.setFirstName(playerDetails.getFirstName());
        player.setLastName(playerDetails.getLastName());
        player.setTeamId(playerDetails.getTeamId());
        player.setJerseyNumber(playerDetails.getJerseyNumber());
        player.setPosition(playerDetails.getPosition());
        return playerRepository.save(player);
    }
    
    @Transactional
    public void deletePlayer(Long id) {
        Player player = getPlayerById(id);
        playerRepository.delete(player);
    }
}
