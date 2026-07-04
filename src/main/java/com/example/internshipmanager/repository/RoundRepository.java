package com.example.internshipmanager.repository;

import com.example.internshipmanager.models.entity.AssessmentRound;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoundRepository extends JpaRepository<AssessmentRound, Integer> {
    @EntityGraph(attributePaths = "internshipPhase")
    List<AssessmentRound> findAll();
}
