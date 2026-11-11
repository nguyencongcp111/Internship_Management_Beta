package com.example.internshipmanager.models.entity;

<<<<<<< HEAD
import com.fasterxml.jackson.annotation.JsonIgnore;
=======
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
import jakarta.persistence.*;
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
@Table(name = "assessment_round")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AssessmentRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roundId;

    @Column(nullable = false)
    private String roundName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String description;

    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "phase_id")
    private InternshipPhase internshipPhase;

    @OneToMany(mappedBy = "assessmentRound")
<<<<<<< HEAD
    @JsonIgnore
    private List<RoundCriteria> roundCriteria;

    @OneToMany(mappedBy = "assessmentRound")
    @JsonIgnore
=======
    private List<RoundCriteria> roundCriteria;

    @OneToMany(mappedBy = "assessmentRound")
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
    private List<AssessmentResult> assessmentResult;
}
