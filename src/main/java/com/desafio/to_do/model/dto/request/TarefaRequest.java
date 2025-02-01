package com.desafio.to_do.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TarefaRequest(

        @NotBlank(message = "O título não pode ser nulo ou branco.")
        String titulo,

        @Size(max = 500, message = "A descrição não pode ter mais que 500 caracteres.")
        String descricao
) {}
