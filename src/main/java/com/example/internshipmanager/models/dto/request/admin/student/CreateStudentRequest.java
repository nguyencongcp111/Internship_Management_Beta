package com.example.internshipmanager.models.dto.request.admin.student;

import com.fasterxml.jackson.annotation.JsonFormat;
<<<<<<< HEAD
import jakarta.validation.constraints.NotBlank;
=======
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateStudentRequest {
<<<<<<< HEAD
    @NotBlank(message = "Không được để trống mã sinh viên")
=======
    @NotNull(message = "Không được để trống mã sinh viên")
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
    private String studentCode;

    private String major;

    private String className;
<<<<<<< HEAD

    @NotNull(message = "Không được để trống ngày sinh")
=======
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private String address;

    private Integer userId;
}
