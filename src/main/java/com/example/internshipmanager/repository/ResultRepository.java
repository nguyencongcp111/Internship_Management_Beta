package com.example.internshipmanager.repository;

import com.example.internshipmanager.models.entity.AssessmentResult;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository <AssessmentResult, Integer> {
    @EntityGraph(attributePaths = {"internshipAssignment.student.user", "internshipAssignment.mentor", "assessmentRound", "evaluationCriteria", "evaluatedBy"})
    List<AssessmentResult> findAll();

    @EntityGraph(attributePaths = {"internshipAssignment.student.user", "internshipAssignment.mentor", "assessmentRound", "evaluationCriteria", "evaluatedBy"})
    List<AssessmentResult> findByEvaluatedBy_Id(Integer evaluatedById);

    @EntityGraph(attributePaths = {"internshipAssignment.student.user", "internshipAssignment.mentor", "assessmentRound", "evaluationCriteria", "evaluatedBy"})
    List<AssessmentResult> findByInternshipAssignment_Student_StudentId(Integer studentId);
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository <AssessmentResult, Integer> {

>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
}
