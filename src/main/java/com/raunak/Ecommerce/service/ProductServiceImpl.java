package com.raunak.Ecommerce.service;

import com.raunak.Ecommerce.model.Product;
import com.raunak.Ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService{
    ProductRepository productRepository;
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }
    @Override
    public Optional<Product> getProductById(Long theId) {
        return productRepository.findById(theId);
    }
    @Override
    public void deleteProductById(Long theId) {
        productRepository.deleteById(theId);
    }
    @Override
    public List<Product> getAllProductsByCategoryId(int theId) {
        return productRepository.findAllByCategory_Id(theId);
    }
}
