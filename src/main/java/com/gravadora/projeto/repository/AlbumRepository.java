package com.gravadora.projeto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByArtista(Artista artista);

    // Buscar álbuns por status
    List<Album> findByDcStatus(String dcStatus);

    // Buscar álbuns pelo título (contendo parte do nome)
    List<Album> findByDcTituloContaining(String titulo);

    // Verificar se um artista já possui um álbum com o mesmo título
    List<Album> findByDcTituloAndArtistaIdArtista(String dcTitulo, Long idArtista);

    // Contar álbuns que um artista lançou em um determinado ano
    int countByArtistaIdArtistaAndDtAnoLancamento(Long idArtista, LocalDate dtAnoLancamento);
}
