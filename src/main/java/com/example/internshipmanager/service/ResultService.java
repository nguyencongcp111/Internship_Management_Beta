package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.admin.result.EditResultRequest;
import com.example.internshipmanager.models.dto.request.admin.result.ResultRequest;
import com.example.internshipmanager.models.entity.AssessmentResult;
import com.example.internshipmanager.models.entity.User;
import java.util.List;

public interface ResultService {
    List<AssessmentResult> getAssessmentResult(User currentUser, Integer assignmentId, Integer userId);

    AssessmentResult createResult(ResultRequest createResultRequest, User currentUser);

    AssessmentResult getAssessmentResultById(Integer id, User currentUser);

    AssessmentResult updateResult(Integer id, EditResultRequest editResultRequest, User currentUser);

}
