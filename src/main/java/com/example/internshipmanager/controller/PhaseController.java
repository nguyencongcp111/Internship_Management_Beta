package com.example.internshipmanager.controller;

import com.example.internshipmanager.models.dto.request.admin.phase.CreatePhaseRequest;
import com.example.internshipmanager.models.dto.request.admin.phase.EditPhaseRequest;
import com.example.internshipmanager.models.dto.response.DataResponse;
import com.example.internshipmanager.models.entity.InternshipPhase;
import com.example.internshipmanager.service.PhaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
=======
import org.springframework.cglib.core.Local;
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internship_phases")
public class PhaseController {
    private final PhaseService phaseService;

    @GetMapping
    public ResponseEntity<DataResponse<List<InternshipPhase>>> getInternshipPhases() {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
<<<<<<< HEAD
                        "Tìm thấy " + phaseService.getInternshipPhases().size() + " giai đoạn",
=======
                        "Tìm thấy " + phaseService.getInternshipPhases() + " giai đoạn",
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
                        phaseService.getInternshipPhases(),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<InternshipPhase>> getInternshipPhaseById(
            @PathVariable Integer id
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy id: " + id,
                        phaseService.getInternshipPhasesById(id),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DataResponse<InternshipPhase>> createPhase (
            @Valid @RequestBody CreatePhaseRequest createPhaseRequest
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã tạo giai đoạn " + createPhaseRequest.getPhaseName() + " thành công",
                        phaseService.createPhase(createPhaseRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<InternshipPhase>> editPhase (
            @PathVariable Integer id,
            @Valid @RequestBody EditPhaseRequest editPhaseRequest
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã sửa giai đoạn",
                        phaseService.editPhase(id, editPhaseRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<InternshipPhase>> deletePhase (
            @PathVariable Integer id
    ) {
        phaseService.deletePhase(id);

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã xóa giai đoạn " +id,
                        null,
                        null,
                        LocalDateTime.now()
                ), HttpStatus.NO_CONTENT
        );
    }
}
