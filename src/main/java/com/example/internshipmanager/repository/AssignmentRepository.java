package com.example.internshipmanager.repository;

import com.example.internshipmanager.models.entity.InternshipAssignment;
import com.example.internshipmanager.models.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<InternshipAssignment, Integer> {
    @EntityGraph(attributePaths = {"student", "mentor", "internshipPhase"})
    List<InternshipAssignment> findAll();

    @EntityGraph(attributePaths = {"student", "mentor", "internshipPhase"})
    List<InternshipAssignment> getInternshipAssignmentsByMentor_MentorId(Integer mentorMentorId);

    @EntityGraph(attributePaths = {"student", "mentor", "internshipPhase"})
    List<InternshipAssignment> getInternshipAssignmentByStudent_StudentId(Integer studentStudentId);

    Optional<InternshipAssignment> findByAssignmentIdAndMentor_MentorId(Integer assignmentId, Integer mentorMentorId);

    Optional<InternshipAssignment> findByAssignmentIdAndStudent_StudentId(Integer assignmentId, Integer studentStudentId);
}
