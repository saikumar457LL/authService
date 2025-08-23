package org.ocean.authservice.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.ocean.authservice.entity.User;
import org.ocean.authservice.exceptions.InvalidToken;
import org.ocean.authservice.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JjwtFilter extends OncePerRequestFilter {

    private final JjwtUtils jjwtUtils;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        String username = null;
        User user = null;
        if(null != authorization && authorization.startsWith("Bearer ")) {
            authorization = authorization.substring(7);
            username = jjwtUtils.getUserName(authorization);
            Optional<User> userDetails = userRepository.findByUsername(username);
            if(userDetails.isPresent()) {
                user = userDetails.get();
            }else {
                throw new UsernameNotFoundException("request username not found");
            }
        }
        if(null != user && SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken userPrincipal = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            userPrincipal.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(userPrincipal);
        }

        filterChain.doFilter(request,response);
    }
}
