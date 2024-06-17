package com.raunak.Ecommerce.service;

import com.raunak.Ecommerce.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    void addProduct(Product product);
    Optional<Product> getProductById(Long theId);
    void deleteProductById(Long theId);
    List<Product> getAllProductsByCategoryId(int theId);
}
