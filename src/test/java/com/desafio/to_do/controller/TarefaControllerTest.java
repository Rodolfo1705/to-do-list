package com.desafio.to_do.controller;

import com.desafio.to_do.model.dto.request.TarefaRequest;
import com.desafio.to_do.model.dto.request.TarefaUpdateRequest;
import com.desafio.to_do.model.dto.response.TarefaResponse;
import com.desafio.to_do.model.entity.Tarefa;
import com.desafio.to_do.model.enums.StatusTarefa;
import com.desafio.to_do.service.TarefaService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TarefaControllerTest {
    private MockMvc mockMvc;
    private RequestBuilder requestBuilder;

    @InjectMocks
    private TarefaController tarefaController;
    @Mock
    private TarefaService tarefaService;

    private Long id = 1L;
    private final List<TarefaResponse> tarefas = new ArrayList<>();
    private TarefaRequest tarefaRequest;
    private TarefaResponse tarefaResponse;
    private TarefaUpdateRequest tarefaUpdateRequest;

    @Before
    public void setUp() {
        mockMvc = createMockMvc();
        requestBuilder = createRequestBuilder();

        tarefaRequest = createTarefaRequest();
        tarefaResponse = createTarefaResponse();
        tarefaUpdateRequest = createTarefaUpdateRequest();
    }

    @Test
    @Order(1)
    @DisplayName("Chamar endpoint para salvar uma tarefa com sucesso.")
    public void criarTarefaSuccessfully() throws Exception {
        when(tarefaService.saveTarefa(tarefaRequest)).thenReturn(id);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.header().exists("Location"))
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/tarefas/" + id));

        verify(tarefaService, times(1)).saveTarefa(tarefaRequest);
    }

    @Test
    @Order(2)
    @DisplayName("Chamar endpoint para resgatar todas as tarefas.")
    public void getAllTarefasSuccessfully() {
        when(tarefaService.getAllTarefas()).thenReturn(tarefas);

        ResponseEntity<List<TarefaResponse>> response = tarefaController.getAllTarefas();

        verify(tarefaService, times(1)).getAllTarefas();
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(tarefas, response.getBody());
    }

    @Test
    @Order(3)
    @DisplayName("Chamar endpoint para resgatar uma tarefa por id.")
    public void getTarefaSuccessfully() {
        when(tarefaService.getTarefa(id)).thenReturn(tarefaResponse);

        ResponseEntity<TarefaResponse> response = tarefaController.getTarefa(id);

        verify(tarefaService, times(1)).getTarefa(id);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(tarefaResponse, response.getBody());
    }

    @Test
    @Order(4)
    @DisplayName("Chamar endpoint para atualizar o status de uma tarefa por id.")
    public void updateTarefaSuccessfully() {
        doNothing().when(tarefaService).updateTarefa(id, tarefaUpdateRequest);

        ResponseEntity<Void> response = tarefaController.updateTarefa(id, tarefaUpdateRequest);

        verify(tarefaService, times(1)).updateTarefa(id, tarefaUpdateRequest);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(5)
    @DisplayName("Chamar endpoint para deletar tarefa lógicamente")
    public void deleteTarefaSuccessfully() {
        doNothing().when(tarefaService).deleteTarefa(id);

        ResponseEntity<Void> response = tarefaController.deleteTarefa(id);

        verify(tarefaService, times(1)).deleteTarefa(id);
        Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }



    private MockMvc createMockMvc() {
        return MockMvcBuilders.standaloneSetup(tarefaController).build();
    }

    private RequestBuilder createRequestBuilder() {
        return MockMvcRequestBuilders
                .post("/api/tarefas")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"titulo\":\"Título\"," +
                        "    \"descricao\": \"Descrição\"" +
                        "}"
                );
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

    private TarefaResponse createTarefaResponse() {
        return new TarefaResponse(
                id,
                "Título",
                "Descrição",
                StatusTarefa.PENDENTE.getDescricao()
        );
    }

    private TarefaUpdateRequest createTarefaUpdateRequest() {
        return new TarefaUpdateRequest(
                "CONCLUIDA"
        );
    }
}
