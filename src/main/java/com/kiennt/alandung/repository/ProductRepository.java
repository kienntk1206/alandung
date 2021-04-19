package com.kiennt.alandung.repository;

import com.kiennt.alandung.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product getById(Long id);
}
