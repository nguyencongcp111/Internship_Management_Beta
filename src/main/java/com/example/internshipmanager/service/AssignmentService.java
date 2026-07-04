package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.admin.assignment.CreateAssignmentRequest;
import com.example.internshipmanager.models.dto.request.admin.assignment.EditAssignmentRequest;
import com.example.internshipmanager.models.entity.InternshipAssignment;

import java.util.List;
import java.util.Optional;

public interface AssignmentService {

    List<InternshipAssignment> getInternshipAssignmentList(Integer id);

    Optional<InternshipAssignment> getInternshipAssignmentById(Integer id, Integer userId);

    InternshipAssignment addAssignment(CreateAssignmentRequest createAssignmentRequest);

    InternshipAssignment editAssignment(Integer id, EditAssignmentRequest editAssignmentRequest);
}
