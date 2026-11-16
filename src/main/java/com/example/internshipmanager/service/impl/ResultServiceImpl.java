package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceNotFoundException;
import com.example.internshipmanager.exception.UnexpectedRoleException;
import com.example.internshipmanager.models.dto.request.admin.result.EditResultRequest;
import com.example.internshipmanager.models.dto.request.admin.result.ResultRequest;
import com.example.internshipmanager.models.entity.*;
import com.example.internshipmanager.repository.*;
import com.example.internshipmanager.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;
    private final AssignmentRepository assignmentRepository;
    private final RoundRepository roundRepository;
    private final CriteriaRepository criteriaRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;


    @Override
    public List<AssessmentResult> getAssessmentResult(User currentUser, Integer assignmentId, Integer userId) {
        List<AssessmentResult> results;

        if (currentUser.getRole() == Roles.ADMIN) {
            results = resultRepository.findAll();
        } else if (currentUser.getRole() == Roles.MENTOR) {
            results = resultRepository.findByEvaluatedBy_Id(currentUser.getId());
        } else if (currentUser.getRole() == Roles.STUDENT) {
            Student student = studentRepository.findStudentByUser(currentUser);

            if (student == null) {
                throw new ResourceNotFoundException("Không tìm thấy sinh viên");
            }

            results = resultRepository.findByInternshipAssignment_Student_StudentId(student.getStudentId());
        } else {
            throw new IllegalArgumentException("Role không hợp lệ");
        }

        if (assignmentId != null) {
            results = results.stream()
                    .filter(result -> result.getInternshipAssignment() != null
                            && assignmentId.equals(result.getInternshipAssignment().getAssignmentId()))
                    .toList();
        }

        if (userId != null) {
            results = results.stream()
                    .filter(result -> result.getInternshipAssignment() != null
                            && result.getInternshipAssignment().getStudent() != null
                            && result.getInternshipAssignment().getStudent().getUser() != null
                            && userId.equals(result.getInternshipAssignment().getStudent().getUser().getId()))
                    .toList();
        }

        return results;
    }

    @Override
    public AssessmentResult createResult(ResultRequest createResultRequest, User currentUser) {
        InternshipAssignment assignment = assignmentRepository.findById(
                createResultRequest.getAssignmentId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        if (assignment.getMentor() == null
                || !assignment.getMentor().getUser().getId().equals(currentUser.getId())) {
            throw new UnexpectedRoleException("Bạn không được phân công cho sinh viên này");
        }

        AssessmentRound round = roundRepository.findById(
                createResultRequest.getRoundId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        EvaluationCriteria criterion = criteriaRepository.findById(
                createResultRequest.getCriterionId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        if (currentUser.getRole() != Roles.MENTOR) {
            throw new UnexpectedRoleException("Chỉ mentor mới được tạo kết quả đánh giá");
        }
        AssessmentResult result = AssessmentResult.builder()
                .internshipAssignment(assignment)
                .assessmentRound(round)
                .evaluationCriteria(criterion)
                .score(createResultRequest.getScore())
                .comments(createResultRequest.getComments())
                .evaluatedBy(currentUser)
                .evaluationDate(createResultRequest.getEvaluationDate())
                .build();

        return resultRepository.save(result);
    }

    @Override
    public AssessmentResult getAssessmentResultById(Integer id, User currentUser) {
        AssessmentResult result = resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        if (currentUser.getRole() == Roles.ADMIN) {
            return result;
        }

        if (currentUser.getRole() == Roles.MENTOR) {
            if (result.getEvaluatedBy() != null
                    && result.getEvaluatedBy().getId().equals(currentUser.getId())) {
                return result;
            }
            throw new ResourceNotFoundException("Không tìm thấy id");
        }

        if (currentUser.getRole() == Roles.STUDENT) {
            Student student = studentRepository.findStudentByUser(currentUser);

            if (student != null
                    && result.getInternshipAssignment() != null
                    && result.getInternshipAssignment().getStudent() != null
                    && result.getInternshipAssignment().getStudent().getStudentId().equals(student.getStudentId())) {
                return result;
            }
            throw new ResourceNotFoundException("Không tìm thấy id");
        }

        throw new IllegalArgumentException("Role không hợp lệ");
    }

    @Override
    public AssessmentResult updateResult(Integer id, EditResultRequest editResultRequest, User currentUser){
            AssessmentResult result = resultRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

            if (result.getEvaluatedBy() == null
                    || !result.getEvaluatedBy().getId().equals(currentUser.getId())) {
                throw new UnexpectedRoleException("Bạn không có quyền sửa kết quả này");
            }

            AssessmentRound round = roundRepository.findById(
                    editResultRequest.getRoundId()
            ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

            EvaluationCriteria criterion = criteriaRepository.findById(
                    editResultRequest.getCriterionId()
            ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

            result.setAssessmentRound(round);
            result.setEvaluationCriteria(criterion);
            result.setScore(editResultRequest.getScore());
            result.setComments(editResultRequest.getComments());
            result.setEvaluationDate(editResultRequest.getEvaluationDate());

            return resultRepository.save(result);
    }
}
