package com.soccerhub.backend.service;

import com.soccerhub.backend.entity.Division;
import com.soccerhub.backend.entity.Match;
import com.soccerhub.backend.entity.Team;
import com.soccerhub.backend.entity.Tournament;
import com.soccerhub.backend.entity.Venue;
import com.soccerhub.backend.exception.BadRequestException;
import com.soccerhub.backend.exception.ResourceNotFoundException;
import com.soccerhub.backend.repository.DivisionRepository;
import com.soccerhub.backend.repository.MatchRepository;
import com.soccerhub.backend.repository.TeamRepository;
import com.soccerhub.backend.repository.TournamentRepository;
import com.soccerhub.backend.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DivisionService {
    
    private final DivisionRepository divisionRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final MatchService matchService;
    
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

    @Transactional
    public List<Match> generateSchedule(Long divisionId) {
        Division division = getDivisionById(divisionId);
        List<Team> teams = teamRepository.findByDivisionId(divisionId);
        if (teams.size() < 2) {
            throw new BadRequestException("Division must have at least 2 teams to generate a schedule");
        }

        Tournament tournament = tournamentRepository.findById(division.getTournamentId())
            .orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + division.getTournamentId()));
        LocalDate startDate = tournament.getStartDate();
        LocalDate endDate = tournament.getEndDate();
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("Tournament end date must be on or after the start date");
        }

        matchRepository.deleteByDivisionIdAndStatus(divisionId, Match.Status.SCHEDULED);

        List<Venue> venues = venueRepository.findAll();
        List<Long> teamIds = teams.stream().map(Team::getId).toList();
        List<List<MatchPair>> rounds = buildRoundRobinRounds(teamIds);
        int totalRounds = rounds.size();
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate);
        double step = totalRounds > 1 ? (double) totalDays / (totalRounds - 1) : 0;

        List<Match> generatedMatches = new ArrayList<>();
        int venueIndex = 0;
        for (int roundIndex = 0; roundIndex < rounds.size(); roundIndex++) {
            long offset = Math.round(step * roundIndex);
            LocalDate matchDay = startDate.plusDays(offset);
            LocalDateTime matchDateTime = matchDay.atTime(LocalTime.NOON);

            for (MatchPair pair : rounds.get(roundIndex)) {
                Match match = new Match();
                match.setDivisionId(divisionId);
                match.setHomeTeamId(pair.homeTeamId());
                match.setAwayTeamId(pair.awayTeamId());
                if (!venues.isEmpty()) {
                    match.setVenueId(venues.get(venueIndex % venues.size()).getId());
                    venueIndex++;
                }
                match.setMatchDate(matchDateTime);
                match.setStatus(Match.Status.SCHEDULED);
                generatedMatches.add(matchService.createMatch(match));
            }
        }

        return generatedMatches;
    }

    private List<List<MatchPair>> buildRoundRobinRounds(List<Long> teamIds) {
        List<Long> teams = new ArrayList<>(teamIds);
        if (teams.size() % 2 != 0) {
            teams.add(null);
        }

        int totalTeams = teams.size();
        int rounds = totalTeams - 1;
        List<List<MatchPair>> schedule = new ArrayList<>();

        for (int round = 0; round < rounds; round++) {
            List<MatchPair> matchPairs = new ArrayList<>();
            for (int i = 0; i < totalTeams / 2; i++) {
                Long home = teams.get(i);
                Long away = teams.get(totalTeams - 1 - i);
                if (home != null && away != null) {
                    matchPairs.add(new MatchPair(home, away));
                }
            }
            schedule.add(matchPairs);
            teams = rotateTeams(teams);
        }

        return schedule;
    }

    private List<Long> rotateTeams(List<Long> teams) {
        List<Long> rotated = new ArrayList<>(teams.size());
        rotated.add(teams.get(0));
        rotated.add(teams.get(teams.size() - 1));
        rotated.addAll(teams.subList(1, teams.size() - 1));
        return rotated;
    }

    private record MatchPair(Long homeTeamId, Long awayTeamId) {
    }
}
