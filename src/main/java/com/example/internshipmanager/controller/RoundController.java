package com.example.internshipmanager.controller;

import com.example.internshipmanager.models.dto.request.admin.round.CreateRoundRequest;
import com.example.internshipmanager.models.dto.request.admin.round.EditRoundRequest;
import com.example.internshipmanager.models.dto.response.DataResponse;
import com.example.internshipmanager.models.entity.AssessmentRound;
import com.example.internshipmanager.service.RoundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assessment_rounds")
public class RoundController {
    private final RoundService roundService;

    @GetMapping
    public ResponseEntity<DataResponse<List<AssessmentRound>>> getAssessmentRounds() {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy " + roundService.getAssessmentRounds().size() + " đợt đánh giá",
                        roundService.getAssessmentRounds(),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<AssessmentRound>> getAssessmentRoundById(
            @PathVariable Integer id
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy đợt đánh giá có id " + id,
                        roundService.getAssessmentRoundById(id),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DataResponse<AssessmentRound>> createRound(
            @Valid @RequestBody CreateRoundRequest createRoundRequest
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã tạo đợt đánh giá",
                        roundService.addRound(createRoundRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<AssessmentRound>> editRound(
            @PathVariable Integer id,
            @Valid @RequestBody EditRoundRequest editRoundRequest
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã sửa đợt đánh giá",
                        roundService.updateRound(id, editRoundRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<?>> deleteRound(
            @PathVariable Integer id
    ) {
        roundService.deleteRound(id);

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã xóa đợt đánh giá",
                        null,
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }
}
