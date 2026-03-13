package org.example.springbootrestapi.Service;

import org.example.springbootrestapi.Exception.ProductNotFoundException;
import org.example.springbootrestapi.Model.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    private Map<String, Product> productMap = new HashMap<>();

    public Product addProduct(String name, double price, String category, int quantity) {
        Product product = new Product();

        product.setName(name);
        product.setPrice(price);
        product.setCategory(category);
        product.setQuantity(quantity);

        productMap.put(product.getName(), product);

        return product;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productMap.values());
    }

    public Product getProductByName(String name) {
        Product product = productMap.get(name);
        if (product == null) {
            throw new ProductNotFoundException("Product not found: " + name);
        }
        return product;
    }

    public void updateProduct(String name, Product updatedProduct) {
        if (!productMap.containsKey(name)) {
            throw new ProductNotFoundException("Product not found: " + name);
        }
        productMap.put(name, updatedProduct);
    }

    public void deleteProduct(String name) {
        if (!productMap.containsKey(name)) {
            throw new ProductNotFoundException("Product not found: " + name);
        }
        productMap.remove(name);
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> result = new ArrayList<>();
        for (Product p : productMap.values()) {
            if (p.getCategory().equalsIgnoreCase(category)) {
                result.add(p);
            }
        }
        return result;
    }

    public List<Product> getProductsByPriceRange(double min, double max) {
        List<Product> result = new ArrayList<>();
        for (Product p : productMap.values()) {
            if (p.getPrice() >= min && p.getPrice() <= max) {
                result.add(p);
            }
        }
        return result;
    }
}
