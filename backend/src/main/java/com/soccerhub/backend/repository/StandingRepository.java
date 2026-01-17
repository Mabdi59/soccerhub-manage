package com.soccerhub.backend.repository;

import com.soccerhub.backend.entity.Standing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StandingRepository extends JpaRepository<Standing, Long> {
    List<Standing> findByDivisionIdOrderByPointsDescGoalDifferenceDesc(Long divisionId);
    Optional<Standing> findByDivisionIdAndTeamId(Long divisionId, Long teamId);
}
