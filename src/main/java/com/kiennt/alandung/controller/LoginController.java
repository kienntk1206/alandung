package com.kiennt.alandung.controller;

import com.kiennt.alandung.entity.User;
import com.kiennt.alandung.entity.UserLogin;
import com.kiennt.alandung.repository.UserRepository;
import com.kiennt.alandung.security.JwtTokenProvider;
import com.kiennt.alandung.service.AuthenticationService;
import com.kiennt.alandung.service.UserLoginService;
import com.kiennt.alandung.util.CommonConstant;
import com.kiennt.alandung.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserLoginService userLoginService;

    @GetMapping("/login-page")
    public String login() {
        return "login-page";
    }

//    @GetMapping("/")
//    public ModelAndView index(Model model){
//        ModelAndView mav = new ModelAndView("index");
//        List<UserLogin> userLogins = userLoginService.getUserLogins();
//
//        Optional<UserLogin> userLogin = userLogins.stream().findFirst();
//
//        userLogin.ifPresent(user -> mav.addObject("userLogin", user.getFirstName().concat(CommonConstant.EMPTY_STRING)
//                                                                                    .concat(user.getLastName())));
//
//        return mav;
//    }

    @GetMapping("/test")
    public ModelAndView testlogin() {
        ModelAndView mav = new ModelAndView("test");
        List<UserLogin> userLogins = userLoginService.getUserLogins();

        Optional<UserLogin> userLogin = userLogins.stream().findFirst();

        userLogin.ifPresent(user -> mav.addObject("userLogin", user.getFirstName().concat(CommonConstant.EMPTY_STRING)
                .concat(user.getLastName())));

        return mav;
    }

    @PostMapping("/login")
    @Transactional
    public String doLogin(@RequestParam("email") String email,
                          @RequestParam("password") String password, HttpServletResponse response){
        Optional<User> users = userRepository.findByEmail(email);

        if(!users.isPresent() || !passwordEncoder.matches(password ,users.get().getPassword())) {
            return null;
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email, password
        ));

        authenticationService.setAuthentication(authentication);

        String jwtToken = tokenProvider.generateToken(authentication);
        CookieUtils.addCookie(response, "jwtToken", jwtToken);

        if (authenticationService.getAuthentication() != null) {
            return "redirect:/management/product-list";
        }

        return "redirect:/login-page";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationService.getAuthentication();

        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return null;
    }
}
