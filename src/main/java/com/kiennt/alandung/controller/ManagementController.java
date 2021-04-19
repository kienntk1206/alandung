package com.kiennt.alandung.controller;

import com.kiennt.alandung.entity.Product;
import com.kiennt.alandung.service.AuthenticationService;
import com.kiennt.alandung.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/management")
public class ManagementController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public String view() {
//        boolean authenticated = authenticationService.isAuthenticated();

//        if(!authenticated) {
//            return "redirect:/login-page";
//        }

        return "list-product";
    }

    @GetMapping("/product-list")
    public String getProducts(Model model) {
        List<Product> products = productService.getProducts();
        model.addAttribute("products", products);
        return "list-product";
    }

    @GetMapping("/create-product")
    public String showCreateProductForm() {
        return "create-product";
    }

    @PostMapping("/create-product")
    public String createProduct(@Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "create-product";
        }
        productService.upsert(product);
        return "redirect:/list-product";
    }

    @GetMapping("/update-product/{id}")
    public String showUpdateProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);

        if (product == null) {
            return "Invalid";
        }

        model.addAttribute("product", product);
        return "update-product";
    }

    @PostMapping("/update-product")
    public String updateProduct(@Valid Product product, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "update-product";
        }
        productService.upsert(product);
        return "redirect:/list-product";
    }

    @PostMapping("delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);

        if (product == null) {
            return "Invalid";
        }

        productService.deleteProduct(product);
        return "redirect:/list-product";
    }
}