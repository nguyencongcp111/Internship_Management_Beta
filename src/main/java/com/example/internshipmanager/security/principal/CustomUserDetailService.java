package com.example.internshipmanager.security.principal;

import com.example.internshipmanager.models.entity.User;
import com.example.internshipmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
<<<<<<< HEAD
=======
import org.springframework.security.core.userdetails.UsernameNotFoundException;
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username)
<<<<<<< HEAD
                .orElseThrow(() -> new RuntimeException("Không tồn tại tài khoản này"));

        if (!user.isActive()) {
            throw new RuntimeException("Tài khoản không hoạt động");
        }
=======
                .orElseThrow(() -> new UsernameNotFoundException("Không tồn tại tài khoản này"));
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)

        Collection<? extends GrantedAuthority> authorities =
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        )
                );

        return UserPrincipal.builder()
                .user(user)
                .authorities(authorities)
                .build();
    }
}
