package com.tastecamp.api.dtos;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RecipeDTO {

    @NotBlank(message = "Title is mandatory")
    @Size(max = 150)
    private String title;

    @NotBlank
    private String ingredients;

    @NotBlank
    private String steps;

    @NotNull
    private Long authorId;

    private List<Long> categoryIds;
}
