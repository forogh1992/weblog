package com.example.weblog.modules.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CategoryService {


    private CategoryRepo categoryRepo;

    @Autowired
    public CategoryService(CategoryRepo categoryRepository) {
        this.categoryRepo = categoryRepository;
    }

    public Category registerCategory(Category category) {
        return this.categoryRepo.save(category);
    }

    public List<Category> findAllCategories() {
        return this.categoryRepo.findAll();
    }

    public Category findById(Long id) {
        return categoryRepo.getOne(id);
    }

    @Transactional
    public void deleteById(Long id) {
        categoryRepo.deleteById(id);
    }

}
