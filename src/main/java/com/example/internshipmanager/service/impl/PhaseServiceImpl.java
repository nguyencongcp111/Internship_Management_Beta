package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceNotFoundException;
import com.example.internshipmanager.models.dto.request.admin.phase.CreatePhaseRequest;
import com.example.internshipmanager.models.dto.request.admin.phase.EditPhaseRequest;
import com.example.internshipmanager.models.entity.InternshipPhase;
import com.example.internshipmanager.repository.PhaseRepository;
import com.example.internshipmanager.service.PhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhaseServiceImpl implements PhaseService {
    private final PhaseRepository phaseRepository;

    @Override
    public List<InternshipPhase> getInternshipPhases() {
        return phaseRepository.findAll();
    }

    @Override
    public InternshipPhase getInternshipPhasesById(Integer id) {
        return phaseRepository.findById(id)

                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));
    }

    @Override
    public InternshipPhase createPhase (CreatePhaseRequest createPhaseRequest) {
        if (createPhaseRequest.getStartDate().isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ngày bắt đầu không được ở quá khứ"
            );
        }

        InternshipPhase phase = InternshipPhase.builder()
                .phaseName(createPhaseRequest.getPhaseName())
                .description(createPhaseRequest.getDescription())
                .startDate(createPhaseRequest.getStartDate())
                .endDate(createPhaseRequest.getEndDate())
                .build();

        return phaseRepository.save(phase);
    }

    @Override
    public InternshipPhase editPhase (Integer id, EditPhaseRequest editPhaseRequest) {
        InternshipPhase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        phase.setPhaseName(editPhaseRequest.getPhaseName());
        phase.setStartDate(editPhaseRequest.getStartDate());
        phase.setEndDate(editPhaseRequest.getEndDate());
        phase.setDescription(editPhaseRequest.getDescription());

        return phaseRepository.save(phase);

    }

    @Override
    public void deletePhase (Integer id) {
        InternshipPhase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        phaseRepository.delete(phase);
    }
}
