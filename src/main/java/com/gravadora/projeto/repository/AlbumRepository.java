package com.gravadora.projeto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Lista de álbuns de um artista
    List<Album> findByArtista(Artista artista);

    // Lista de álbuns de um artista (por ID)
    List<Album> findByArtista_IdArtista(Long idArtista);

    // Buscar álbuns por status
    List<Album> findByDcStatus(String dcStatus);

    // Buscar álbuns pelo título (contendo o nome)
    List<Album> findByDcTituloContaining(String titulo);

    // Verificar se o artista já possui um álbum com o mesmo título
    List<Album> findByDcTituloAndArtista_IdArtista(String dcTitulo, Long idArtista);

    // Contar álbuns lançados por um artista em determinado ano // Método antigo, foi mantido para não quebrar nada, mas não usado na RN-04
    int countByArtista_IdArtistaAndDtAnoLancamento(Long idArtista, LocalDate dtAnoLancamento);

     // RN-04: Contar álbuns lançados por um artista em determinado ANO
    // Corrigido para contar por ano inteiro e não por data exata
    @Query("SELECT COUNT(a) FROM Album a WHERE a.artista.idArtista = :idArtista AND YEAR(a.dtAnoLancamento) = :ano")
    int countByArtista_IdArtistaAndAno(@Param("idArtista") Long idArtista, @Param("ano") int ano);
}
    // ===================================================================
    // INFORMAÇÃO SOMPRE O MÉTODO UTILIZADO
    // ===================================================================

    /*@Param É quem faz a ligação entre o parâmetro do método Java e o :placeholder dentro da @query */
    /*Utilizei a @query para extrair somente o ano da data, pois antes o Spring não conseguia fazer*/

    /*O :placeholder é um padrão de segurança, o valor nunca entra direto na query como texto, ele 
    é tratado pelo Hibernate antes de chegar no banco, impedindo ataques de SQL Injection */