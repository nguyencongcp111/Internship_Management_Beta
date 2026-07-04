package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceFoundException;
import com.example.internshipmanager.exception.ResourceNotFoundException;
import com.example.internshipmanager.models.dto.request.admin.criteria.CreateCriteriaRequest;
import com.example.internshipmanager.models.dto.request.admin.criteria.EditCriteriaRequest;
import com.example.internshipmanager.models.entity.EvaluationCriteria;
import com.example.internshipmanager.repository.CriteriaRepository;
import com.example.internshipmanager.service.CriteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CriteriaServiceImpl implements CriteriaService {
    private final CriteriaRepository criteriaRepository;

    @Override
    public List<EvaluationCriteria> getCriteria() {
        return criteriaRepository.findAll();
    }

    public EvaluationCriteria getCriteriaById(Integer id) {
        return criteriaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy id")
        );
    }

    public EvaluationCriteria createCriteria(CreateCriteriaRequest createCriteriaRequest) {

        boolean find = criteriaRepository.findEvaluationCriteriaByCriterionName(createCriteriaRequest.getCriterionName());

        if (find) {
            throw new ResourceFoundException("Tồn tại tiêu chí có tên này!");
        }

        EvaluationCriteria criteria = EvaluationCriteria.builder()
                .criterionName(createCriteriaRequest.getCriterionName())
                .description(createCriteriaRequest.getDescription())
                .maxScore(createCriteriaRequest.getMaxScore())
                .build();

        return criteriaRepository.save(criteria);
    }

    public EvaluationCriteria editCriteria(Integer id, EditCriteriaRequest editCriteriaRequest) {
        EvaluationCriteria criteria = criteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        criteria.setCriterionName(editCriteriaRequest.getCriterionName());
        criteria.setDescription(editCriteriaRequest.getDescription());
        criteria.setMaxScore(editCriteriaRequest.getMaxScore());

        return criteriaRepository.save(criteria);
    }

    public void deleteCriteria(Integer id) {
        EvaluationCriteria criteria = criteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        criteriaRepository.delete(criteria);
    }
}
