package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceNotFoundException;
import com.example.internshipmanager.models.dto.request.admin.round.CreateRoundRequest;
import com.example.internshipmanager.models.dto.request.admin.round.EditRoundRequest;
import com.example.internshipmanager.models.entity.AssessmentRound;
import com.example.internshipmanager.models.entity.InternshipPhase;
import com.example.internshipmanager.repository.PhaseRepository;
import com.example.internshipmanager.repository.RoundRepository;
import com.example.internshipmanager.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {
    private final RoundRepository roundRepository;
    private final PhaseRepository phaseRepository;

    public List<AssessmentRound> getAssessmentRounds() {
        return roundRepository.findAll();
    }

    public AssessmentRound getAssessmentRoundById(Integer id) {
        return roundRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Không tìm thấy id"));
    }

    public AssessmentRound addRound(CreateRoundRequest createRoundRequest) {
        InternshipPhase phase = phaseRepository.findById(
                createRoundRequest.getPhaseId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        AssessmentRound round = AssessmentRound.builder()
                .roundName(createRoundRequest.getRoundName())
                .internshipPhase(phase)
                .startDate(createRoundRequest.getStartDate())
                .endDate(createRoundRequest.getEndDate())
                .description(createRoundRequest.getDescription())
                .build();

        return roundRepository.save(round);
    }

    public AssessmentRound updateRound(Integer id, EditRoundRequest editRoundRequest) {
        AssessmentRound round = roundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        InternshipPhase phase = phaseRepository.findById(
                editRoundRequest.getPhaseId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        round.setRoundName(editRoundRequest.getRoundName());
        round.setInternshipPhase(phase);
        round.setStartDate(editRoundRequest.getStartDate());
        round.setEndDate(editRoundRequest.getEndDate());
        round.setDescription(editRoundRequest.getDescription());

        return roundRepository.save(round);

    }

    public void deleteRound(Integer id) {
        AssessmentRound round = roundRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        roundRepository.delete(round);
    }
}
