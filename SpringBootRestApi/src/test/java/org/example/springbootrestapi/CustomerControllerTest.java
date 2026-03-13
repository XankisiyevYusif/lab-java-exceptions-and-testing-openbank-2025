package org.example.springbootrestapi;

import org.example.springbootrestapi.Controller.CustomerController;
import org.example.springbootrestapi.Exception.CustomerNotFoundException;
import org.example.springbootrestapi.Model.Customer;
import org.example.springbootrestapi.Service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setName("Yusif");
        customer.setEmail("yusif@gmail.com");
    }

    @Test
    void createCustomer_success() throws Exception {

        Mockito.when(customerService.save(Mockito.any(Customer.class)))
                .thenReturn(customer);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("yusif@gmail.com"));
    }

    @Test
    void getAllCustomers_success() throws Exception {

        Mockito.when(customerService.getAllCustomers())
                .thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("yusif@gmail.com"));
    }

    @Test
    void getCustomerByEmail_success() throws Exception {

        Mockito.when(customerService.getCustomerByEmail("yusif@gmail.com"))
                .thenReturn(customer);

        mockMvc.perform(get("/api/customers/yusif@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yusif"));
    }

    @Test
    void getCustomerByEmail_notFound() throws Exception {

        Mockito.when(customerService.getCustomerByEmail("notfound@gmail.com"))
                .thenThrow(new CustomerNotFoundException("Customer not found"));

        mockMvc.perform(get("/api/customers/notfound@gmail.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomer_success() throws Exception {

        Mockito.when(customerService.updateCustomer(
                        Mockito.eq("yusif@gmail.com"),
                        Mockito.any(Customer.class)))
                .thenReturn(customer);

        mockMvc.perform(put("/api/customers/yusif@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("yusif@gmail.com"));
    }

    @Test
    void deleteCustomer_success() throws Exception {

        Mockito.doNothing().when(customerService).deleteCustomer("yusif@gmail.com");

        mockMvc.perform(delete("/api/customers/yusif@gmail.com"))
                .andExpect(status().isNoContent());
    }
}
