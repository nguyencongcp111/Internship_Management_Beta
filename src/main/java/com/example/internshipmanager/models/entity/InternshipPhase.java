package com.example.internshipmanager.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "internship_phases")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InternshipPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer phaseId;
    @Column(length = 100, nullable = false, unique = true)
    private String phaseName;
    @Column(nullable = false)
    private LocalDate startDate;
    @Column(nullable = false)
    private LocalDate endDate;

    private String description;

    @CreationTimestamp
    @Column(name = "created_at" ,nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    @Column(name = "updated_at" ,nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "internshipPhase")
    @JsonIgnore
    private List<AssessmentRound> assessmentRound;

    @OneToMany(mappedBy = "internshipPhase")
    @JsonIgnore
    private List<InternshipAssignment> internshipAssignments;
}
