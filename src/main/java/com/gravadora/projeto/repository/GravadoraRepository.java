package com.gravadora.projeto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gravadora.projeto.model.Gravadora;

public interface GravadoraRepository extends JpaRepository<Gravadora, Long> {

    //Consulta 1: Buscar gravadora pelo país
    List<Gravadora> findByDcPais(String dcPais);

    //Consulta 2: Buscar gravadora pelo
    List<Gravadora> findByDcNomeContaining(String nome);

}
