package com.example.internshipmanager.repository;

import com.example.internshipmanager.models.entity.InternshipAssignment;
import com.example.internshipmanager.models.entity.User;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.EntityGraph;
=======
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<InternshipAssignment, Integer> {
<<<<<<< HEAD
    @EntityGraph(attributePaths = {"student", "mentor", "internshipPhase"})
    List<InternshipAssignment> findAll();

    @EntityGraph(attributePaths = {"student", "mentor", "internshipPhase"})
    List<InternshipAssignment> getInternshipAssignmentsByMentor_MentorId(Integer mentorMentorId);

    @EntityGraph(attributePaths = {"student", "mentor", "internshipPhase"})
=======
    List<InternshipAssignment> getInternshipAssignmentsByMentor_MentorId(Integer mentorMentorId);

>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
    List<InternshipAssignment> getInternshipAssignmentByStudent_StudentId(Integer studentStudentId);

    Optional<InternshipAssignment> findByAssignmentIdAndMentor_MentorId(Integer assignmentId, Integer mentorMentorId);

    Optional<InternshipAssignment> findByAssignmentIdAndStudent_StudentId(Integer assignmentId, Integer studentStudentId);
}
