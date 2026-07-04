package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceNotFoundException;
import com.example.internshipmanager.models.dto.request.admin.roundcriteria.CreateRoundCriteriaRequest;
import com.example.internshipmanager.models.dto.request.admin.roundcriteria.EditRoundCriteriaRequest;
import com.example.internshipmanager.models.entity.AssessmentRound;
import com.example.internshipmanager.models.entity.EvaluationCriteria;
import com.example.internshipmanager.models.entity.RoundCriteria;
import com.example.internshipmanager.repository.CriteriaRepository;
import com.example.internshipmanager.repository.RoundCriteriaRepository;
import com.example.internshipmanager.repository.RoundRepository;
import com.example.internshipmanager.service.RoundCriteriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundCriteriaServiceImpl implements RoundCriteriaService {
    private final RoundRepository roundRepository;
    private final CriteriaRepository criteriaRepository;
    private final RoundCriteriaRepository roundCriteriaRepository;

    @Override
    public List<RoundCriteria> getRoundCriteriaList() {
        return roundCriteriaRepository.findAll();
    }

    @Override
    public RoundCriteria getRoundCriteriaById(Integer id) {
        return roundCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));
    }

    @Override
    public RoundCriteria addRoundCriteria(CreateRoundCriteriaRequest createRoundCriteriaRequest) {
        AssessmentRound round = roundRepository.findById(
                createRoundCriteriaRequest.getRoundId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        EvaluationCriteria criteria = criteriaRepository.findById(
                createRoundCriteriaRequest.getCriteriaId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        RoundCriteria rc = RoundCriteria.builder()
                .weight(createRoundCriteriaRequest.getWeight())
                .assessmentRound(round)
                .evaluationCriteria(criteria)
                .build();

        return roundCriteriaRepository.save(rc);
    }

    @Override
    public RoundCriteria editRoundCriteria(Integer id, EditRoundCriteriaRequest editRoundCriteriaRequest){
        RoundCriteria rc = roundCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        AssessmentRound round = roundRepository.findById(
                editRoundCriteriaRequest.getRoundId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        EvaluationCriteria criteria = criteriaRepository.findById(
                editRoundCriteriaRequest.getCriteriaId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        rc.setWeight(editRoundCriteriaRequest.getWeight());
        rc.setAssessmentRound(round);
        rc.setEvaluationCriteria(criteria);

        return roundCriteriaRepository.save(rc);
    }

    @Override
    public void deleteRoundCriteria(Integer id) {
        RoundCriteria rc = roundCriteriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy id"));

        roundCriteriaRepository.delete(rc);
    }
}
