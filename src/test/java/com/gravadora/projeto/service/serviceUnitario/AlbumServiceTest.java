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
import com.gravadora.projeto.repository.ArtistaRepository;
import com.gravadora.projeto.service.AlbumService;
import com.gravadora.projeto.service.ArtistaService;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito para os testes
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository; // Cria um repositório falso de álbum

    @Mock
    private ArtistaRepository artistaRepository; // Cria um repositório falso de artista

    @Mock
    private ArtistaService artistaService; // Cria um service falso de artista

    @InjectMocks
    private AlbumService albumService; // Cria instância real do service usando os repositórios falsos

    private Album album;
    private AlbumDTO albumDTO;
    private Artista artista;
    private Gravadora gravadora;

    @BeforeEach // Roda antes de cada teste
    void setup() {
        artista = new Artista(); // Mockado
        artista.setIdArtista(1L);
        artista.setDcNome("Artista Teste");

        gravadora = new Gravadora();
        gravadora.setIdGravadora(1L);
        gravadora.setDcNome("Gravadora Teste");

        albumDTO = new AlbumDTO(1L, "Album Teste",
                LocalDate.of(2024, 1, 1), 6,
                Time.valueOf(LocalTime.of(1, 0)), artista.getIdArtista(), gravadora.getIdGravadora());

        album = new Album();
        album.setArtista(artista);
        album.setDcTitulo(albumDTO.dcTitulo());
        album.setDtAnoLancamento(albumDTO.dtAnoLancamento());
        album.setQtdMusica(albumDTO.qtdMusica());
        album.setTmDuracao(albumDTO.tmDuracao());
    }

    // Cenário 1 — cadastrar álbum com sucesso
    @Test
    void deveCadastrarAlbumComSucesso() {

        when(artistaService.buscarPorId(1L))
                .thenReturn(artista);

        when(artistaRepository.findById(1L))
                .thenReturn(Optional.of(artista));

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

        verify(albumRepository, times(1)).save(any(Album.class));
    }

    // Cenário 2 – erro ao buscar álbum inexistente
    @Test
    void deveLancarErroQuandoAlbumNaoExiste() {

        when(albumRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> albumService.buscarPorId(99L)); // Espera erro
    }

    // Cenário 3 – listar todos
    @Test
    void deveListarTodosAlbuns() {

        when(albumRepository.findAll()).thenReturn(Collections.singletonList(album));

        var lista = albumService.listar();

        assertEquals(1, lista.size()); // Deve retornar lista com 1 item
        verify(albumRepository).findAll();
    }

    // Cenário 4 – atualizar álbum
    @Test
    void deveAtualizarAlbumComSucesso() {

        when(artistaService.buscarPorId(1L))
                .thenReturn(artista);

        when(artistaRepository.findById(1L))
                .thenReturn(Optional.of(artista));

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

        verify(albumRepository, times(1)).save(any(Album.class));
    }

    // Cenário 5 – deletar álbum
    @Test
    void deveDeletarAlbumComSucesso() {

        when(albumRepository.existsById(1L)).thenReturn(true); // Simula existência

        doNothing().when(albumRepository).deleteById(1L);

        albumService.deletar(1L);

        verify(albumRepository).deleteById(1L); // Verifica se deleteById foi chamado
    }
}
