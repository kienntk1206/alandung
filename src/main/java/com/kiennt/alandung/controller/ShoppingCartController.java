package com.kiennt.alandung.controller;

import com.kiennt.alandung.service.CustomerService;
import com.kiennt.alandung.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/cart")
    public String showShoppingCart(Model model, @AuthenticationPrincipal Authentication authentication) {


        return "shopping_cart";
    }
}
