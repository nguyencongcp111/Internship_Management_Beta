package com.example.internshipmanager.repository;

import com.example.internshipmanager.models.entity.Student;
import com.example.internshipmanager.models.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @EntityGraph(attributePaths = "user")
    List<Student> findAll();

    @EntityGraph(attributePaths = "user")
    Student findStudentByUser(User user);

    Boolean findStudentByStudentCode(String studentCode);

}
