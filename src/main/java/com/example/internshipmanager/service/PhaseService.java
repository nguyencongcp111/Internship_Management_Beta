package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.admin.phase.CreatePhaseRequest;
import com.example.internshipmanager.models.dto.request.admin.phase.EditPhaseRequest;
import com.example.internshipmanager.models.entity.InternshipPhase;

import java.util.List;

public interface PhaseService {
    List<InternshipPhase> getInternshipPhases();

    InternshipPhase getInternshipPhasesById(Integer id);

    InternshipPhase createPhase (CreatePhaseRequest createPhaseRequest);

    InternshipPhase editPhase (Integer id, EditPhaseRequest editPhaseRequest);

    void deletePhase(Integer id);


}
