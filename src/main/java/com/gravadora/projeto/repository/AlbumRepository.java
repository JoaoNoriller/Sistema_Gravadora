package com.gravadora.projeto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Lista o artista dentro de álbum
    List<Album> findByArtista(Artista artista);

    // Buscar álbuns por status
    List<Album> findByDcStatus(String dcStatus);

    // Buscar álbuns pelo título (contendo parte do nome)
    List<Album> findByDcTitulo(String titulo);

    // Verificar se um artista já possui um álbum com o mesmo título
    List<Album> findByDcTituloEIdArtista(String dcTitulo, Long idArtista);

    // Contar álbuns que um artista lançou em um determinado ano
    int countByIdArtistaEDtAnoLancamento(Long idArtista, LocalDate dtAnoLancamento);
}
