package com.soccerhub.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "standings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"divisionId", "teamId"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Standing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long divisionId;
    
    @Column(nullable = false)
    private Long teamId;
    
    @Column(nullable = false)
    private Integer played = 0;
    
    @Column(nullable = false)
    private Integer won = 0;
    
    @Column(nullable = false)
    private Integer drawn = 0;
    
    @Column(nullable = false)
    private Integer lost = 0;
    
    @Column(nullable = false)
    private Integer goalsFor = 0;
    
    @Column(nullable = false)
    private Integer goalsAgainst = 0;
    
    @Column(nullable = false)
    private Integer goalDifference = 0;
    
    @Column(nullable = false)
    private Integer points = 0;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
