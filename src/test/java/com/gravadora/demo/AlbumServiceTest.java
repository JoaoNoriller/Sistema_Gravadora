package com.gravadora.demo;

import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.repository.AlbumRepository;
import com.gravadora.projeto.repository.ArtistaRepository;
import com.gravadora.projeto.service.AlbumService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistaRepository artistaRepository;

    @InjectMocks
    private AlbumService albumService;

    private Album album;
    private Artista artista;

    @BeforeEach
    void setup() {
        artista = new Artista();
        artista.setIdArtista(1L);
        artista.setDcNome("Artista Teste");

        album = new Album();
        album.setIdAlbum(1L);
        album.setDcTitulo("Album Teste");
        album.setQtdMusica(6); // obrigatório > 5
        album.setTmduracao(Time.valueOf(LocalTime.of(1, 0))); // 1h
        album.setDtAnoLancamento(LocalDate.of(2024, 1, 1));
        album.setArtista(artista);
    }

    // ----------------------------------------------------------
    // Cenário 1 — cadastrar álbum com sucesso
    // ----------------------------------------------------------
    @Test
    void deveCadastrarAlbumComSucesso() {

        when(artistaRepository.findById(1L)).thenReturn(Optional.of(artista));
        when(albumRepository.countByArtista_IdArtistaAndDtAnoLancamento(1L, album.getDtAnoLancamento()))
                .thenReturn(0); // ano liberado
        when(albumRepository.findByDcTituloAndArtista_IdArtista("Album Teste", 1L))
                .thenReturn(Collections.emptyList()); // título liberado

        when(albumRepository.save(album)).thenReturn(album);

        Album salvo = albumService.salvarAlbum(album);

        assertNotNull(salvo);
        assertEquals("Album Teste", salvo.getDcTitulo());

        verify(albumRepository, times(1)).save(album);
    }

    // ----------------------------------------------------------
    // Cenário 2 – erro ao buscar álbum inexistente
    // ----------------------------------------------------------
    @Test
    void deveLancarErroQuandoAlbumNaoExiste() {

        when(albumRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> albumService.buscarPorId(99L));
    }

    // ----------------------------------------------------------
    // Cenário 3 – listar todos
    // ----------------------------------------------------------
    @Test
    void deveListarTodosAlbuns() {

        when(albumRepository.findAll()).thenReturn(Collections.singletonList(album));

        var lista = albumService.listar();

        assertEquals(1, lista.size());
        verify(albumRepository).findAll();
    }

    // ----------------------------------------------------------
    // Cenário 4 – atualizar álbum
    // ----------------------------------------------------------
    @Test
    void deveAtualizarAlbumComSucesso() {

        album.setDcTitulo("Novo Título");

        when(artistaRepository.findById(1L)).thenReturn(Optional.of(artista));
        when(albumRepository.countByArtista_IdArtistaAndDtAnoLancamento(1L, album.getDtAnoLancamento()))
                .thenReturn(0);
        when(albumRepository.findByDcTituloAndArtista_IdArtista("Novo Título", 1L))
                .thenReturn(Collections.emptyList());

        when(albumRepository.save(album)).thenReturn(album);

        Album atualizado = albumService.salvarAlbum(album);

        assertEquals("Novo Título", atualizado.getDcTitulo());
    }

    // ----------------------------------------------------------
    // Cenário 5 – deletar álbum
    // ----------------------------------------------------------
    @Test
    void deveDeletarAlbumComSucesso() {

        when(albumRepository.existsById(1L)).thenReturn(true);

        doNothing().when(albumRepository).deleteById(1L);

        albumService.deletar(1L);

        verify(albumRepository).deleteById(1L);
    }
}
