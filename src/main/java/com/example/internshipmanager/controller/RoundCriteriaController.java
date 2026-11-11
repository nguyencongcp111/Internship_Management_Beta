package com.example.internshipmanager.controller;

import com.example.internshipmanager.models.dto.request.admin.roundcriteria.CreateRoundCriteriaRequest;
import com.example.internshipmanager.models.dto.request.admin.roundcriteria.EditRoundCriteriaRequest;
import com.example.internshipmanager.models.dto.response.DataResponse;
import com.example.internshipmanager.models.entity.RoundCriteria;
import com.example.internshipmanager.service.RoundCriteriaService;
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
@RequestMapping("/api/round_criteria")
public class RoundCriteriaController {
    private final RoundCriteriaService roundCriteriaService;

    @GetMapping
<<<<<<< HEAD
    public ResponseEntity<DataResponse<List<RoundCriteria>>> getRoundCriteriaList(){
=======
    private ResponseEntity<DataResponse<List<RoundCriteria>>> getRoundCriteriaList(){
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy tiêu chí",
                        roundCriteriaService.getRoundCriteriaList(),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
<<<<<<< HEAD
    public ResponseEntity<DataResponse<RoundCriteria>> getRoundCriteriaById(
=======
    private ResponseEntity<DataResponse<RoundCriteria>> getRoundCriteriaById(
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
            @PathVariable Integer id
    ){
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy tiêu chí",
                        roundCriteriaService.getRoundCriteriaById(id),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
<<<<<<< HEAD
    public ResponseEntity<DataResponse<RoundCriteria>> addRoundCriteria(
=======
    private ResponseEntity<DataResponse<RoundCriteria>> addRoundCriteria(
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
            @Valid @RequestBody CreateRoundCriteriaRequest createRoundCriteriaRequest
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã tạo tiêu chí",
                        roundCriteriaService.addRoundCriteria(createRoundCriteriaRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
<<<<<<< HEAD
    public ResponseEntity<DataResponse<RoundCriteria>> editRoundCriteria(
=======
    private ResponseEntity<DataResponse<RoundCriteria>> editRoundCriteria(
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
            @PathVariable Integer id,
            @Valid @RequestBody EditRoundCriteriaRequest editRoundCriteriaRequest
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã sửa tiêu chí",
                        roundCriteriaService.editRoundCriteria(id, editRoundCriteriaRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
<<<<<<< HEAD
    public ResponseEntity<DataResponse<RoundCriteria>> deleteRoundCriteria(
=======
    private ResponseEntity<DataResponse<RoundCriteria>> deleteRoundCriteria(
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
            @PathVariable Integer id
    ) {
        roundCriteriaService.deleteRoundCriteria(id);

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã sửa tiêu chí",
                        null,
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }
}
