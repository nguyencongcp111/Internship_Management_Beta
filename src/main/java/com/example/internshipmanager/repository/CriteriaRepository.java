package com.example.internshipmanager.repository;

import com.example.internshipmanager.models.entity.EvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CriteriaRepository extends JpaRepository<EvaluationCriteria, Integer> {
    boolean existsByCriterionName(String criterionName);
}
