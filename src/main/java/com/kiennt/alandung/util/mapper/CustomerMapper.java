package com.kiennt.alandung.util.mapper;

import com.kiennt.alandung.dto.CustomerDTO;
import com.kiennt.alandung.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public Customer toEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setAddress(customerDTO.getAddress());
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setMessage(customerDTO.getMessage());
        return customer;
    }
}
