package com.kiennt.alandung.repository;

import com.kiennt.alandung.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository {
    public Customer findByEmail(String email);
}
