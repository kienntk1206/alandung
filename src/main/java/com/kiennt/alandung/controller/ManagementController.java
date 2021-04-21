package com.kiennt.alandung.controller;

import com.kiennt.alandung.entity.Product;
import com.kiennt.alandung.service.AuthenticationService;
import com.kiennt.alandung.service.ProductService;
import com.kiennt.alandung.util.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
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

        return "product/list-product";
    }

    @GetMapping("/product-list")
    public String getProducts(Model model) {
        List<Product> products = productService.getProducts();
        model.addAttribute("products", products);
        return "product/list-product";
    }

    @GetMapping("/create-product")
    public String showCreateProductForm() {
        return "create-product";
    }

    @PostMapping("/create-product")
    public String createProduct(@Valid Product product, @RequestParam("image") MultipartFile multipartFile, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "product/create-product";
        }
        String productImage = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setImageName(productImage);
        Product productSaved = productService.upsert(product);

        String uploadDir = "product-photos/" + productSaved.getId();
        FileUploadUtils.saveFile(uploadDir, productImage, multipartFile);

        return "redirect:/product/list-product";
    }

    @GetMapping("/update-product/{id}")
    public String showUpdateProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);

        if (product == null) {
            return "Invalid";
        }

        model.addAttribute("product", product);
        return "product/update-product";
    }

    @PostMapping("/update-product")
    public String updateProduct(@Valid Product product, @RequestParam("image") MultipartFile multipartFile, BindingResult result) throws IOException {
        if (result.hasErrors()) {
            return "update-product";
        }
        String productImage = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        product.setImageName(productImage);
        Product productUpdated = productService.upsert(product);

        String uploadDir = "product-photos/" + productUpdated.getId();
        FileUploadUtils.saveFile(uploadDir, productImage, multipartFile);
        return "redirect:/product/list-product";
    }

    @PostMapping("delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);

        if (product == null) {
            return "Invalid";
        }

        productService.deleteProduct(product);
        return "redirect:/product/list-product";
    }
}