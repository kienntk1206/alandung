package com.kiennt.alandung.repository;

import com.kiennt.alandung.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public Customer findByEmail(String email);
}
