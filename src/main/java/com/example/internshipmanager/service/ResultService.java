package com.example.internshipmanager.service;

<<<<<<< HEAD
import com.example.internshipmanager.models.dto.request.admin.result.EditResultRequest;
import com.example.internshipmanager.models.dto.request.admin.result.ResultRequest;
import com.example.internshipmanager.models.entity.AssessmentResult;
import com.example.internshipmanager.models.entity.User;
=======
import com.example.internshipmanager.models.dto.request.admin.result.ResultRequest;
import com.example.internshipmanager.models.entity.AssessmentResult;
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)

import java.util.List;

public interface ResultService {
<<<<<<< HEAD
    List<AssessmentResult> getAssessmentResult(User currentUser, Integer assignmentId, Integer userId);

    AssessmentResult createResult(ResultRequest createResultRequest, User currentUser);

    AssessmentResult getAssessmentResultById(Integer id, User currentUser);

    AssessmentResult updateResult(Integer id, EditResultRequest editResultRequest, User currentUser);
=======
    List<AssessmentResult> getAssessmentResult();

    AssessmentResult createResult(ResultRequest createResultRequest);

    AssessmentResult getAssessmentResultById(Integer id);
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
}
