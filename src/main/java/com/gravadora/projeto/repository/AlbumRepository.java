package com.gravadora.projeto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Lista de álbuns de um artista
    List<Album> findByArtista(Artista artista);

    // Buscar álbuns por status
    List<Album> findByDcStatus(String dcStatus);

    // Buscar álbuns pelo título (contendo o nome)
    List<Album> findByDcTituloContaining(String titulo);

    // Verificar se o artista já possui um álbum com o mesmo título
    List<Album> findByDcTituloAndArtista_IdArtista(String dcTitulo, Long idArtista);

    // Contar álbuns lançados por um artista em determinado ano
    int countByArtista_IdArtistaAndDtAnoLancamento(Long idArtista, LocalDate dtAnoLancamento);
}
