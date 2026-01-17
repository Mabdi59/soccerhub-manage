package com.soccerhub.backend.service;

import com.soccerhub.backend.dto.MatchResultRequest;
import com.soccerhub.backend.entity.Match;
import com.soccerhub.backend.entity.Standing;
import com.soccerhub.backend.exception.BadRequestException;
import com.soccerhub.backend.exception.ResourceNotFoundException;
import com.soccerhub.backend.repository.MatchRepository;
import com.soccerhub.backend.repository.StandingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    
    private final MatchRepository matchRepository;
    private final StandingRepository standingRepository;
    
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }
    
    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + id));
    }
    
    public List<Match> getMatchesByDivisionId(Long divisionId) {
        return matchRepository.findByDivisionId(divisionId);
    }
    
    public List<Match> getMatchesByTeamId(Long teamId) {
        return matchRepository.findByHomeTeamIdOrAwayTeamId(teamId, teamId);
    }
    
    @Transactional
    public Match createMatch(Match match) {
        if (match.getHomeTeamId().equals(match.getAwayTeamId())) {
            throw new BadRequestException("Home team and away team cannot be the same");
        }
        return matchRepository.save(match);
    }
    
    @Transactional
    public Match updateMatch(Long id, Match matchDetails) {
        Match match = getMatchById(id);
        match.setDivisionId(matchDetails.getDivisionId());
        match.setHomeTeamId(matchDetails.getHomeTeamId());
        match.setAwayTeamId(matchDetails.getAwayTeamId());
        match.setVenueId(matchDetails.getVenueId());
        match.setMatchDate(matchDetails.getMatchDate());
        match.setHomeScore(matchDetails.getHomeScore());
        match.setAwayScore(matchDetails.getAwayScore());
        match.setStatus(matchDetails.getStatus());
        match.setRefereeId(matchDetails.getRefereeId());
        return matchRepository.save(match);
    }
    
    @Transactional
    public Match updateMatchResult(Long id, MatchResultRequest result) {
        Match match = getMatchById(id);
        
        boolean wasCompleted = match.getStatus() == Match.Status.COMPLETED;
        Integer oldHomeScore = match.getHomeScore();
        Integer oldAwayScore = match.getAwayScore();
        
        match.setHomeScore(result.getHomeScore());
        match.setAwayScore(result.getAwayScore());
        match.setStatus(Match.Status.COMPLETED);
        match = matchRepository.save(match);
        
        if (wasCompleted && oldHomeScore != null && oldAwayScore != null) {
            reverseStandingsUpdate(match.getDivisionId(), match.getHomeTeamId(), 
                                  match.getAwayTeamId(), oldHomeScore, oldAwayScore);
        }
        
        updateStandings(match.getDivisionId(), match.getHomeTeamId(), 
                       match.getAwayTeamId(), result.getHomeScore(), result.getAwayScore());
        
        return match;
    }
    
    @Transactional
    public void deleteMatch(Long id) {
        Match match = getMatchById(id);
        
        if (match.getStatus() == Match.Status.COMPLETED && 
            match.getHomeScore() != null && match.getAwayScore() != null) {
            reverseStandingsUpdate(match.getDivisionId(), match.getHomeTeamId(), 
                                  match.getAwayTeamId(), match.getHomeScore(), match.getAwayScore());
        }
        
        matchRepository.delete(match);
    }
    
    private void updateStandings(Long divisionId, Long homeTeamId, Long awayTeamId, 
                                Integer homeScore, Integer awayScore) {
        Standing homeStanding = getOrCreateStanding(divisionId, homeTeamId);
        Standing awayStanding = getOrCreateStanding(divisionId, awayTeamId);
        
        homeStanding.setPlayed(homeStanding.getPlayed() + 1);
        awayStanding.setPlayed(awayStanding.getPlayed() + 1);
        
        homeStanding.setGoalsFor(homeStanding.getGoalsFor() + homeScore);
        homeStanding.setGoalsAgainst(homeStanding.getGoalsAgainst() + awayScore);
        awayStanding.setGoalsFor(awayStanding.getGoalsFor() + awayScore);
        awayStanding.setGoalsAgainst(awayStanding.getGoalsAgainst() + homeScore);
        
        if (homeScore > awayScore) {
            homeStanding.setWon(homeStanding.getWon() + 1);
            homeStanding.setPoints(homeStanding.getPoints() + 3);
            awayStanding.setLost(awayStanding.getLost() + 1);
        } else if (homeScore < awayScore) {
            awayStanding.setWon(awayStanding.getWon() + 1);
            awayStanding.setPoints(awayStanding.getPoints() + 3);
            homeStanding.setLost(homeStanding.getLost() + 1);
        } else {
            homeStanding.setDrawn(homeStanding.getDrawn() + 1);
            homeStanding.setPoints(homeStanding.getPoints() + 1);
            awayStanding.setDrawn(awayStanding.getDrawn() + 1);
            awayStanding.setPoints(awayStanding.getPoints() + 1);
        }
        
        homeStanding.setGoalDifference(homeStanding.getGoalsFor() - homeStanding.getGoalsAgainst());
        awayStanding.setGoalDifference(awayStanding.getGoalsFor() - awayStanding.getGoalsAgainst());
        
        standingRepository.save(homeStanding);
        standingRepository.save(awayStanding);
    }
    
    private void reverseStandingsUpdate(Long divisionId, Long homeTeamId, Long awayTeamId, 
                                       Integer homeScore, Integer awayScore) {
        Standing homeStanding = getOrCreateStanding(divisionId, homeTeamId);
        Standing awayStanding = getOrCreateStanding(divisionId, awayTeamId);
        
        homeStanding.setPlayed(homeStanding.getPlayed() - 1);
        awayStanding.setPlayed(awayStanding.getPlayed() - 1);
        
        homeStanding.setGoalsFor(homeStanding.getGoalsFor() - homeScore);
        homeStanding.setGoalsAgainst(homeStanding.getGoalsAgainst() - awayScore);
        awayStanding.setGoalsFor(awayStanding.getGoalsFor() - awayScore);
        awayStanding.setGoalsAgainst(awayStanding.getGoalsAgainst() - homeScore);
        
        if (homeScore > awayScore) {
            homeStanding.setWon(homeStanding.getWon() - 1);
            homeStanding.setPoints(homeStanding.getPoints() - 3);
            awayStanding.setLost(awayStanding.getLost() - 1);
        } else if (homeScore < awayScore) {
            awayStanding.setWon(awayStanding.getWon() - 1);
            awayStanding.setPoints(awayStanding.getPoints() - 3);
            homeStanding.setLost(homeStanding.getLost() - 1);
        } else {
            homeStanding.setDrawn(homeStanding.getDrawn() - 1);
            homeStanding.setPoints(homeStanding.getPoints() - 1);
            awayStanding.setDrawn(awayStanding.getDrawn() - 1);
            awayStanding.setPoints(awayStanding.getPoints() - 1);
        }
        
        homeStanding.setGoalDifference(homeStanding.getGoalsFor() - homeStanding.getGoalsAgainst());
        awayStanding.setGoalDifference(awayStanding.getGoalsFor() - awayStanding.getGoalsAgainst());
        
        standingRepository.save(homeStanding);
        standingRepository.save(awayStanding);
    }
    
    private Standing getOrCreateStanding(Long divisionId, Long teamId) {
        return standingRepository.findByDivisionIdAndTeamId(divisionId, teamId)
            .orElseGet(() -> {
                Standing standing = new Standing();
                standing.setDivisionId(divisionId);
                standing.setTeamId(teamId);
                standing.setPlayed(0);
                standing.setWon(0);
                standing.setDrawn(0);
                standing.setLost(0);
                standing.setGoalsFor(0);
                standing.setGoalsAgainst(0);
                standing.setGoalDifference(0);
                standing.setPoints(0);
                return standingRepository.save(standing);
            });
    }
}
