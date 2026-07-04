package com.example.internshipmanager.models.dto.request.admin.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResultRequest {
    @NotNull(message = "Không được để trống id assignment")
    private Integer assignmentId;

    @NotNull(message = "Không được để trống Id round")
    private Integer roundId;

    @NotNull(message = "Không được để trống id tiêu chí")
    private Integer criterionId;

    @NotNull(message = "Không được để trống điểm")
    private double score;

    private String comments;

    @NotNull(message = "Không được để trống ngày đánh giá")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime evaluationDate;


}
