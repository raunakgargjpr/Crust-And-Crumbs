package com.raunak.Ecommerce.service;

import com.raunak.Ecommerce.model.Category;
import com.raunak.Ecommerce.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }
    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }
    @Override
    public void deleteCategoryById(int theId) {
        categoryRepository.deleteById(theId);
    }
    @Override
    public Optional<Category> findCategoryById(int theId) {
        Optional<Category> result = categoryRepository.findById(theId);
        return result;
    }
}
