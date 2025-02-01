package com.desafio.to_do.model.entity;

import com.desafio.to_do.model.enums.StatusTarefa;
import jakarta.persistence.*;

@Entity
@Table(name = "tarefas")
public class Tarefa {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column
    private String descricao;

    @Column(nullable = false)
    private String status = StatusTarefa.PENDENTE.getDescricao();

    public Tarefa() {}

    public Long getId() {
        return this.id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status.getDescricao();
    }
}
