package com.desafio.to_do.service;

import com.desafio.to_do.model.dto.request.TarefaRequest;
import com.desafio.to_do.model.dto.request.TarefaUpdateRequest;
import com.desafio.to_do.model.dto.response.TarefaResponse;
import com.desafio.to_do.model.entity.Tarefa;
import com.desafio.to_do.model.enums.StatusTarefa;
import com.desafio.to_do.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;

    /*
    * Para realizar o mapeamento de DTOs para entidade e vice-versa, fiz de forma manual por ser um projeto com poucas
    * entidades.
    *
    * Portanto, em um projeto real, optaria pelo uso da biblioteca MapStruct.
    */


    public Long saveTarefa(TarefaRequest tarefaRequest) {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(tarefaRequest.titulo());
        tarefa.setDescricao(tarefaRequest.descricao());

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        return tarefaSalva.getId();
    }

    public List<TarefaResponse> getAllTarefas() {
        List<Tarefa> tarefas = tarefaRepository.findAll();

        List<TarefaResponse> tarefasResponse = new ArrayList<>();
        tarefas.forEach((t) -> {
            tarefasResponse.add(
                    new TarefaResponse(
                            t.getId(),
                            t.getTitulo(),
                            t.getDescricao(),
                            t.getStatus()
                    )
            );
        });

        return tarefasResponse;
    }

    public TarefaResponse getTarefa(Long id) {
        Tarefa tarefa = findTarefaById(id);

        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus()
        );
    }

    public void updateTarefa(Long id, TarefaUpdateRequest tarefaUpdateRequest) {
        Tarefa tarefa = findTarefaById(id);

        try {
            tarefa.setStatus(StatusTarefa.valueOf(tarefaUpdateRequest.status()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido");
        }

        tarefaRepository.save(tarefa);
    }

    public void deleteTarefa(Long id) {
        /*
        * Cogitei utilizar da exclusão lógica para caso o usuário desejasse recuperar um tarefa deletada anteriormente,
        * tanto por arrependimento como por acabar excluindo 'sem querer' a tarefa errada, Além de manter o histórico.
        *
        * Para uma exclusão física definitiva, seria preciso apenas realizar o seguinte comando:
        *
        * tarefaRepository.deleteById(id);
        */

        Tarefa tarefa = findTarefaById(id);

        tarefa.setStatus(StatusTarefa.EXCLUIDA);

        tarefaRepository.save(tarefa);
    }

    private Tarefa findTarefaById(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }
}
