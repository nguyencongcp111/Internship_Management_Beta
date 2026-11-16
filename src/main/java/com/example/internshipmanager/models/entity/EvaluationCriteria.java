package com.example.internshipmanager.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table (name = "evaluation_criteria")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EvaluationCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer criterionId;

    @Column(length = 200, nullable = false)
    private String criterionName;

    private String description;

    @Column(nullable = false)
    private double maxScore;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "evaluationCriteria")
    @JsonIgnore
    private List<RoundCriteria> roundCriteria;

    @OneToMany(mappedBy = "evaluationCriteria")
    @JsonIgnore
    private List<AssessmentResult> assessmentResult;
}
