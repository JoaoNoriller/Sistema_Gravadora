package com.gravadora.demo;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.repository.ArtistaRepository;
import com.gravadora.projeto.service.ArtistaService;

@ExtendWith(MockitoExtension.class) // Ativa o Mockito para testes unitários
class ArtistaServiceTest {

    @Mock
    private ArtistaRepository artistaRepository;

    @InjectMocks
    private ArtistaService artistaService;

    private Artista artista;

    @BeforeEach
    void setup() {
        artista = new Artista();
        artista.setIdArtista(1L);
        artista.setDcNome("João Victor");
    }

    // Cenário 1 — Cadastro com sucesso
    @Test
    void deveCadastrarArtistaComSucesso() {
        when(artistaRepository.findByDcNome("João Victor")).thenReturn(Collections.emptyList());
        when(artistaRepository.save(artista)).thenReturn(artista);

        Artista salvo = artistaService.salvar(artista);

        assertNotNull(salvo);
        assertEquals("João Victor", salvo.getDcNome());
    }

    // Cenário 2 — Buscar artista inexistente
    @Test
    void deveRetornarErroAoBuscarArtistaInexistente() {
        when(artistaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            artistaService.buscarPorId(99L);
        });

        assertEquals("Artista não encontrado.", ex.getMessage());
    }

    // Cenário 4 – Atualizar nome do artista
    @Test
    void deveAtualizarNomeDoArtista() {
        Artista atualizado = new Artista();
        atualizado.setIdArtista(1L);
        atualizado.setDcNome("Nome Atualizado");

        when(artistaRepository.save(atualizado)).thenReturn(atualizado);
        when(artistaRepository.findByDcNome("Nome Atualizado")).thenReturn(Collections.emptyList());

        Artista retorno = artistaService.salvar(atualizado);

        assertEquals("Nome Atualizado", retorno.getDcNome());
    }

    // Cenário 5 – Listar todos
    @Test
    void deveListarTodosOsArtistas() {
        when(artistaRepository.findAll()).thenReturn(Arrays.asList(artista));

        List<Artista> lista = artistaService.listarTodos();

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }
}