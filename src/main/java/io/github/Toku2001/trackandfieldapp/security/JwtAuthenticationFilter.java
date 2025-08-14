package io.github.Toku2001.trackandfieldapp.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.Toku2001.trackandfieldapp.dto.user.UserDetailsForToken;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        if (jwtUtil.validateToken(token)) {
            String userName = jwtUtil.extractUserName(token);
            String userRole = jwtUtil.extractUserRole(token);
            long userId = jwtUtil.extractUserId(token);
            List<GrantedAuthority> authorities = new ArrayList<>();

            if (userRole != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole));
            }

            UserDetailsForToken userDetails = new UserDetailsForToken(userId, userName, authorities);
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}