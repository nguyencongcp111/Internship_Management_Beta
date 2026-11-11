package com.example.internshipmanager.controller;

import com.example.internshipmanager.models.dto.request.admin.criteria.CreateCriteriaRequest;
import com.example.internshipmanager.models.dto.request.admin.criteria.EditCriteriaRequest;
import com.example.internshipmanager.models.dto.response.DataResponse;
import com.example.internshipmanager.models.entity.EvaluationCriteria;
import com.example.internshipmanager.service.CriteriaService;
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
@RequestMapping("/api/evaluation_criteria")
public class CriteriaController {
    private final CriteriaService criteriaService;

    @GetMapping
    public ResponseEntity<DataResponse<List<EvaluationCriteria>>> getCriteria() {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
<<<<<<< HEAD
                        "Tìm thấy " + criteriaService.getCriteria().size() + " tiêu chí",
=======
                        "Tìm thấy " + criteriaService.getCriteria() + " tiêu chí",
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
                        criteriaService.getCriteria(),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<EvaluationCriteria>> getCriteriaById(
            @PathVariable Integer id
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy tiêu chí " + id,
                        criteriaService.getCriteriaById(id),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DataResponse<EvaluationCriteria>> addCriteria(
            @Valid @RequestBody CreateCriteriaRequest createCriteriaRequest)
    {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã tạo tiêu chí " + createCriteriaRequest.getCriterionName(),
                        criteriaService.createCriteria(createCriteriaRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<EvaluationCriteria>> editCriteria(
            @PathVariable Integer id,
            @Valid @RequestBody EditCriteriaRequest editCriteriaRequest)
    {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã sửa tiêu chí " + editCriteriaRequest.getCriterionName(),
                        criteriaService.editCriteria(id,editCriteriaRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse<EvaluationCriteria>> deleteCriteria(
            @PathVariable Integer id
    ) {
        criteriaService.deleteCriteria(id);

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã xóa tiêu chí " + id,
                        null,
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }
}
