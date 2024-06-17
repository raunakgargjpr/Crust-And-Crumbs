package com.raunak.Ecommerce.repository;

import com.raunak.Ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JPA will automatically create the method, we just need to tell the name
    List<Product> findAllByCategory_Id(int id);
}
