package com.example.internshipmanager.controller;

import com.example.internshipmanager.models.dto.request.admin.result.EditResultRequest;
import com.example.internshipmanager.models.dto.request.admin.result.ResultRequest;
import com.example.internshipmanager.models.dto.response.DataResponse;
import com.example.internshipmanager.models.entity.AssessmentResult;
import com.example.internshipmanager.security.principal.UserPrincipal;
import com.example.internshipmanager.service.ResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assessment_results")
public class ResultController {
    private final ResultService resultService;

    @GetMapping
    public ResponseEntity<DataResponse<List<AssessmentResult>>> getAssessmentResult(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) Integer assignmentId,
            @RequestParam(required = false) Integer userId
    ) {
        List<AssessmentResult> results = resultService.getAssessmentResult(
                userPrincipal.getUser(), assignmentId, userId);

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy " + results.size() + " kết quả",
                        results,
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping
    public ResponseEntity<DataResponse<AssessmentResult>> addResult(
            @Valid @RequestBody ResultRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã tạo kết quả",
                        resultService.createResult(request, userPrincipal.getUser()),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<AssessmentResult>> getResultById(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy kết quả có id " + id,
                        resultService.getAssessmentResultById(id, userPrincipal.getUser()),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('MENTOR')")
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<AssessmentResult>> editResult(
            @PathVariable Integer id,
            @Valid @RequestBody EditResultRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã cập nhật kết quả",
                        resultService.updateResult(id, request, userPrincipal.getUser()),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }
}
