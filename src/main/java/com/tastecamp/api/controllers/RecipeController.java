package com.tastecamp.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @GetMapping    
    public String getRecipes() {
        return "Lista de Receitas";
    }

    @GetMapping("/{id}")
    public String getRecipeById(@PathVariable Long id) {
        return "Essa é a receita " + id;
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
