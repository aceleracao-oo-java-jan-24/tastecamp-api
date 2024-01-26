package com.tastecamp.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tastecamp.api.dtos.RecipeDTO;
import com.tastecamp.api.models.CategoryModel;
import com.tastecamp.api.models.RecipeModel;
import com.tastecamp.api.models.UserModel;
import com.tastecamp.api.repositories.CategoryRepository;
import com.tastecamp.api.repositories.RecipeRepository;
import com.tastecamp.api.repositories.UserRepository;

@Service
public class RecipeService {

    final RecipeRepository recipeRepository;
    final UserRepository userRepository;
    final CategoryRepository categoryRepository;

    RecipeService(RecipeRepository recipeRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<RecipeModel> findAll() {
        return recipeRepository.findAll();
    }

    public Optional<RecipeModel> findById(Long id) {
        return recipeRepository.findById(id);
    }

    public Optional<RecipeModel> save(RecipeDTO dto) {
        if (recipeRepository.existsByTitle(dto.getTitle())) {
            return Optional.empty();
        }

        Optional<UserModel> user = userRepository.findById(dto.getAuthorId());

        if (!user.isPresent()) {
            return Optional.empty();
        }

        List<CategoryModel> categories = categoryRepository.findAllById(dto.getCategoryIds());
        
        RecipeModel recipe = new RecipeModel(dto, user.get(), categories);
        return Optional.of(recipeRepository.save(recipe));
    }

    public RecipeModel update(RecipeDTO dto, Long id) {
        RecipeModel newRecipe = new RecipeModel(dto);
        newRecipe.setId(id);
        return recipeRepository.save(newRecipe);
    }

    public void deleteById(Long id) {
        recipeRepository.deleteById(id);
    }
}
