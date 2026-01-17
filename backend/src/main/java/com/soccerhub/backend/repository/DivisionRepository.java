package com.soccerhub.backend.repository;

import com.soccerhub.backend.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {
    List<Division> findByTournamentId(Long tournamentId);
}
