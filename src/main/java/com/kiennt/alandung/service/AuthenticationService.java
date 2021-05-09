package com.kiennt.alandung.service;

import com.kiennt.alandung.entity.Role;
import com.kiennt.alandung.entity.User;
import com.kiennt.alandung.entity.enums.RoleName;
import com.kiennt.alandung.security.CustomUserDetailsService;
import com.kiennt.alandung.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public Authentication setAuthentication(User user, HttpServletRequest request) {
        UserDetails userDetails = customUserDetailsService.loadUserById(user.getId());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return authentication;
    }

    public Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return authentication;
        }

        return null;
    }

    public boolean checkUserHasRole(User user, List<RoleName> roleNames) {
        return user.getRoles().stream()
                .map(Role::getName)
                .anyMatch(new HashSet<>(roleNames)::contains);
    }

    public User getLoginedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public boolean isAccessDenied() {
        User loginedUser = getLoginedUser();
        boolean hasPermission = checkUserHasRole(loginedUser, Collections.singletonList(RoleName.ROLE_ADMIN));
        return !hasPermission;
    }
}
