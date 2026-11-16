package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceFoundException;
import com.example.internshipmanager.exception.ResourceNotFoundException;
import com.example.internshipmanager.models.dto.request.admin.mentor.AdminEditMentorRequest;
import com.example.internshipmanager.models.dto.request.admin.mentor.CreateMentorRequest;
import com.example.internshipmanager.models.dto.request.general.EditMentorRequest;
import com.example.internshipmanager.models.entity.Mentor;
import com.example.internshipmanager.models.entity.Roles;
import com.example.internshipmanager.models.entity.User;
import com.example.internshipmanager.repository.MentorRepository;
import com.example.internshipmanager.repository.UserRepository;
import com.example.internshipmanager.service.MentorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {
    private final MentorRepository mentorRepository;
    private final UserRepository userRepository;
    public List<Mentor> getAllMentor() {
        return mentorRepository.findAll();
    }

    public Mentor getMentorById(Integer id) {
        return mentorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy id này"));
    }

    public Mentor getMentorByUserId(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy id này"));

        if (user.getRole() != Roles.MENTOR) {
            throw new ResourceNotFoundException("id " + user.getId() + " không phải mentor");
        }

        return mentorRepository.findMentorByUser(user);
    }

    public Mentor addMentor(CreateMentorRequest createMentorRequest) {
        User user = userRepository.findById(createMentorRequest.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy id này"));

        if(user.getRole() != Roles.MENTOR) {
            throw new ResourceNotFoundException("id " + user.getId() + " không phải mentor");
        }

        Mentor mentor = Mentor.builder()
                .department(createMentorRequest.getDepartment())
                .academicRank(createMentorRequest.getAcademicRank())
                .user(user)
                .build();

        return mentorRepository.save(mentor);

    }

    public Mentor editMentorAdmin(Integer id, AdminEditMentorRequest adminEditMentorRequest){
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy id này"));

        User user = mentor.getUser();

        mentor.setDepartment(adminEditMentorRequest.getDepartment());
        mentor.setAcademicRank(adminEditMentorRequest.getAcademicRank());
        user.setFullName(adminEditMentorRequest.getFullName());
        user.setEmail(adminEditMentorRequest.getEmail());
        String phoneNumber = adminEditMentorRequest.getPhoneNumber().trim();
        if (!phoneNumber.equals(user.getPhoneNumber())
                && userRepository.existsUserByPhoneNumberAndIdNot(phoneNumber, user.getId())) {
            throw new ResourceFoundException("Số điện thoại đã được sử dụng");
        }
        user.setPhoneNumber(phoneNumber);

        return mentorRepository.save(mentor);
    }

    public Mentor editMentor(Integer id, EditMentorRequest editMentorRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Không tìm thấy id này"));

        Mentor mentor = mentorRepository.findMentorByUser(user);

        user.setFullName(editMentorRequest.getFullName());
        user.setEmail(editMentorRequest.getEmail());
        String phoneNumber = editMentorRequest.getPhoneNumber().trim();
        if (!phoneNumber.equals(user.getPhoneNumber())
                && userRepository.existsUserByPhoneNumberAndIdNot(phoneNumber, user.getId())) {
            throw new ResourceFoundException("Số điện thoại đã được sử dụng");
        }
        user.setPhoneNumber(phoneNumber);

        return mentorRepository.save(mentor);
    }
}
