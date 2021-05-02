package com.kiennt.alandung.controller;

import com.kiennt.alandung.dto.CustomerDTO;
import com.kiennt.alandung.entity.CartItem;
import com.kiennt.alandung.entity.enums.Status;
import com.kiennt.alandung.service.CustomerService;
import com.kiennt.alandung.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/detail")
    public ModelAndView getCartItems() {
        return shoppingCartService.getCartItemsPage();
    }

    @GetMapping("/add-to-cart/{id}")
    public ModelAndView addToCart(@PathVariable("id") Long productId) {
        shoppingCartService.addToCart(productId);
        return shoppingCartService.getCartItemsPage();
    }

    @GetMapping("/contact")
    public ModelAndView showFormContact(CustomerDTO customerDTO) {
        ModelAndView mav = new ModelAndView("cycle-contact");
        List<CartItem> cartItems = shoppingCartService.getCartItemsByStatus(Status.PENDING);
        mav.addObject("cartItems", cartItems);
        return mav;
    }
}
