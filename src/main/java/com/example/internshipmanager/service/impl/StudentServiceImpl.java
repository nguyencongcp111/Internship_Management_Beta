package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceFoundException;
import com.example.internshipmanager.exception.ResourceNotFoundException;
import com.example.internshipmanager.models.dto.request.admin.student.AdminEditStudentRequest;
import com.example.internshipmanager.models.dto.request.admin.student.CreateStudentRequest;
import com.example.internshipmanager.models.dto.request.general.EditStudentRequest;
import com.example.internshipmanager.models.entity.Roles;
import com.example.internshipmanager.models.entity.Student;
import com.example.internshipmanager.models.entity.User;
import com.example.internshipmanager.repository.StudentRepository;
import com.example.internshipmanager.repository.UserRepository;
import com.example.internshipmanager.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Override
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Integer id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thể tìm thấy người dùng"));
    }

    @Override
    public Student getStudentByUserId(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thể tìm thấy người dùng"));

        if (user.getRole() != Roles.STUDENT) {
            throw new ResourceNotFoundException("id " + user.getId() + " không phải sinh viên");
        }

        return studentRepository.findStudentByUser(user);
    }

    @Override
    public Student addStudent(CreateStudentRequest createStudentRequest) {
        User user = userRepository.findById(createStudentRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Không thể tìm thấy người dùng"));

        if (user.getRole() != Roles.STUDENT) {
            throw new ResourceNotFoundException("id " + user.getId() + " không phải sinh viên");
        }

        Boolean find = studentRepository.existsByStudentCode(
                createStudentRequest.getStudentCode()
        );

        if (find) {
            throw new ResourceFoundException("Tồn tại sinh viên có mã "  + createStudentRequest.getStudentCode());
        }

        Student student = Student.builder()
                .studentCode(createStudentRequest.getStudentCode())
                .major(createStudentRequest.getMajor())
                .className(createStudentRequest.getClassName())
                .dateOfBirth(createStudentRequest.getDateOfBirth())
                .address(createStudentRequest.getAddress())
                .user(user)
                .build();

        return studentRepository.save(student);
    }

    @Override
    public Student editStudent(Integer id, EditStudentRequest editStudentRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thể tìm thấy người dùng"));

        Student student = studentRepository.findStudentByUser(user);

        student.setDateOfBirth(editStudentRequest.getDateOfBirth());
        student.setAddress(editStudentRequest.getAddress());
        user.setFullName(editStudentRequest.getFullName());
        user.setEmail(editStudentRequest.getEmail());
        String phoneNumber = editStudentRequest.getPhoneNumber().trim();
        if (!phoneNumber.equals(user.getPhoneNumber())
                && userRepository.existsUserByPhoneNumberAndIdNot(phoneNumber, user.getId())) {
            throw new ResourceFoundException("Số điện thoại đã được sử dụng");
        }
        user.setPhoneNumber(phoneNumber);

        return studentRepository.save(student);
    }

    @Override
    public Student editStudentAdmin(Integer id, AdminEditStudentRequest adminEditStudentRequest) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thể tìm thấy sinh viên"));

        User user = student.getUser();

        student.setMajor(adminEditStudentRequest.getMajor());
        student.setClassName(adminEditStudentRequest.getClassName());
        student.setDateOfBirth(adminEditStudentRequest.getDateOfBirth());
        student.setAddress(adminEditStudentRequest.getAddress());
        user.setFullName(adminEditStudentRequest.getFullName());
        user.setEmail(adminEditStudentRequest.getEmail());
        String phoneNumber = adminEditStudentRequest.getPhoneNumber().trim();
        if (!phoneNumber.equals(user.getPhoneNumber())
                && userRepository.existsUserByPhoneNumberAndIdNot(phoneNumber, user.getId())) {
            throw new ResourceFoundException("Số điện thoại đã được sử dụng");
        }
        user.setPhoneNumber(phoneNumber);

        return studentRepository.save(student);
    }
}
