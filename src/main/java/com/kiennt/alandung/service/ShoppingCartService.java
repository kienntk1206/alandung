package com.kiennt.alandung.service;

import com.kiennt.alandung.entity.CartItem;
import com.kiennt.alandung.entity.Customer;
import com.kiennt.alandung.entity.Product;
import com.kiennt.alandung.repository.CartItemRepository;
import com.kiennt.alandung.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<CartItem> getCartItemsByCustomer(Customer customer) {
        return cartItemRepository.findByCustomer(customer);
    }

    public Integer addProductToCart(Long productId, Integer quantity, Customer customer) {
        Integer addedQuantity = quantity;

        Product product = productRepository.findById(productId).get();

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);

        if (cartItem != null) {
            addedQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(addedQuantity);
        } else {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
        }
        cartItemRepository.save(cartItem);

        return  addedQuantity;
    }
}
