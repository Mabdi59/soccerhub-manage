package com.soccerhub.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long divisionId;
    
    @Column(nullable = false)
    private Long homeTeamId;
    
    @Column(nullable = false)
    private Long awayTeamId;
    
    @Column
    private Long venueId;
    
    @Column(nullable = false)
    private LocalDateTime matchDate;
    
    @Column
    private Integer homeScore;
    
    @Column
    private Integer awayScore;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.SCHEDULED;
    
    @Column
    private Long refereeId;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    public enum Status {
        SCHEDULED, IN_PROGRESS, COMPLETED, POSTPONED, CANCELLED
    }
}
