package com.tastecamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tastecamp.api.dtos.RecipeDTO;
import com.tastecamp.api.models.RecipeModel;
import com.tastecamp.api.services.RecipeService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/recipes")
public class RecipeController {

    final RecipeService recipeService;

    RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping    
    public ResponseEntity<List<RecipeModel>> getRecipes() {
        List<RecipeModel> recipes = recipeService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(recipes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeModel> getRecipeById(@PathVariable Long id) {
        RecipeModel recipe = recipeService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(recipe);
    }

    @PostMapping
    public ResponseEntity<RecipeModel> createRecipe(@RequestBody @Valid RecipeDTO body) {
        RecipeModel recipe = recipeService.save(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(recipe);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecipeModel> updateRecipe(@PathVariable Long id, @RequestBody RecipeDTO body) {
        RecipeModel recipe = recipeService.update(body, id);
        return ResponseEntity.status(HttpStatus.OK).body(recipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
} 