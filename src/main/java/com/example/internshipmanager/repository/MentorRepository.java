package com.example.internshipmanager.repository;

import com.example.internshipmanager.models.entity.Mentor;
import com.example.internshipmanager.models.entity.Student;
import com.example.internshipmanager.models.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Integer> {
    @EntityGraph(attributePaths = "user")
    List<Mentor> findAll();

    @EntityGraph(attributePaths = "user")
    Mentor findMentorByUser(User user);
}
