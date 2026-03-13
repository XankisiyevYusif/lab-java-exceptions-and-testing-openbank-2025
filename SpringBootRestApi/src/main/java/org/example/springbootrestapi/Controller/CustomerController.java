package org.example.springbootrestapi.Controller;

import jakarta.validation.Valid;
import org.example.springbootrestapi.Model.Customer;
import org.example.springbootrestapi.Service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@Valid @RequestBody Customer customer) {
        Customer saved = customerService.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAll() {
        List<Customer> list = customerService.getAllCustomers();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Customer> getByEmail(@PathVariable String email) {
        Customer customer = customerService.getCustomerByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    @PutMapping("/{email}")
    public ResponseEntity<Customer> update(@PathVariable String email,
                                           @RequestBody Customer customer) {
        Customer updated = customerService.updateCustomer(email, customer);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email) {
        customerService.deleteCustomer(email);
        return ResponseEntity.noContent().build();
    }
}