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
@Table(name = "assessment_result")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AssessmentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resultId;
    @Column(nullable = false)
    private double score;

    private String comments;

    @Column(nullable = false)
    private LocalDateTime evaluationDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn (name = "assignment_id")
    private InternshipAssignment internshipAssignment;

    @ManyToOne
    @JoinColumn (name = "round_id")
    private AssessmentRound assessmentRound;

    @ManyToOne
    @JoinColumn (name = "criterion_id")
    private EvaluationCriteria evaluationCriteria;

    @ManyToOne
    @JoinColumn (name = "evaluated_by")
    private User evaluatedBy;
}
