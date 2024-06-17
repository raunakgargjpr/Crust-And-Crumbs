package com.raunak.Ecommerce.service;

import com.raunak.Ecommerce.model.Category;
import com.raunak.Ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    void addCategory(Category category);
    List<Category> getAllCategory();
    void deleteCategoryById(int theId);
    Optional<Category> findCategoryById(int theId);
}
