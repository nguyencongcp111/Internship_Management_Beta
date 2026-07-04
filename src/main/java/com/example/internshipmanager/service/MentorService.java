package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.admin.mentor.AdminEditMentorRequest;
import com.example.internshipmanager.models.dto.request.admin.mentor.CreateMentorRequest;
import com.example.internshipmanager.models.dto.request.general.EditMentorRequest;
import com.example.internshipmanager.models.entity.Mentor;

import java.util.List;

public interface MentorService {
    List<Mentor> getAllMentor();

    Mentor getMentorById(Integer id);

    Mentor getMentorByUserId(Integer id);

    Mentor addMentor(CreateMentorRequest createMentorRequest);

    Mentor editMentorAdmin(Integer id, AdminEditMentorRequest adminEditMentorRequest);

    Mentor editMentor(Integer id, EditMentorRequest editMentorRequest);
}
