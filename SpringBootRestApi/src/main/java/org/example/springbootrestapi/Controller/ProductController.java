package org.example.springbootrestapi.Controller;

import jakarta.validation.Valid;
import org.example.springbootrestapi.Model.Product;
import org.example.springbootrestapi.Service.ProductService;
import org.example.springbootrestapi.Exception.MissingApiKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private static final String API_KEY = "123456";

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private void checkApiKey(String apiKey) {
        if (apiKey == null) {
            throw new MissingApiKeyException("API-Key header is missing");
        }
        if (!API_KEY.equals(apiKey)) {
            throw new MissingApiKeyException("Invalid API-Key");
        }
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestHeader("API-Key") String apiKey,
                                          @Valid @RequestBody Product product) {
        checkApiKey(apiKey);
        Product created = productService.addProduct(
                product.getName(),
                product.getPrice(),
                product.getCategory(),
                product.getQuantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAll(@RequestHeader("API-Key") String apiKey) {
        checkApiKey(apiKey);
        List<Product> list = productService.getAllProducts();
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Product> getByName(@RequestHeader("API-Key") String apiKey,
                                             @PathVariable String name) {
        checkApiKey(apiKey);
        Product product = productService.getProductByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PutMapping("/{name}")
    public ResponseEntity<Product> update(@RequestHeader("API-Key") String apiKey,
                                          @PathVariable String name,
                                          @RequestBody Product product)
    {
        checkApiKey(apiKey);
        productService.updateProduct(name, product);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> delete(@RequestHeader("API-Key") String apiKey,
                                       @PathVariable String name)
    {
        checkApiKey(apiKey);
        productService.deleteProduct(name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getByCategory(@RequestHeader("API-Key") String apiKey,
                                                       @PathVariable String category) {
        checkApiKey(apiKey);
        List<Product> list = productService.getProductsByCategory(category);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/price")
    public ResponseEntity<List<Product>> getByPriceRange(@RequestHeader("API-Key") String apiKey,
                                                         @RequestParam double min,
                                                         @RequestParam double max) {
        checkApiKey(apiKey);
        List<Product> list = productService.getProductsByPriceRange(min, max);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }
}