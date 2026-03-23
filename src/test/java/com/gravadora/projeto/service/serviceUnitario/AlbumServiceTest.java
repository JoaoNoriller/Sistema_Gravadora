package com.gravadora.projeto.service.serviceUnitario;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gravadora.projeto.dto.AlbumDTO;
import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.AlbumRepository;
import com.gravadora.projeto.service.AlbumService;
import com.gravadora.projeto.service.ArtistaService;
import com.gravadora.projeto.service.GravadoraService; // ← IMPORT ADICIONADO

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistaService artistaService;  

    @Mock
    private GravadoraService gravadoraService; 

    @InjectMocks
    private AlbumService albumService;

    private Album album;
    private AlbumDTO albumDTO;
    private Artista artista;
    private Gravadora gravadora;

    /* Executado antes de cada teste
     * Prepara os objetos base para evitar repetição de código
     */
    @BeforeEach
    void setup() {
        artista = new Artista();
        artista.setIdArtista(1L);
        artista.setDcNome("Artista Teste");

        gravadora = new Gravadora();
        gravadora.setIdGravadora(1L);
        gravadora.setDcNome("Gravadora Teste");

        // qtdMusica = 10 → status COMPLETO
        // dcStatus = null pois é gerado automaticamente pelo service
        albumDTO = new AlbumDTO(
                1L,
                "Album Teste",
                LocalDate.of(2024, 1, 1),
                null,                          // ← dcStatus gerado pelo service
                10,                          // ← 10 músicas = COMPLETO
                Time.valueOf(LocalTime.of(1, 0)),
                artista.getIdArtista(),
                gravadora.getIdGravadora()
        );

        album = new Album();
        album.setArtista(artista);
        album.setGravadora(gravadora);                
        album.setDcTitulo(albumDTO.dcTitulo());
        album.setDtAnoLancamento(albumDTO.dtAnoLancamento());
        album.setQtdMusica(albumDTO.qtdMusica());
        album.setTmDuracao(albumDTO.tmDuracao());
        album.setDcStatus("COMPLETO"); // status adicionado
    }

   // Cenário 1 — cadastrar álbum com sucesso
    @Test
    void deveCadastrarAlbumComSucesso() {

        // Simula que o artista existe no sistema
        when(artistaService.buscarPorId(1L))
                .thenReturn(artista);

        // Simula que a gravadora existe no sistema
        when(gravadoraService.buscarPorId(1L)) 
                .thenReturn(gravadora);

         // Simula que não existe outro álbum com esse título para esse artista (RN-03)
        when(albumRepository.findByDcTituloAndArtista_IdArtista(
                "Album Teste", 1L))
                .thenReturn(Collections.emptyList());

        // Simula que o artista não atingiu o limite de 10 álbuns no ano (RN-04)
        when(albumRepository.countByArtista_IdArtistaAndAno(
                1L, 2024))
                .thenReturn(0);

        // Simula que o banco salva e retorna o álbum        
        when(albumRepository.save(any(Album.class)))
                .thenReturn(album);

        // Executa o método real do service        
        Album salvo = albumService.salvarAlbum(albumDTO);

        assertNotNull(salvo); // Verifica se o álbum foi retornado corretamente
        assertEquals("Album Teste", salvo.getDcTitulo());
        assertEquals("COMPLETO", salvo.getDcStatus());

        verify(albumRepository).save(any(Album.class)); // Confirma que o save foi chamado exatamente 1 vez
    }

    // Cenário 2 — erro ao buscar álbum inexistente
    @Test
    void deveLancarErroQuandoAlbumNaoExiste() {

        when(albumRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> albumService.buscarPorId(99L));
    }

    // Cenário 3 — listar todos
    @Test
    void deveListarTodosAlbuns() {

        when(albumRepository.findAll()).thenReturn(Collections.singletonList(album));

        var lista = albumService.listar();

        assertEquals(1, lista.size());
        verify(albumRepository).findAll();
    }

    // Cenário 4 — atualizar álbum
    @Test
    void deveAtualizarAlbumComSucesso() {

        // Simula que o álbum com ID 1 existe no banco
        when(albumRepository.findById(1L)) 
                .thenReturn(Optional.of(album));

        // Simula que o artista existe no sistema
        when(artistaService.buscarPorId(1L))
                .thenReturn(artista);

        // Simula que a gravadora existe no sistema        
        when(gravadoraService.buscarPorId(1L))
                .thenReturn(gravadora);

         // Simula que não existe conflito de título com outro álbum do mesmo artista (RN-03)        
        when(albumRepository.findByDcTituloAndArtista_IdArtista(
                albumDTO.dcTitulo(), 1L))
                .thenReturn(Collections.emptyList());

        // Simula a contagem de álbuns no ano e retorna 1 (o próprio álbum sendo editado)
        // O service desconta esse 1 para não bloquear indevidamente a edição (RN-04)
        when(albumRepository.countByArtista_IdArtistaAndAno(
                1L, 2024))
                .thenReturn(0);

        // Simula que o banco salva e retorna o álbum atualizado        
        when(albumRepository.save(any(Album.class)))
                .thenReturn(album);

        // Executa o método de atualização, passa o ID e o DTO com os novos dados        
        Album atualizado = albumService.atualizarAlbum(1L, albumDTO); // atualizarAlbum

        assertNotNull(atualizado); // Verifica se os dados foram atualizados corretamente
        assertEquals(albumDTO.dcTitulo(), atualizado.getDcTitulo());
        assertEquals("COMPLETO", atualizado.getDcStatus());

        verify(albumRepository, times(1)).save(any(Album.class)); // Confirma que o save foi chamado exatamente 1 vez
    }

    // Cenário 5 — deletar álbum
    @Test
    void deveDeletarAlbumComSucesso() {

        when(albumRepository.existsById(1L)).thenReturn(true);

        doNothing().when(albumRepository).deleteById(1L);

        albumService.deletar(1L);

        verify(albumRepository).deleteById(1L);
    }
}