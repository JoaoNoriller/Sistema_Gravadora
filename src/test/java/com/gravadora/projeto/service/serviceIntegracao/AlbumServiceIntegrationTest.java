package com.gravadora.projeto.service.serviceIntegracao;

import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gravadora.projeto.dto.AlbumDTO;
import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.AlbumRepository;
import com.gravadora.projeto.repository.ArtistaRepository;
import com.gravadora.projeto.repository.GravadoraRepository;
import com.gravadora.projeto.service.AlbumService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class AlbumServiceIntegrationTest {

   @Autowired
   private AlbumService albumService;

   @Autowired
   private AlbumRepository albumRepository;

   @Autowired
   private ArtistaRepository artistaRepository;

   @Autowired
   private GravadoraRepository gravadoraRepository;

   
   private AlbumDTO albumDTO;
   private Artista artista;
   private Gravadora gravadora;
   // private Gravadora gravadora;

   @BeforeEach
   void setup() {

      artista = new Artista();
      artista.setDcNome("Coldplay");
      artista = artistaRepository.save(artista);

      
      gravadora = new Gravadora();
      gravadora.setDcNome("Records");
      gravadora.setDcEndereco("Rua das Flores, 100");
      gravadora.setDcTelefone("11999999999"); gravadora.setDcPais("Brasil");
      gravadora.setDtDataFundacao(LocalDate.of(2000, 1, 1));
      gravadora.setDcCnpj("12345678000199"); gravadoraRepository.save(gravadora);
      

      /*
       * Album album = new Album();
       * album.setDcTitulo("Parachutes");
       * album.setDtAnoLancamento(LocalDate.of(2000, 7, 10));
       * album.setQtdMusica(10); album.setTmDuracao(Time.valueOf("01:30:00"));
       */

       albumDTO = new AlbumDTO(1L, "Parachutes",
            LocalDate.of(200, 7, 10), 6, Time.valueOf("01:30:00"), artista.getIdArtista(), gravadora.getIdGravadora());

   }

   @Test
   public void deveSalvarAlbumComSucesso() {

      Album salvo = albumService.salvarAlbum(albumDTO);

      assertNotNull(salvo.getIdAlbum());
      assertEquals("Parachutes", salvo.getDcTitulo());
   }
/* 
   @Test
   public void naoDeveSalvarAlbumComMenosDe5Musicas() {

      AlbumDTO albumDTO = new AlbumDTO(1L, "Parachutes",
            LocalDate.of(200, 7, 10), 4, Time.valueOf("01:30:00"), 2L, 3L);

      RuntimeException ex = assertThrows(RuntimeException.class,
            () -> albumService.salvarAlbum(album));
      assertEquals("O álbum deve ter mais de 5 músicas.", ex.getMessage());
   }

   @Test
   public void naoDeveSalvarTituloInvalido() { // Menos de 3 caracteres

      RuntimeException ex = assertThrows(RuntimeException.class,
            () -> albumService.salvarAlbum(album));
      assertEquals("O título do álbum deve ter no mínimo 3 caracteres.", ex.getMessage());
   }

   @Test
   public void naoDeveSalvarComDuracaoMaiorQue2Horas() {

      RuntimeException ex = assertThrows(RuntimeException.class,
            () -> albumService.salvarAlbum(album));
      assertEquals("A duração total do álbum não pode ultrapassar 2 horas.", ex.getMessage());
   }

   @Test
   public void naoDeveSalvarAlbumComTituloDuplicadoParaMesmoArtista() {

      albumService.salvarAlbum(album1);

      // Segundo álbum com mesmo título
      Album album2 = new Album();
      album2.setDcTitulo("Parachutes"); // mesmo nome
      album2.setDtAnoLancamento(LocalDate.of(2000, 7, 10));
      album2.setQtdMusica(8);
      album2.setTmDuracao(Time.valueOf("01:10:00"));
      album2.setArtista(artista);
      album2.setGravadora(gravadora);

      RuntimeException ex = assertThrows(RuntimeException.class,
            () -> albumService.salvarAlbum(album2));
      assertEquals("Este artista já possui um álbum com esse título.", ex.getMessage());

   }

   @Test
   public void naoDevePermitirMaisDe10AlbunsNoMesmoAno() {

      Artista artista = new Artista();
      artista.setDcNome("Coldplay");
      artista = artistaRepository.save(artista);

      Gravadora gravadora = new Gravadora();
      gravadora.setDcNome("Records");
      gravadora.setDcEndereco("Rua das Flores, 100");
      gravadora.setDcTelefone("11999999999");
      gravadora.setDcPais("Brasil");
      gravadora.setDtDataFundacao(LocalDate.of(2000, 1, 1));
      gravadora.setDcCnpj("12345678000199");
      gravadoraRepository.save(gravadora);

      LocalDate ano = LocalDate.now();

      // Salvar 10 álbuns válidos
      for (int i = 1; i <= 10; i++) {
         Album album = new Album();
         album.setDcTitulo("Parachutes" + i);
         album.setDtAnoLancamento(ano);
         album.setQtdMusica(10);
         album.setTmDuracao(Time.valueOf("01:00:00"));
         album.setArtista(artista);
         album.setGravadora(gravadora);

         albumService.salvarAlbum(album);

      }

      Album album11 = new Album();
      album11.setDcTitulo("Parachutes11");
      album11.setDtAnoLancamento(ano);
      album11.setQtdMusica(10);
      album11.setTmDuracao(Time.valueOf("01:00:00"));
      album11.setArtista(artista);
      album11.setGravadora(gravadora);

      RuntimeException ex = assertThrows(RuntimeException.class,
            () -> albumService.salvarAlbum(album11));
      assertEquals("O artista já lançou o máximo de 10 álbuns neste ano.", ex.getMessage());

   }*/
}
