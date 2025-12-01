package com.gravadora.projeto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gravadora.projeto.model.Album;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    //Consulta 1: Buscar álbuns pelo status (Ex.: “ATIVO”, “INATIVO”)
    List<Album> findByDcStatus(String dcStatus);

    //Consulta 2: Buscar álbuns pelo título contendo parte do nome
    List<Album> findByDcTituloContaining(String titulo);

}
