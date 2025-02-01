package com.desafio.to_do.controller;

import com.desafio.to_do.model.dto.request.TarefaRequest;
import com.desafio.to_do.model.dto.request.TarefaUpdateRequest;
import com.desafio.to_do.model.dto.response.TarefaResponse;
import com.desafio.to_do.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;


    @PostMapping
    public ResponseEntity<Void> criarTarefa(@RequestBody TarefaRequest tarefaRequest) {
        Long idTarefaCriada = tarefaService.saveTarefa(tarefaRequest);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(idTarefaCriada)
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<List<TarefaResponse>> getAllTarefas() {
        List<TarefaResponse> tarefas = tarefaService.getAllTarefas();
        return ResponseEntity.ok(tarefas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponse> getTarefa(@PathVariable Long id) {
        TarefaResponse tarefa = tarefaService.getTarefa(id);
        return ResponseEntity.ok(tarefa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTarefa(@PathVariable Long id, @RequestBody TarefaUpdateRequest tarefaUpdateRequest) {
        tarefaService.updateTarefa(id, tarefaUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarefa(@PathVariable Long id) {
        tarefaService.deleteTarefa(id);
        return ResponseEntity.noContent().build();
    }
}
