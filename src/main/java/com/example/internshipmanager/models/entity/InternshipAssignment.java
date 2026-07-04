package com.example.internshipmanager.models.entity;

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
@Table(name = "internship_assignment")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InternshipAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer assignmentId;

    private LocalDateTime assignmentTime;

    private Status status = Status.PENDING;

    @CreationTimestamp
    @Column(name = "created_at" ,nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at" ,nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "student_id", unique = true)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "phase_id", unique = true)
    private InternshipPhase internshipPhase;

    @OneToMany(mappedBy = "internshipAssignment")
    private List<AssessmentResult> assessmentResult;


}
