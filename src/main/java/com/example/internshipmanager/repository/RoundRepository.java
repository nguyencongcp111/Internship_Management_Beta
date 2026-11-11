package com.example.internshipmanager.repository;

import com.example.internshipmanager.models.entity.AssessmentRound;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<AssessmentRound, Integer> {
    @EntityGraph(attributePaths = "internshipPhase")
    List<AssessmentRound> findAll();
=======
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<AssessmentRound, Integer> {
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
}
