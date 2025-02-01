package com.desafio.to_do.service;

import com.desafio.to_do.model.dto.request.TarefaRequest;
import com.desafio.to_do.model.dto.request.TarefaUpdateRequest;
import com.desafio.to_do.model.dto.response.TarefaResponse;
import com.desafio.to_do.model.entity.Tarefa;
import com.desafio.to_do.model.enums.StatusTarefa;
import com.desafio.to_do.repository.TarefaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TarefaServiceTest {
    @InjectMocks
    private TarefaService tarefaService;
    @Mock
    private TarefaRepository tarefaRepository;

    private Long id = 1L;
    private Tarefa tarefa;
    private final List<Tarefa> tarefas = new ArrayList<>();
    private TarefaRequest tarefaRequest;
    private TarefaUpdateRequest tarefaUpdateRequest;

    @Before
    public void setUp() {
        tarefa = createTarefa();
        tarefas.add(tarefa);
        tarefaRequest = createTarefaRequest();
        tarefaUpdateRequest = createTarefaUpdateRequest();
    }


    @Test
    @Order(1)
    @DisplayName("Salvar uma tarefa com sucesso.")
    public void saveTarefaSuccessfully() {
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        Long tarefaId = tarefaService.saveTarefa(tarefaRequest);

        verify(tarefaRepository, times(1)).save(any());
        Assert.assertNotNull(tarefaId);
        Assert.assertEquals(tarefaId, tarefa.getId());
    }

    @Test
    @Order(2)
    @DisplayName("Resgatar todas as tarefas.")
    public void getAllTarefasSuccessfully() {
        when(tarefaRepository.findAll()).thenReturn(tarefas);

        List<TarefaResponse> tarefaResponseList = tarefaService.getAllTarefas();

        verify(tarefaRepository, times(1)).findAll();
        Assert.assertNotNull(tarefaResponseList.get(0));
        Assert.assertEquals(tarefaResponseList.size(), tarefas.size());
    }

    @Test
    @Order(3)
    @DisplayName("Resgatar lista de tarefas vazia.")
    public void getAllTarefasEmpty() {
        when(tarefaRepository.findAll()).thenReturn(new ArrayList<>());

        List<TarefaResponse> tarefaResponseList = tarefaService.getAllTarefas();

        verify(tarefaRepository, times(1)).findAll();
        Assert.assertTrue(tarefaResponseList.isEmpty());
    }

    @Test
    @Order(4)
    @DisplayName("Resgatar tarefa por id.")
    public void getTarefaByIdSuccessfully() {
        when(tarefaRepository.findById(id)).thenReturn(Optional.ofNullable(tarefa));

        TarefaResponse tarefaResponse = tarefaService.getTarefa(id);

        verify(tarefaRepository, times(1)).findById(id);
        Assert.assertNotNull(tarefaResponse);
        Assert.assertEquals(tarefaResponse.id(), id);
    }

    @Test
    @Order(5)
    @DisplayName("Resgatar tarefa com id inexistente.")
    public void getTarefaByIdNotFound() {
        when(tarefaRepository.findById(id)).thenReturn(Optional.empty());

        Assert.assertThrows(EntityNotFoundException.class, () -> tarefaService.getTarefa(id));
        verify(tarefaRepository, times(1)).findById(id);
    }

    @Test
    @Order(6)
    @DisplayName("Atualizar status de uma tarefa.")
    public void updateTarefaSuccessfully() {
        when(tarefaRepository.findById(id)).thenReturn(Optional.ofNullable(tarefa));

        Assert.assertEquals(tarefa.getStatus(), "Pendente");

        tarefaService.updateTarefa(id, tarefaUpdateRequest);

        verify(tarefaRepository, times(1)).save(tarefa);
        Assert.assertEquals(tarefa.getStatus(),"Concluída");
    }

    @Test
    @Order(7)
    @DisplayName("Atualizar tarefa com id inexistente.")
    public void updateTarefaNotFound() {
        when(tarefaRepository.findById(id)).thenReturn(Optional.empty());

        Assert.assertThrows(EntityNotFoundException.class, () -> tarefaService.updateTarefa(id, tarefaUpdateRequest));
        verify(tarefaRepository, times(1)).findById(id);
    }

    @Test
    @Order(8)
    @DisplayName("Atualizar tarefa com status inválido.")
    public void updateTarefaStatusInvalido(){
        TarefaUpdateRequest tarefaUpdateRequestInvalido = new TarefaUpdateRequest("Inválido");

        when(tarefaRepository.findById(id)).thenReturn(Optional.ofNullable(tarefa));

        Assert.assertThrows(IllegalArgumentException.class, () -> tarefaService.updateTarefa(id, tarefaUpdateRequestInvalido));
        verify(tarefaRepository, times(1)).findById(id);
    }

    @Test
    @Order(9)
    @DisplayName("Deletar tarefa lógicamente.")
    public void deleteTarefaSuccessfully() {
        when(tarefaRepository.findById(id)).thenReturn(Optional.ofNullable(tarefa));

        Assert.assertEquals(tarefa.getStatus(), "Pendente");

        tarefaService.deleteTarefa(id);

        Assert.assertEquals(tarefa.getStatus(), "Excluída");
    }

    @Test
    @Order(10)
    @DisplayName("Deletar tarefa inexistente.")
    public void deleteTarefaNotFound() {
        when(tarefaRepository.findById(id)).thenReturn(Optional.empty());

        Assert.assertThrows(EntityNotFoundException.class, () -> tarefaService.deleteTarefa(id));
    }

    private Tarefa createTarefa() {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Título");
        tarefa.setDescricao("Descrição");
        tarefa.setStatus(StatusTarefa.PENDENTE);

        ReflectionTestUtils.setField(tarefa, "id", id);

        return tarefa;
    }

    private TarefaRequest createTarefaRequest() {
        return new TarefaRequest(
                "Título",
                "Descrição"
        );
    }

    private TarefaUpdateRequest createTarefaUpdateRequest() {
        return new TarefaUpdateRequest(
                "CONCLUIDA"
        );
    }

}
