package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.admin.round.CreateRoundRequest;
import com.example.internshipmanager.models.dto.request.admin.round.EditRoundRequest;
import com.example.internshipmanager.models.entity.AssessmentRound;

import java.util.List;

public interface RoundService {
    List<AssessmentRound> getAssessmentRounds();

    AssessmentRound getAssessmentRoundById(Integer id);

    AssessmentRound addRound(CreateRoundRequest createRoundRequest);

    AssessmentRound updateRound(Integer id, EditRoundRequest editRoundRequest);

    void deleteRound(Integer id);
}
