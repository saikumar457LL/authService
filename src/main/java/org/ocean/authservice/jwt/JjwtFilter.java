package org.ocean.authservice.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.ocean.authservice.entity.User;
import org.ocean.authservice.repository.UserRepository;
import org.ocean.authservice.responses.ApiResponse;
import org.ocean.authservice.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class JjwtFilter extends OncePerRequestFilter {

    private final JjwtUtils jjwtUtils;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String authorization = request.getHeader("Authorization");
            String username = null;
            User user = null;
            if(null != authorization && authorization.startsWith("Bearer ")) {
                authorization = authorization.substring(7);
                username = jjwtUtils.getUserName(authorization);
                user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username"));
            }
            if(null != user && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken userPrincipal = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                userPrincipal.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userPrincipal);
            }

            filterChain.doFilter(request,response);
        } catch (JwtException jwtException) {
            logger.error("JWT exception: {}", jwtException);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(
                    ApiResponse.builder()
                            .success(false)
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message("Invalid or expired token")
                            .error(ErrorResponse.builder()
                                    .path(request.getRequestURI())
                                    .error(jwtException.getMessage())
                                    .build())
                            .build()
            ));
        }
    }
}
