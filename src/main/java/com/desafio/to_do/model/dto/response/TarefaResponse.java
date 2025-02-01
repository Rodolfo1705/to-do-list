package com.desafio.to_do.model.dto.response;

public record TarefaResponse(
        Long id,
        String titulo,
        String descricao,
        String status
) {}
