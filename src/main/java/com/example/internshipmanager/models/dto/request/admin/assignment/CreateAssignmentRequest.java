package com.example.internshipmanager.models.dto.request.admin.assignment;

import com.example.internshipmanager.models.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAssignmentRequest {
    private Status status = Status.PENDING;

    @NotNull(message = "Không được để trống Student Id")
    private Integer studentId;

    @NotNull(message = "Không được để trống Mentor Id")
    private Integer mentorId;

    @NotNull(message = "Không được để trống Phase Id")
    private Integer phaseId;
}
