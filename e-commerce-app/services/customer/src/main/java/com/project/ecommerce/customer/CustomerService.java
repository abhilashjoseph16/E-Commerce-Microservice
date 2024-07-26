package com.project.ecommerce.customer;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.project.ecommerce.exception.CustomerNotFoundException;

import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public String createCustomer(CustomerRequest request){
        var customer = repository.save(mapper.toCustomer(request));
        return customer.getId();
    }

    public void updateCustomer(@Valid CustomerRequest request) {
        var customer = repository.findById(request.id())
        .orElseThrow(()-> new CustomerNotFoundException(String.format("Cannot update customer:: No Customer found with the provied ID:: %s", request.id())));

       mergeCustomer(customer,request);
       repository.save(customer);
    }

    private void mergeCustomer(Customer customer, @Valid CustomerRequest request) {

        if(StringUtils.isNotBlank(request.firstname())){
            customer.setFirstname(request.firstname());
        }
        if(StringUtils.isNotBlank(request.lastname())){
            customer.setFirstname(request.lastname());
        }
        if(StringUtils.isNotBlank(request.email())){
            customer.setFirstname(request.email());
        }
        if(request.address()!= null){
            customer.setAddress(request.address());
        }
    }

    public List<CustomerResponse> findAllCustomers() {
        return repository.findAll()
        .stream()
        .map(mapper::fromCustomer)
        .collect(Collectors.toList());
    }

    public Boolean existsById(String customerId) {
        return repository.findById(customerId).isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return repository.findById(customerId)
        .map(mapper::fromCustomer)
        .orElseThrow(()-> new CustomerNotFoundException(String.format("Not Customer found with the provided ID:: %s",customerId)));
    }

    public void deleteCustomer(String customerId){
        repository.deleteById(customerId);
    }
    
}