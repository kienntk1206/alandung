package com.kiennt.alandung.controller.rest;

import com.kiennt.alandung.entity.User;
import com.kiennt.alandung.exception.BadRequestException;
import com.kiennt.alandung.payload.*;
import com.kiennt.alandung.repository.UserRepository;
import com.kiennt.alandung.security.CurrentUser;
import com.kiennt.alandung.security.JwtTokenProvider;
import com.kiennt.alandung.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        UserSummary userSummary = new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
        return userSummary;
    }

    @GetMapping("/user/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/user/information")
    public ResponseEntity<?> getUserFromToken(@RequestParam(value = "jwtToken") String jwtToken) {
        boolean isValidToken = tokenProvider.validateToken(jwtToken);
        if (!isValidToken) {
            throw new BadRequestException("Invalid JWT Token");
        }

        Long userId = tokenProvider.getUserIdFromJWT(jwtToken);
        User user = userRepository.getUserById(userId);
        return ResponseEntity.ok(user);

    }
}
