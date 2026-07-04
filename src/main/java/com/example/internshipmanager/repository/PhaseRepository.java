package com.example.internshipmanager.repository;

import com.example.internshipmanager.models.entity.InternshipPhase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhaseRepository extends JpaRepository<InternshipPhase, Integer> {
    boolean existsByPhaseId(Integer phaseId);
}
