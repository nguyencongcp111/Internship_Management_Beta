package com.example.internshipmanager.models.dto.request.admin.mentor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateMentorRequest {
    private String department;
    private String academicRank;
    private Integer userId;
}
