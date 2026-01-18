package com.soccerhub.backend.service;

import com.soccerhub.backend.dto.MatchResultRequest;
import com.soccerhub.backend.entity.Division;
import com.soccerhub.backend.entity.Match;
import com.soccerhub.backend.entity.Standing;
import com.soccerhub.backend.entity.Tournament;
import com.soccerhub.backend.exception.BadRequestException;
import com.soccerhub.backend.exception.ResourceNotFoundException;
import com.soccerhub.backend.repository.DivisionRepository;
import com.soccerhub.backend.repository.MatchRepository;
import com.soccerhub.backend.repository.StandingRepository;
import com.soccerhub.backend.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    
    private final MatchRepository matchRepository;
    private final StandingRepository standingRepository;
    private final DivisionRepository divisionRepository;
    private final TournamentRepository tournamentRepository;
    
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
        match.setPlayoffRound(matchDetails.getPlayoffRound());
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
        
        if (match.getPlayoffRound() == null) {
            if (wasCompleted && oldHomeScore != null && oldAwayScore != null) {
                reverseStandingsUpdate(match.getDivisionId(), match.getHomeTeamId(),
                                      match.getAwayTeamId(), oldHomeScore, oldAwayScore);
            }

            updateStandings(match.getDivisionId(), match.getHomeTeamId(),
                           match.getAwayTeamId(), result.getHomeScore(), result.getAwayScore());
        } else {
            handlePlayoffProgression(match);
        }
        
        return match;
    }
    
    @Transactional
    public void deleteMatch(Long id) {
        Match match = getMatchById(id);
        
        if (match.getPlayoffRound() == null && match.getStatus() == Match.Status.COMPLETED &&
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

    private void handlePlayoffProgression(Match completedMatch) {
        if (!"SEMIFINAL".equalsIgnoreCase(completedMatch.getPlayoffRound())) {
            return;
        }

        List<Match> semifinals = matchRepository.findByDivisionIdAndPlayoffRound(
            completedMatch.getDivisionId(), "SEMIFINAL");

        boolean allCompleted = semifinals.stream().allMatch(match ->
            match.getStatus() == Match.Status.COMPLETED &&
            match.getHomeScore() != null &&
            match.getAwayScore() != null
        );

        if (!allCompleted) {
            return;
        }

        List<Long> winners = semifinals.stream()
            .map(match -> match.getHomeScore() >= match.getAwayScore()
                ? match.getHomeTeamId()
                : match.getAwayTeamId())
            .toList();

        if (winners.size() < 2) {
            return;
        }

        List<Match> finals = matchRepository.findByDivisionIdAndPlayoffRound(
            completedMatch.getDivisionId(), "FINAL");
        Match finalMatch = finals.isEmpty() ? null : finals.get(0);

        if (finalMatch == null) {
            Division division = divisionRepository.findById(completedMatch.getDivisionId())
                .orElseThrow(() -> new ResourceNotFoundException("Division not found with id: " + completedMatch.getDivisionId()));
            Tournament tournament = tournamentRepository.findById(division.getTournamentId())
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + division.getTournamentId()));
            LocalDateTime matchDate = tournament.getEndDate().atTime(LocalTime.NOON);

            Match match = new Match();
            match.setDivisionId(completedMatch.getDivisionId());
            match.setHomeTeamId(winners.get(0));
            match.setAwayTeamId(winners.get(1));
            match.setMatchDate(matchDate);
            match.setStatus(Match.Status.SCHEDULED);
            match.setPlayoffRound("FINAL");
            matchRepository.save(match);
        } else {
            finalMatch.setHomeTeamId(winners.get(0));
            finalMatch.setAwayTeamId(winners.get(1));
            matchRepository.save(finalMatch);
        }
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
