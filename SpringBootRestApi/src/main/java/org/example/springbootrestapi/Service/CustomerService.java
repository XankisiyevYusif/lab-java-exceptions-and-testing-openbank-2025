package org.example.springbootrestapi.Service;

import org.example.springbootrestapi.Exception.CustomerNotFoundException;
import org.example.springbootrestapi.Model.Customer;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CustomerService {

    private Map<String, Customer> customers = new HashMap<>();

    public Customer save(Customer customer) {
        customers.put(customer.getEmail(), customer);
        return customer;
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }

    public Customer getCustomerByEmail(String email)
    {
        if (!customers.containsKey(email)) {
            throw new CustomerNotFoundException("Customer not found with email: " + email);
        }
        return customers.get(email);
    }

    public Customer updateCustomer(String email, Customer customer) {
        customers.put(email, customer);
        return customer;
    }

    public void deleteCustomer(String email) {
        customers.remove(email);
    }
}
