package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.admin.criteria.CreateCriteriaRequest;
import com.example.internshipmanager.models.dto.request.admin.criteria.EditCriteriaRequest;
import com.example.internshipmanager.models.entity.EvaluationCriteria;

import java.util.List;

public interface CriteriaService {
    List<EvaluationCriteria> getCriteria();

    EvaluationCriteria getCriteriaById(Integer id);

    EvaluationCriteria createCriteria(CreateCriteriaRequest createCriteriaRequest);

    EvaluationCriteria editCriteria(Integer id, EditCriteriaRequest editCriteriaRequest);

    void deleteCriteria(Integer id);
}
