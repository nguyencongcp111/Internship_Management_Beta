package com.example.internshipmanager.service.impl;

import com.example.internshipmanager.exception.ResourceFoundException;
import com.example.internshipmanager.exception.ResourceNotFoundException;
import com.example.internshipmanager.exception.UnexpectedRoleException;
import com.example.internshipmanager.models.dto.request.admin.user.CreateUserRequest;
import com.example.internshipmanager.models.dto.request.admin.user.EditRoleRequest;
import com.example.internshipmanager.models.dto.request.admin.user.EditStatusRequest;
import com.example.internshipmanager.models.dto.request.admin.user.EditUserRequest;
import com.example.internshipmanager.models.entity.Roles;
import com.example.internshipmanager.models.entity.User;
import com.example.internshipmanager.repository.UserRepository;
import com.example.internshipmanager.service.AdminService;
<<<<<<< HEAD
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
=======
import com.example.internshipmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không thể tìm thấy người dùng"));
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        boolean find = userRepository.existsUserByUsername(createUserRequest.getUsername());

        if (find) {
            throw new ResourceFoundException("Username đã tồn tại");
        }

        boolean find2 = userRepository.existsUserByEmail(createUserRequest.getEmail());

        if (find2) {
            throw new ResourceFoundException("Email đã tồn tại");
        }

        User user = User.builder()
                .username(createUserRequest.getUsername())
                .passwordHash(passwordEncoder.encode(createUserRequest.getPassword()))
                .fullName(createUserRequest.getFullName())
                .email(createUserRequest.getEmail())
                .phoneNumber(createUserRequest.getPhoneNumber())
                .role(createUserRequest.getRole())
                .isActive(createUserRequest.isActive())
                .createAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    @Override
    public User editUser(Integer id, EditUserRequest editUserRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user có id" + id));

<<<<<<< HEAD
        boolean find2 = userRepository.existsUserByEmail(editUserRequest.getEmail());

        if (find2) {
            throw new ResourceFoundException("Email đã tồn tại");
        }


        user.setFullName(editUserRequest.getFullName());
        user.setEmail(editUserRequest.getEmail());
=======
        if (editUserRequest.getEmail() != null && !editUserRequest.getEmail().isEmpty()) {
            boolean find2 = userRepository.existsUserByEmail(editUserRequest.getEmail());

            if (find2 && editUserRequest.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new ResourceFoundException("Email đã tồn tại");
            }

            user.setEmail(editUserRequest.getEmail());
        }

        user.setFullName(editUserRequest.getFullName());
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
        user.setPhoneNumber(editUserRequest.getPhoneNumber());

        return userRepository.save(user);
    }

    @Override
    public User editRole(Integer id, EditRoleRequest editRoleRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user có id" + id));

<<<<<<< HEAD
        if(user.getRole() == Roles.MENTOR && editRoleRequest.getRole() == Roles.STUDENT) {
=======
        if(user.getRole() == Roles.MENTOR && (editRoleRequest.getRole() == Roles.STUDENT || editRoleRequest.getRole() == null)) {
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
            throw new UnexpectedRoleException("Không thể chỉnh sửa role");
        }

        if(user.getRole() == Roles.ADMIN &&
<<<<<<< HEAD
                (editRoleRequest.getRole() == Roles.MENTOR ||
                        editRoleRequest.getRole() == Roles.STUDENT)) {
=======
                (Set.of(Roles.MENTOR, Roles.STUDENT).contains(editRoleRequest.getRole()) || editRoleRequest.getRole() == null)) {
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
            throw new UnexpectedRoleException("Không thể chỉnh sửa role");
        }

        user.setRole(editRoleRequest.getRole());

        return userRepository.save(user);
    }

    @Override
<<<<<<< HEAD
=======
    @Transactional
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
    public User editStatus(Integer id, EditStatusRequest editStatusRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user có id" + id));

        boolean currentStatus = user.isActive();
        boolean newStatus = editStatusRequest.isActive();

<<<<<<< HEAD
        System.out.println("user " + id + " có trạng thái: " + currentStatus);

        System.out.println("Trạng thái mới " + newStatus);

        if (currentStatus == newStatus) {
            throw new UnexpectedRoleException("Trạng thái không thay đổi");
=======
        if (currentStatus == newStatus) {
            throw new RuntimeException("Trạng thái không thay đổi");
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
        }

        user.setActive(newStatus);

        return userRepository.save(user);
    }

<<<<<<< HEAD
    @Override
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user có id" + id));

        user.setActive(false);
    }

=======
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
}
