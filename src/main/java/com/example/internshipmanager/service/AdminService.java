package com.example.internshipmanager.service;

import com.example.internshipmanager.models.dto.request.admin.user.CreateUserRequest;
import com.example.internshipmanager.models.dto.request.admin.user.EditRoleRequest;
import com.example.internshipmanager.models.dto.request.admin.user.EditStatusRequest;
import com.example.internshipmanager.models.dto.request.admin.user.EditUserRequest;
import com.example.internshipmanager.models.entity.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUser();

    User getUserById(Integer id);

    User createUser(CreateUserRequest createUserRequest);

    User editUser(Integer id, EditUserRequest editUserRequest);

    User editRole(Integer id, EditRoleRequest editRoleRequest);

    User editStatus(Integer id, EditStatusRequest editStatusRequest);

    void deleteUser(Integer id);
}
