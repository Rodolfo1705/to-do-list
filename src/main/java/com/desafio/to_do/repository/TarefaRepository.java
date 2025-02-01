package com.desafio.to_do.repository;

import com.desafio.to_do.model.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    @Query("SELECT t FROM Tarefa t WHERE t.status <> 'Excluída'")
    List<Tarefa> findAll();

    @Query("SELECT t FROM Tarefa t WHERE t.id = :id AND t.status <> 'Excluída'")
    Optional<Tarefa> findById(Long id);
}
