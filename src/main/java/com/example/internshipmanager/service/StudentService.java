package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.admin.student.AdminEditStudentRequest;
import com.example.internshipmanager.models.dto.request.admin.student.CreateStudentRequest;
import com.example.internshipmanager.models.dto.request.general.EditStudentRequest;
import com.example.internshipmanager.models.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudent();

    Student getStudentById(Integer id);

    Student addStudent(CreateStudentRequest createStudentRequest);

    Student editStudent(Integer id, EditStudentRequest editStudentRequest);

    Student editStudentAdmin(Integer id, AdminEditStudentRequest adminEditStudentRequest);

    Student getStudentByUserId(Integer id);
}
