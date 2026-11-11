package com.example.internshipmanager.controller;

import com.example.internshipmanager.models.dto.request.admin.student.AdminEditStudentRequest;
import com.example.internshipmanager.models.dto.request.admin.student.CreateStudentRequest;
import com.example.internshipmanager.models.dto.request.general.EditStudentRequest;
import com.example.internshipmanager.models.dto.response.DataResponse;
import com.example.internshipmanager.models.entity.Student;
import com.example.internshipmanager.security.principal.UserPrincipal;
import com.example.internshipmanager.service.StudentService;
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
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse<List<Student>>> getAllStudent() {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
<<<<<<< HEAD
                        "Tìm thấy " + studentService.getAllStudent().size() + " sinh viên",
=======
                        "Tìm thấy " + studentService.getAllStudent() + " sinh viên",
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
                        studentService.getAllStudent(),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<Student>> getStudentById(@PathVariable Integer id) {
        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy sinh viên có id " + id,
                        studentService.getStudentById(id),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<DataResponse<Student>> getMyProfileStudent(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Integer userId = userPrincipal.getUser().getId();

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Tìm thấy sinh viên có id " + userId,
                        studentService.getStudentByUserId(userId),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<DataResponse<Student>> createUser (
            @Valid @RequestBody CreateStudentRequest createStudentRequest) {

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã tạo người dùng",
                        studentService.addStudent(createStudentRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse<Student>> editUserAdmin (
            @PathVariable Integer id, @Valid @RequestBody AdminEditStudentRequest adminEditStudentRequest) {

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã cập nhật người dùng",
                        studentService.editStudentAdmin(id, adminEditStudentRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }

    @PutMapping("/my-profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<DataResponse<Student>> editUser (
            @AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody EditStudentRequest editStudentRequest) {

        Integer userId = userPrincipal.getUser().getId();

        return new ResponseEntity<>(
                new DataResponse<>(
                        true,
                        "Đã cập nhật người dùng",
                        studentService.editStudent(userId, editStudentRequest),
                        null,
                        LocalDateTime.now()
                ), HttpStatus.OK
        );
    }
}
