package com.kiennt.alandung.controller;

import com.kiennt.alandung.entity.Product;
import com.kiennt.alandung.entity.UserLogin;
import com.kiennt.alandung.service.ProductService;
import com.kiennt.alandung.service.ShoppingCartService;
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
public class PhoneController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/")
    public ModelAndView index(Model model){
        ModelAndView mav = new ModelAndView("index");
        List<Product> products = productService.getProducts();
        mav.addObject("products", products);
        Integer numeberOfItemsInCart = shoppingCartService.getNumberOfItemsInCart();
        mav.addObject("numberOfItem", numeberOfItemsInCart);

        return mav;
    }
}
