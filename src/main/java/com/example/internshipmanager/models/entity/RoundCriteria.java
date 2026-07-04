package com.example.internshipmanager.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "round_criteria")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RoundCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roundCriterionId;

    @Column(nullable = false)
    // @NotNull(message = "Không được để trống trọng số")
    // @Min(value = 0, message = "Trọng số không được thấp hơn 0")
    private double weight;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "round_id")
    private AssessmentRound assessmentRound;

    @ManyToOne
    @JoinColumn(name = "criteria_id")
    private EvaluationCriteria evaluationCriteria;
}
