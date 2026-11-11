package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceNotFoundException;
<<<<<<< HEAD
import com.example.internshipmanager.exception.UnexpectedRoleException;
import com.example.internshipmanager.models.dto.request.admin.result.EditResultRequest;
=======
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
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
<<<<<<< HEAD
    private final StudentRepository studentRepository;

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
=======
    private final UserRepository userRepository;

    @Override
    public List<AssessmentResult> getAssessmentResult() {
        return resultRepository.findAll();
    }

    @Override
    public AssessmentResult createResult(ResultRequest createResultRequest) {
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
        InternshipAssignment assignment = assignmentRepository.findById(
                createResultRequest.getAssignmentId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

<<<<<<< HEAD
        if (assignment.getMentor() == null
                || !assignment.getMentor().getUser().getId().equals(currentUser.getId())) {
            throw new UnexpectedRoleException("Bạn không được phân công cho sinh viên này");
        }

=======
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
        AssessmentRound round = roundRepository.findById(
                createResultRequest.getRoundId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        EvaluationCriteria criterion = criteriaRepository.findById(
                createResultRequest.getCriterionId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

<<<<<<< HEAD
=======
        User mentor = userRepository.findById(
                createResultRequest.getEvaluatedBy()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        if (mentor.getRole() != Roles.MENTOR) {
            throw new ResourceNotFoundException("Không phải mentor");
        }

>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
        AssessmentResult result = AssessmentResult.builder()
                .internshipAssignment(assignment)
                .assessmentRound(round)
                .evaluationCriteria(criterion)
                .score(createResultRequest.getScore())
                .comments(createResultRequest.getComments())
<<<<<<< HEAD
                .evaluatedBy(currentUser)
=======
                .evaluatedBy(mentor)
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
                .evaluationDate(createResultRequest.getEvaluationDate())
                .build();

        return resultRepository.save(result);
    }

    @Override
<<<<<<< HEAD
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
    public AssessmentResult updateResult(Integer id, EditResultRequest editResultRequest, User currentUser) {
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
=======
    public AssessmentResult getAssessmentResultById(Integer id) {
        return resultRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
    }
}
