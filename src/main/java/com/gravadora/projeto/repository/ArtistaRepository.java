package com.gravadora.projeto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gravadora.projeto.model.Artista;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    // 🔍 Consulta 1: Buscar artistas por nacionalidade
    List<Artista> findByDcNaconalidade(String dcNaconalidade);

    // 🔍 Consulta 2: Buscar artistas por gênero musical
    List<Artista> findByDcGeneroMusical(String dcGeneroMusical);

}
