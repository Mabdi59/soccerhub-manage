package com.soccerhub.backend.config;

import com.soccerhub.backend.entity.*;
import com.soccerhub.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final TournamentRepository tournamentRepository;
    private final DivisionRepository divisionRepository;
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final VenueRepository venueRepository;
    private final MatchRepository matchRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.seed.enabled:true}")
    private boolean seedEnabled;
    
    @Override
    public void run(String... args) {
        if (!seedEnabled || userRepository.count() > 0) {
            log.info("Skipping data seeding");
            return;
        }
        
        log.info("Starting data seeding...");
        seedUsers();
        seedOrganizations();
        seedTournaments();
        seedDivisions();
        seedTeams();
        seedPlayers();
        seedVenues();
        seedMatches();
        log.info("Data seeding completed successfully!");
    }
    
    private void seedUsers() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@soccerhub.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);
        
        User organizer = new User();
        organizer.setUsername("organizer");
        organizer.setEmail("organizer@soccerhub.com");
        organizer.setPassword(passwordEncoder.encode("organizer123"));
        organizer.setRole(User.Role.ORGANIZER);
        userRepository.save(organizer);
        
        User referee = new User();
        referee.setUsername("referee");
        referee.setEmail("referee@soccerhub.com");
        referee.setPassword(passwordEncoder.encode("referee123"));
        referee.setRole(User.Role.REFEREE);
        userRepository.save(referee);
        
        log.info("Seeded users");
    }
    
    private void seedOrganizations() {
        Organization org1 = new Organization();
        org1.setName("Premier Soccer League");
        org1.setDescription("The leading soccer organization");
        organizationRepository.save(org1);
        
        Organization org2 = new Organization();
        org2.setName("Youth Soccer Federation");
        org2.setDescription("Promoting youth soccer development");
        organizationRepository.save(org2);
        
        log.info("Seeded organizations");
    }
    
    private void seedTournaments() {
        Tournament tournament1 = new Tournament();
        tournament1.setName("Spring Championship 2024");
        tournament1.setOrganizationId(1L);
        tournament1.setStartDate(LocalDate.now().plusDays(7));
        tournament1.setEndDate(LocalDate.now().plusDays(90));
        tournament1.setStatus(Tournament.Status.UPCOMING);
        tournamentRepository.save(tournament1);
        
        Tournament tournament2 = new Tournament();
        tournament2.setName("Fall Cup 2024");
        tournament2.setOrganizationId(1L);
        tournament2.setStartDate(LocalDate.now().plusDays(180));
        tournament2.setEndDate(LocalDate.now().plusDays(270));
        tournament2.setStatus(Tournament.Status.UPCOMING);
        tournamentRepository.save(tournament2);
        
        log.info("Seeded tournaments");
    }
    
    private void seedDivisions() {
        Division division1 = new Division();
        division1.setName("Men's Premier Division");
        division1.setTournamentId(1L);
        divisionRepository.save(division1);
        
        Division division2 = new Division();
        division2.setName("Women's Premier Division");
        division2.setTournamentId(1L);
        divisionRepository.save(division2);
        
        log.info("Seeded divisions");
    }
    
    private void seedTeams() {
        String[] teamNames = {"FC Thunder", "City United", "River Plate", "Mountain FC"};
        
        for (int i = 0; i < teamNames.length; i++) {
            Team team = new Team();
            team.setName(teamNames[i]);
            team.setDivisionId(1L);
            team.setLogo(null);
            teamRepository.save(team);
        }
        
        log.info("Seeded teams");
    }
    
    private void seedPlayers() {
        String[][] players = {
            {"John", "Smith", "Forward"},
            {"Michael", "Johnson", "Midfielder"},
            {"David", "Williams", "Defender"},
            {"James", "Brown", "Goalkeeper"}
        };
        
        for (int i = 0; i < players.length; i++) {
            Player player = new Player();
            player.setFirstName(players[i][0]);
            player.setLastName(players[i][1]);
            player.setTeamId(1L);
            player.setJerseyNumber(i + 1);
            player.setPosition(players[i][2]);
            playerRepository.save(player);
        }
        
        log.info("Seeded players");
    }
    
    private void seedVenues() {
        Venue venue1 = new Venue();
        venue1.setName("Central Stadium");
        venue1.setAddress("123 Main Street");
        venue1.setCity("Springfield");
        venue1.setCapacity(50000);
        venueRepository.save(venue1);
        
        Venue venue2 = new Venue();
        venue2.setName("Sports Complex Arena");
        venue2.setAddress("456 Oak Avenue");
        venue2.setCity("Riverside");
        venue2.setCapacity(30000);
        venueRepository.save(venue2);
        
        log.info("Seeded venues");
    }
    
    private void seedMatches() {
        Match match1 = new Match();
        match1.setDivisionId(1L);
        match1.setHomeTeamId(1L);
        match1.setAwayTeamId(2L);
        match1.setVenueId(1L);
        match1.setMatchDate(LocalDateTime.now().plusDays(10));
        match1.setStatus(Match.Status.SCHEDULED);
        match1.setRefereeId(3L);
        matchRepository.save(match1);
        
        Match match2 = new Match();
        match2.setDivisionId(1L);
        match2.setHomeTeamId(3L);
        match2.setAwayTeamId(4L);
        match2.setVenueId(2L);
        match2.setMatchDate(LocalDateTime.now().plusDays(11));
        match2.setStatus(Match.Status.SCHEDULED);
        match2.setRefereeId(3L);
        matchRepository.save(match2);
        
        log.info("Seeded matches");
    }
}
