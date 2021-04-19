package com.kiennt.alandung.repository;

import com.kiennt.alandung.entity.CartItem;
import com.kiennt.alandung.entity.Customer;
import com.kiennt.alandung.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    public List<CartItem> findByCustomer(Customer customer);

    public CartItem findByCustomerAndProduct(Customer customer, Product product);
}
