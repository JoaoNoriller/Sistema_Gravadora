package com.gravadora.demo;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.repository.AlbumRepository;
import com.gravadora.projeto.repository.ArtistaRepository;
import com.gravadora.projeto.service.AlbumService;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito para os testes
class AlbumServiceTest {

    @Mock
    private AlbumRepository albumRepository; // Cria um repositório falso de álbum

    @Mock
    private ArtistaRepository artistaRepository; // Cria um repositório falso de artista

    @InjectMocks
    private AlbumService albumService; // Cria instância real do service usando os repositórios falsos

    private Album album;
    private Artista artista;

    @BeforeEach // Roda antes de cada teste
    void setup() {
        artista = new Artista(); // abm e art Mockado//
        artista.setIdArtista(1L);
        artista.setDcNome("Artista Teste");

        album = new Album();
        album.setIdAlbum(1L);
        album.setDcTitulo("Album Teste");
        album.setQtdMusica(6); // obrigatório > 5
        album.setTmduracao(Time.valueOf(LocalTime.of(1, 0))); // 1h
        album.setDtAnoLancamento(LocalDate.of(2024, 1, 1)); //Data 
        album.setArtista(artista);
    }

    // Cenário 1 — cadastrar álbum com sucesso
    @Test
    void deveCadastrarAlbumComSucesso() {

        when(artistaRepository.findById(1L)).thenReturn(Optional.of(artista)); //artista existente no banco
        when(albumRepository.countByArtista_IdArtistaAndDtAnoLancamento(1L, album.getDtAnoLancamento())) //validação — artista ainda não tem álbum neste ano
                .thenReturn(0); 
        when(albumRepository.findByDcTituloAndArtista_IdArtista("Album Teste", 1L)) //validação — título não existe repetido para o mesmo artista
                .thenReturn(Collections.emptyList()); // título liberado

        when(albumRepository.save(album)).thenReturn(album); //salva

        Album salvo = albumService.salvarAlbum(album);

        assertNotNull(salvo);
        assertEquals("Album Teste", salvo.getDcTitulo()); // Confirma título

        verify(albumRepository, times(1)).save(album); // Confirma que o save foi chamado
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

        album.setDcTitulo("Novo Título");
        
        when(artistaRepository.findById(1L)).thenReturn(Optional.of(artista)); // Regras de validação do service
        when(albumRepository.countByArtista_IdArtistaAndDtAnoLancamento(1L, album.getDtAnoLancamento()))
                .thenReturn(0);
        when(albumRepository.findByDcTituloAndArtista_IdArtista("Novo Título", 1L))
                .thenReturn(Collections.emptyList());

        when(albumRepository.save(album)).thenReturn(album); //Simula salvamento

        Album atualizado = albumService.salvarAlbum(album);

        assertEquals("Novo Título", atualizado.getDcTitulo()); // Confirma atualização
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
