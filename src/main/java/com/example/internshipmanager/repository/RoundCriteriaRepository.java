package com.example.internshipmanager.repository;

import com.example.internshipmanager.models.entity.RoundCriteria;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundCriteriaRepository extends JpaRepository<RoundCriteria, Integer> {
    @EntityGraph(attributePaths = {"assessmentRound", "evaluationCriteria"})
    List<RoundCriteria> findAll();
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundCriteriaRepository extends JpaRepository<RoundCriteria, Integer> {
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
}
