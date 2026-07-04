package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.admin.roundcriteria.CreateRoundCriteriaRequest;
import com.example.internshipmanager.models.dto.request.admin.roundcriteria.EditRoundCriteriaRequest;
import com.example.internshipmanager.models.entity.RoundCriteria;

import java.util.List;

public interface RoundCriteriaService {
    List<RoundCriteria> getRoundCriteriaList();

    RoundCriteria getRoundCriteriaById(Integer id);

    RoundCriteria addRoundCriteria(CreateRoundCriteriaRequest createRoundCriteriaRequest);

    RoundCriteria editRoundCriteria(Integer id, EditRoundCriteriaRequest editRoundCriteriaRequest);

    void deleteRoundCriteria(Integer id);
}
