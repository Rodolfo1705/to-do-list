package com.desafio.to_do.model.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TarefaUpdateRequest(
        @NotBlank(message = "O status não pode ser branco ou nulo.")
        String status
) {
}
