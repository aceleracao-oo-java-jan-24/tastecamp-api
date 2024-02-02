package com.tastecamp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.tastecamp.api.dtos.RecipeDTO;
import com.tastecamp.api.exceptions.RecipeTitleConflictException;
import com.tastecamp.api.exceptions.UserNotFoundException;
import com.tastecamp.api.models.CategoryModel;
import com.tastecamp.api.models.RecipeModel;
import com.tastecamp.api.models.UserModel;
import com.tastecamp.api.repositories.CategoryRepository;
import com.tastecamp.api.repositories.RecipeRepository;
import com.tastecamp.api.repositories.UserRepository;
import com.tastecamp.api.services.RecipeService;

@SpringBootTest
class RecipeUnitTests {

	@InjectMocks
	private RecipeService recipeService;

	@Mock
	private RecipeRepository recipeRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@Test
	void givenRepeatedRecipe_whenCreatingRecipe_thenThrowsError() {
		// given => condições iniciais do teste
		List<Long> categoryIds = List.of(1L, 2L);
		Long authorId = 1L;
		RecipeDTO recipeDto = new RecipeDTO("Title", "Ingredients", "Steps", authorId, categoryIds);

		doReturn(true).when(recipeRepository).existsByTitle(any());

		// when => a operação que vai acontecer (a chamada da função da Service)
		RecipeTitleConflictException exception = assertThrows(
			RecipeTitleConflictException.class, 
			() -> recipeService.save(recipeDto));

		// then => verificações dos testes
		assertNotNull(exception);
		assertEquals("This recipe already exists!", exception.getMessage());
		verify(recipeRepository, times(0)).save(any());
		verify(recipeRepository, times(1)).existsByTitle(any());
	}

	@Test
	void givenWrongUserId_whenCreatingRecipe_thenThrowsError() {
		// given
		RecipeDTO recipeDto = new RecipeDTO("Title", "Ingredients", "Steps", 1L, List.of(1L, 2L));

		doReturn(false).when(recipeRepository).existsByTitle(any());
		doReturn(Optional.empty()).when(userRepository).findById(any());

		// when
		UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> recipeService.save(recipeDto));

		// then
		assertNotNull(exception);
		assertEquals("User not found by this id!", exception.getMessage());
		verify(recipeRepository, times(0)).save(any());
		verify(recipeRepository, times(1)).existsByTitle(any());
		verify(userRepository, times(1)).findById(any());
	}

	@Test
	void givenValidRecipe_whenCreatingRecipe_thenCreatesRecipe() {
		// given
		RecipeDTO recipeDto = new RecipeDTO("Title", "Ingredients", "Steps", 1L, List.of(1L));
		UserModel user = new UserModel(1L, "username", "email@email.com");
		CategoryModel category = new CategoryModel(1L, "categoria");
		RecipeModel newRecipe = new RecipeModel(recipeDto, user, List.of(category));

		doReturn(false).when(recipeRepository).existsByTitle(any());
		doReturn(Optional.of(user)).when(userRepository).findById(any());
		doReturn(List.of(category)).when(categoryRepository).findAllById(any());
		doReturn(newRecipe).when(recipeRepository).save(any());

		// when
		RecipeModel result = recipeService.save(recipeDto);

		// then
		assertNotNull(result);
		verify(recipeRepository, times(1)).existsByTitle(any());
		verify(userRepository, times(1)).findById(any());
		verify(categoryRepository, times(1)).findAllById(any());
		verify(recipeRepository, times(1)).save(any());
		assertEquals(newRecipe, result);

	}
}
