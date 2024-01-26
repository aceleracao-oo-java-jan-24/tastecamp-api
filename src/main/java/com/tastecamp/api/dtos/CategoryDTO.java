package com.tastecamp.api.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDTO {
    @NotBlank(message = "O nome da categoria é obrigatório")
    private String name;
}
