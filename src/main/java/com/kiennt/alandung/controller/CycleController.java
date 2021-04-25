package com.kiennt.alandung.controller;

import com.kiennt.alandung.entity.Product;
import com.kiennt.alandung.entity.UserLogin;
import com.kiennt.alandung.service.ProductService;
import com.kiennt.alandung.service.UserLoginService;
import com.kiennt.alandung.util.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class CycleController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private ProductService productService;


    @GetMapping("/")
    public ModelAndView index(Model model){
        ModelAndView mav = new ModelAndView("index");
        List<UserLogin> userLogins = userLoginService.getUserLogins();

        Optional<UserLogin> userLogin = userLogins.stream().findFirst();

        userLogin.ifPresent(user -> mav.addObject("userLogin", user.getFirstName().concat(CommonConstant.EMPTY_STRING)
                .concat(user.getLastName())));

        List<Product> products = productService.getProducts();
        mav.addObject("products", products);

        return mav;
    }
}
