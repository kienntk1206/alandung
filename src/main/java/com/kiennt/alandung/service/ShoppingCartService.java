package com.kiennt.alandung.service;

import com.kiennt.alandung.entity.*;
import com.kiennt.alandung.entity.enums.Status;
import com.kiennt.alandung.repository.CartItemRepository;
import com.kiennt.alandung.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartItem getCartItemById(Long id) {
        Optional<CartItem> cartItem = cartItemRepository.findById(id);
        return cartItem.orElse(null);
    }

    public List<CartItem> getCartItemsByCustomer(Customer customer) {
        return cartItemRepository.findByCustomer(customer);
    }

    public CartItem getCartItemByCustomerAndProduct(Customer customer, Product product) {
        return cartItemRepository.findByCustomerAndProduct(customer, product);
    }

    public void upsertCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    public List<CartItem> getCartItems() {
        return cartItemRepository.findAll();
    }

    public List<CartItem> getCartItemsByStatus(Status status) {
        if (status == null) {
            return cartItemRepository.findAll();
        }
        return cartItemRepository.findAll().stream()
                .filter(cartItem -> cartItem.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public Integer addProductToCart(Long productId, Integer quantity, Customer customer) {
        Integer addedQuantity = quantity;

        Product product = productRepository.findById(productId).get();

        CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);

        if (cartItem != null && cartItem.getStatus().equals(Status.PENDING)) {
            addedQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(addedQuantity);
        } else {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
            cartItem.setStatus(Status.PENDING);
        }
        cartItemRepository.save(cartItem);

        return  addedQuantity;
    }

    public void addToCart(Long productId) {
        int quantity = 1;
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            CartItem cartItem = cartItemRepository.findByProduct(product.get());
            if (cartItem != null && cartItem.getStatus().equals(Status.PENDING)) {
                cartItem.setProduct(product.get());
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            } else {
                cartItem = new CartItem();
                cartItem.setProduct(product.get());
                cartItem.setQuantity(quantity);
                cartItem.setStatus(Status.PENDING);
            }
            cartItemRepository.save(cartItem);
        }
    }

    public Double getTotalPrice(List<CartItem> cartItems) {
        List<Double> totalItems = new ArrayList<>();
        cartItems.forEach(cartItem -> {
            double totalItem = cartItem.getProduct().getPrice() * cartItem.getQuantity();
            totalItems.add(totalItem);
        });
        return totalItems.stream().mapToDouble(Double::doubleValue).sum();
    }

    public ModelAndView getCartItemsPage() {
        List<CartItem> cartItems = getCartItemsByStatus(Status.PENDING);
        ModelAndView mav = new ModelAndView("cycle-cart");
        mav.addObject("cartItems", cartItems);
        Double subTotal = getTotalPrice(cartItems);
        mav.addObject("subTotal", subTotal);
        Double shipping = 2.0;
        mav.addObject("shipping", shipping);
        mav.addObject("total", subTotal + shipping);
        return mav;
    }
}
