package com.example.internshipmanager.controller;

import com.example.internshipmanager.models.dto.request.admin.mentor.AdminEditMentorRequest;
import com.example.internshipmanager.models.dto.request.admin.mentor.CreateMentorRequest;
import com.example.internshipmanager.models.dto.request.general.EditMentorRequest;
import com.example.internshipmanager.models.dto.response.DataResponse;
import com.example.internshipmanager.models.entity.Mentor;
import com.example.internshipmanager.security.principal.UserPrincipal;
import com.example.internshipmanager.service.MentorService;
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
@RequestMapping("/api/mentor")
@RequiredArgsConstructor
public class MentorController {
    private final MentorService mentorService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse<List<Mentor>>> getAllUser() {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy " + mentorService.getAllMentor().size() + " mentor",
                        mentorService.getAllMentor(),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Mentor>> getMentorById(@PathVariable Integer id) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy Mentor có id " + id,
                        mentorService.getMentorById(id),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<DataResponse<Mentor>> getMyProfileMentor(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Integer userId = userPrincipal.getUser().getId();

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Hồ sơ của bạn",
                        mentorService.getMentorByUserId(userId),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<DataResponse<Mentor>> createUser (
            @Valid @RequestBody CreateMentorRequest createMentorRequest) {

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã tạo người dùng",
                        mentorService.addMentor(createMentorRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse<Mentor>> editUserAdmin (
            @PathVariable Integer id, @Valid @RequestBody AdminEditMentorRequest adminEditMentorRequest) {

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã cạp nhật người dùng",
                        mentorService.editMentorAdmin(id, adminEditMentorRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PutMapping("/my-profile")
    @PreAuthorize("hasRole('MENTOR')")
    public ResponseEntity<DataResponse<Mentor>> editUser (
            @AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody EditMentorRequest editMentorRequest) {

        Integer userId = userPrincipal.getUser().getId();

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã sửa tên của bạn",
                        mentorService.editMentor(userId, editMentorRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }
}
