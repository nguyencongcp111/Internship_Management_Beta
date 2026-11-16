package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceNotFoundException;
import com.example.internshipmanager.models.dto.request.admin.assignment.CreateAssignmentRequest;
import com.example.internshipmanager.models.dto.request.admin.assignment.EditAssignmentRequest;
import com.example.internshipmanager.models.entity.*;
import com.example.internshipmanager.repository.*;
import com.example.internshipmanager.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    private final PhaseRepository phaseRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<InternshipAssignment> getInternshipAssignmentList(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        if (user.getRole() == Roles.MENTOR) {
            Mentor mentor = mentorRepository.findMentorByUser(user);
            if (mentor == null) {
                throw new ResourceNotFoundException("Không tìm thấy mentor");
            }
            return assignmentRepository.getInternshipAssignmentsByMentor_MentorId(mentor.getMentorId());
        }

        if (user.getRole() == Roles.STUDENT) {
            Student student = studentRepository.findStudentByUser(user);
            if (student == null) {
                throw new ResourceNotFoundException("Không tìm thấy student");
            }
            return assignmentRepository.getInternshipAssignmentByStudent_StudentId(student.getStudentId());
        }

        if (user.getRole() == Roles.ADMIN) {
            return assignmentRepository.findAll();
        }

        throw new IllegalArgumentException("Role không hợp lệ");
    }

    @Override
    public Optional<InternshipAssignment> getInternshipAssignmentById(Integer id, Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        if (user.getRole() == Roles.MENTOR) {
            Mentor mentor = mentorRepository.findMentorByUser(user);
            return Optional.of(assignmentRepository.findByAssignmentIdAndMentor_MentorId(id, mentor.getMentorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mentor")));
        }

        if (user.getRole() == Roles.STUDENT) {
            Student student = studentRepository.findStudentByUser(user);
            return Optional.of(assignmentRepository.findByAssignmentIdAndStudent_StudentId(id, student.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sinh viên")));
        }

        if (user.getRole() == Roles.ADMIN) {
            return Optional.of(assignmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy id")));
        }

        throw new IllegalArgumentException("Role không hợp lệ");
    }

    @Override
    public InternshipAssignment addAssignment(CreateAssignmentRequest createAssignmentRequest) {
        Student student = studentRepository.findById(
                createAssignmentRequest.getStudentId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        Mentor mentor = mentorRepository.findById(
                createAssignmentRequest.getMentorId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        InternshipPhase phase = phaseRepository.findById(
                createAssignmentRequest.getPhaseId()
        ).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        InternshipAssignment assignment = InternshipAssignment.builder()
                .assignmentTime(createAssignmentRequest.getAssignmentTime())
                .status(createAssignmentRequest.getStatus())
                .student(student)
                .mentor(mentor)
                .internshipPhase(phase)
                .build();

        return  assignmentRepository.save(assignment);
    }

    public InternshipAssignment editAssignment(Integer id, EditAssignmentRequest editAssignmentRequest){
        InternshipAssignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy id"));

        if (assignment.getStatus() == Status.COMPLETED) {
            throw new IllegalArgumentException("Khóa học đã hoàn thành, không thể cập nhật");
        }

        assignment.setStatus(editAssignmentRequest.getStatus());
        return assignmentRepository.save(assignment);
    }
}
