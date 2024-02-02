package com.tastecamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.tastecamp.api.dtos.RecipeDTO;
import com.tastecamp.api.models.CategoryModel;
import com.tastecamp.api.models.RecipeModel;
import com.tastecamp.api.models.UserModel;
import com.tastecamp.api.repositories.CategoryRepository;
import com.tastecamp.api.repositories.RecipeRepository;
import com.tastecamp.api.repositories.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class RecipeIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    @AfterEach
    void cleanUpDatabase() {
        recipeRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void givenRepeatedRecipe_whenCreatingRecipe_thenThrowsError() {
        // given
        UserModel user = new UserModel(null, "username", "email@email.com");
        UserModel createdUser = userRepository.save(user);

        CategoryModel category = new CategoryModel(null, "category 1");
        CategoryModel createdCategory = categoryRepository.save(category);

        RecipeModel recipeConflict = new RecipeModel(null, "Title", "Ingredients", "Steps", createdUser,
                List.of(createdCategory));
        recipeRepository.save(recipeConflict);

        RecipeDTO recipeDto = new RecipeDTO("Title", "Ingredients", "Steps", createdUser.getId(),
                List.of(createdCategory.getId()));
        HttpEntity<RecipeDTO> body = new HttpEntity<>(recipeDto);

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                "/recipes", // rota
                HttpMethod.POST, // método http
                body, // body da requisição
                String.class // classe que representa o tipo da sua resposta
        // Se tivesse parâmetro na url, seria o quinto param aqui do exchange
        );

        // then
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(1, recipeRepository.count());
    }

    @Test
    void givenWrongUserId_whenCreatingRecipe_thenThrowsError() {
        // given
        UserModel user = new UserModel(null, "Test", "Test");
        UserModel createdUser = userRepository.save(user);
        userRepository.deleteById(createdUser.getId());

        CategoryModel category = new CategoryModel(null, "Test");
        CategoryModel createdCategory = categoryRepository.save(category);

        RecipeDTO recipe = new RecipeDTO("Test", "Test", "Test", createdUser.getId(), List.of(createdCategory.getId()));
        HttpEntity<RecipeDTO> body = new HttpEntity<>(recipe);

        // when
        ResponseEntity<String> response = restTemplate.exchange(
                "/recipes",
                HttpMethod.POST,
                body,
                String.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(0, recipeRepository.count());
    }

    @Test
    void givenValidRecipe_whenCreatingRecipe_thenCreatesRecipe() {
        // given
        UserModel user = new UserModel(null, "Test", "Test");
        UserModel createdUser = userRepository.save(user);

        CategoryModel category = new CategoryModel(null, "Test");
        CategoryModel createdCategory = categoryRepository.save(category);

        RecipeDTO recipe = new RecipeDTO("Test", "Test", "Test", createdUser.getId(), List.of(createdCategory.getId()));
        HttpEntity<RecipeDTO> body = new HttpEntity<>(recipe);

        // when
        ResponseEntity<RecipeModel> response = restTemplate.exchange(
                "/recipes",
                HttpMethod.POST,
                body,
                RecipeModel.class);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, recipeRepository.count());
    }
}
