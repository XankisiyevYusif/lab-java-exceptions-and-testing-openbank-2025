package org.example.springbootrestapi;

import org.example.springbootrestapi.Controller.ProductController;
import org.example.springbootrestapi.Model.Product;
import org.example.springbootrestapi.Service.ProductService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String API_KEY = "123456";

    @Test
    void createProduct_success() throws Exception {

        Product product = new Product("Laptop", 2000, "Tech", 10);

        Mockito.when(productService.addProduct(
                        "Laptop",2000,"Tech",10))
                .thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .header("API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void getAllProducts_success() throws Exception {

        Product product = new Product("Laptop",2000,"Tech",10);

        Mockito.when(productService.getAllProducts())
                .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products")
                        .header("API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    void getProductByName_success() throws Exception {

        Product product = new Product("Laptop",2000,"Tech",10);

        Mockito.when(productService.getProductByName("Laptop"))
                .thenReturn(product);

        mockMvc.perform(get("/api/products/Laptop")
                        .header("API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value("Tech"));
    }

    @Test
    void updateProduct_success() throws Exception {

        Product product = new Product("Laptop",2100,"Tech",5);

        Mockito.doNothing().when(productService)
                .updateProduct(Mockito.eq("Laptop"), Mockito.any(Product.class));

        mockMvc.perform(put("/api/products/Laptop")
                        .header("API-Key", API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProduct_success() throws Exception {

        Mockito.doNothing().when(productService).deleteProduct("Laptop");

        mockMvc.perform(delete("/api/products/Laptop")
                        .header("API-Key", API_KEY))
                .andExpect(status().isNoContent());
    }

    @Test
    void getProductsByCategory_success() throws Exception {

        Product product = new Product("Laptop",2000,"Tech",10);

        Mockito.when(productService.getProductsByCategory("Tech"))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products/category/Tech")
                        .header("API-Key", API_KEY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Tech"));
    }

    @Test
    void getProductsByPriceRange_success() throws Exception {

        Product product = new Product("Laptop",2000,"Tech",10);

        Mockito.when(productService.getProductsByPriceRange(1000,3000))
                .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products/price")
                        .header("API-Key", API_KEY)
                        .param("min","1000")
                        .param("max","3000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].price").value(2000));
    }

    @Test
    void getProducts_apiKeyMissing() throws Exception {

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getProducts_invalidApiKey() throws Exception {

        mockMvc.perform(get("/api/products")
                        .header("API-Key","wrong"))
                .andExpect(status().isUnauthorized());
    }
}