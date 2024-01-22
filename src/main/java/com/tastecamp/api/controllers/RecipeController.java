package com.tastecamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tastecamp.api.models.RecipeModel;
import com.tastecamp.api.repositories.RecipeRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/recipes")
public class RecipeController {

    final RecipeRepository recipeRepository;

    RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @GetMapping    
    public List<RecipeModel> getRecipes() {
        return recipeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<RecipeModel> getRecipeById(@PathVariable Long id) {
        Optional<RecipeModel> recipe = recipeRepository.findById(id);

        if (!recipe.isPresent()) {
            return Optional.empty();
        }

        return recipe;
    }

    @PostMapping
    public String createRecipe(@RequestBody String body) {
        System.out.println(body);
        return body;
    }

    @PutMapping("/{id}")
    public String updateRecipe(@PathVariable Long id, @RequestBody String body) {
        return body + id;
    }

    @DeleteMapping("/{id}")
    public String deleteRecipe(@PathVariable Long id) {
        return "Deletando item " + id;
    }
    
}
