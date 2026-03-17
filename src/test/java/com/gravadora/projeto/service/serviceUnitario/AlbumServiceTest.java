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
    private ArtistaService artistaService;   // ← mantido

    @Mock
    private GravadoraService gravadoraService; // ← ADICIONADO (ArtistaRepository removido)

    @InjectMocks
    private AlbumService albumService;

    private Album album;
    private AlbumDTO albumDTO;
    private Artista artista;
    private Gravadora gravadora;

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
                null,                                   // ← dcStatus gerado pelo service
                10,                                     // ← 10 músicas = COMPLETO
                Time.valueOf(LocalTime.of(1, 0)),
                artista.getIdArtista(),
                gravadora.getIdGravadora()
        );

        album = new Album();
        album.setArtista(artista);
        album.setGravadora(gravadora);                  // ← gravadora adicionada
        album.setDcTitulo(albumDTO.dcTitulo());
        album.setDtAnoLancamento(albumDTO.dtAnoLancamento());
        album.setQtdMusica(albumDTO.qtdMusica());
        album.setTmDuracao(albumDTO.tmDuracao());
        album.setDcStatus("COMPLETO");                  // ← status adicionado
    }

    // Cenário 1 — cadastrar álbum com sucesso
    @Test
    void deveCadastrarAlbumComSucesso() {

        when(artistaService.buscarPorId(1L))
                .thenReturn(artista);

        when(gravadoraService.buscarPorId(1L))          // ← mock da gravadora
                .thenReturn(gravadora);

        when(albumRepository.countByArtista_IdArtistaAndDtAnoLancamento(
                1L, albumDTO.dtAnoLancamento()))
                .thenReturn(0);

        when(albumRepository.findByDcTituloAndArtista_IdArtista(
                "Album Teste", 1L))
                .thenReturn(Collections.emptyList());

        when(albumRepository.save(any(Album.class)))
                .thenReturn(album);

        Album salvo = albumService.salvarAlbum(albumDTO);

        assertNotNull(salvo);
        assertEquals("Album Teste", salvo.getDcTitulo());
        assertEquals("COMPLETO", salvo.getDcStatus());  // ← assert de status

        verify(albumRepository, times(1)).save(any(Album.class));
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

        when(artistaService.buscarPorId(1L))
                .thenReturn(artista);

        when(gravadoraService.buscarPorId(1L))          // ← mock da gravadora
                .thenReturn(gravadora);

        when(albumRepository.countByArtista_IdArtistaAndDtAnoLancamento(
                1L, albumDTO.dtAnoLancamento()))
                .thenReturn(0);

        when(albumRepository.findByDcTituloAndArtista_IdArtista(
                albumDTO.dcTitulo(), 1L))
                .thenReturn(Collections.emptyList());

        when(albumRepository.save(any(Album.class)))
                .thenReturn(album);

        Album atualizado = albumService.salvarAlbum(albumDTO);

        assertNotNull(atualizado);
        assertEquals(albumDTO.dcTitulo(), atualizado.getDcTitulo());
        assertEquals("COMPLETO", atualizado.getDcStatus()); // ← assert de status

        verify(albumRepository, times(1)).save(any(Album.class));
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