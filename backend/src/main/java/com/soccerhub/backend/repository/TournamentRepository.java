package com.soccerhub.backend.repository;

import com.soccerhub.backend.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    List<Tournament> findByOrganizationId(Long organizationId);
}
