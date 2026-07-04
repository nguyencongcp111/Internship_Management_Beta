package com.example.internshipmanager.controller;

import com.example.internshipmanager.models.dto.request.admin.assignment.CreateAssignmentRequest;
import com.example.internshipmanager.models.dto.request.admin.assignment.EditAssignmentRequest;
import com.example.internshipmanager.models.dto.response.DataResponse;
import com.example.internshipmanager.models.entity.InternshipAssignment;
import com.example.internshipmanager.security.principal.UserPrincipal;
import com.example.internshipmanager.service.AssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internship_assignment")
public class AssignmentController {
    private final AssignmentService assignmentService;

    @GetMapping
    public ResponseEntity<DataResponse<List<InternshipAssignment>>> getInternshipAssignmentList(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Integer userId = userPrincipal.getUser().getId();

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy " + assignmentService.getInternshipAssignmentList(userId).size(),
                        assignmentService.getInternshipAssignmentList(userId),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Optional<InternshipAssignment>>> getInternshipAssignmentById(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Integer userId = userPrincipal.getUser().getId();

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy id " + id,
                        assignmentService.getInternshipAssignmentById(id, userId),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DataResponse<InternshipAssignment>> addAssignment(
            @Valid  @RequestBody CreateAssignmentRequest createAssignmentRequest
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã tạo",
                        assignmentService.addAssignment(createAssignmentRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.CREATED
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DataResponse<InternshipAssignment>> editAssignment(
            @PathVariable Integer id,
            @Valid @RequestBody EditAssignmentRequest editAssignmentRequest
    ) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã sửa",
                        assignmentService.editAssignment(id, editAssignmentRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }
}
