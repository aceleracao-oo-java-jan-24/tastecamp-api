package com.tastecamp.api.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tastecamp.api.dtos.CategoryDTO;
import com.tastecamp.api.models.CategoryModel;
import com.tastecamp.api.models.RecipeModel;
import com.tastecamp.api.repositories.CategoryRepository;
import com.tastecamp.api.repositories.RecipeRepository;

@Service
public class CategoryService {

    final CategoryRepository categoryRepository;
    final RecipeRepository recipeRepository;

    public CategoryService(CategoryRepository categoryRepository, RecipeRepository recipeRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
    }

    public CategoryModel createCategory(CategoryDTO categoryDTO) {
        CategoryModel category = new CategoryModel(categoryDTO);
        return categoryRepository.save(category);
    }

    public List<RecipeModel> findByCategoryId(Long id) {
        return recipeRepository.findByCategoryId(id);
    }
}
