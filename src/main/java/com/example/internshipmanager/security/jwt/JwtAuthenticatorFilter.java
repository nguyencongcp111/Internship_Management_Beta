package com.example.internshipmanager.security.jwt;

import com.example.internshipmanager.security.principal.CustomUserDetailService;
<<<<<<< HEAD
=======
import com.example.internshipmanager.service.JwtBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
=======
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
<<<<<<< HEAD
=======
import java.nio.file.AccessDeniedException;
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)

@RequiredArgsConstructor
@Component
public class JwtAuthenticatorFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailService customUserDetailService;
<<<<<<< HEAD
=======
    private final JwtBlacklistService jwtBlacklistService;
>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)

    private String getTokenFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");

        if (authorization!=null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }

        return null;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        String token = getTokenFromRequest(request);

        try {
<<<<<<< HEAD
            if (token != null && jwtProvider.validateToken(token)){
                String username = jwtProvider.getUsernameFromToken(token);
                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

=======
            if (token != null){
                if (!jwtProvider.validateToken(token)) {
                    throw new BadCredentialsException("Token không hợp lệ");
                }

                Claims claims = jwtProvider.getClaimsFromToken(token);

                String jti = claims.getId();

                if (jwtBlacklistService.isBlacklisted(jti)) {
                    throw new BadCredentialsException(
                            "Token đã bị thu hồi"
                    );
                }

                String username = claims.getSubject();

                UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

                if (!userDetails.isEnabled()) {
                    throw new DisabledException(
                            "Tài khoản không hoạt động"
                    );
                }

>>>>>>> 2cf64d9 (fixed auth, gan nham roundId)
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null,
                                userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            request.setAttribute("jwt_error", e.getMessage());

            response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    e.getMessage()
            );

            e.printStackTrace();
            throw e;
        }
    }

}
