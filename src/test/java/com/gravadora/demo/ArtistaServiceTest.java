package com.gravadora.demo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.repository.ArtistaRepository;
import com.gravadora.projeto.service.ArtistaService;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito para os testes
class ArtistaServiceTest {

    @Mock
    private ArtistaRepository artistaRepository; // Cria um repositório falso de artista

    @InjectMocks
    private ArtistaService artistaService;  // Cria instância real do service usando os repositórios falsos

    private Artista artista;

    @BeforeEach // Roda antes de cada teste
    void setup() { 
        artista = new Artista();
        artista.setIdArtista(1L);
        artista.setDcNome("João Victor");
    }

    // Cenário 1 — Cadastro com sucesso
    @Test
    void deveCadastrarArtistaComSucesso() {
        when(artistaRepository.findByDcNome("João Victor")).thenReturn(Collections.emptyList()); //verifica se não existe nenhum artista com esse nome
        when(artistaRepository.save(artista)).thenReturn(artista); //Simula que o save retorna exatamente o artista enviado

        Artista salvo = artistaService.salvar(artista);

        assertNotNull(salvo); //Validações
        assertEquals("João Victor", salvo.getDcNome());
    }

    // Cenário 2 — Buscar artista inexistente
    @Test
    void deveRetornarErroAoBuscarArtistaInexistente() { //Simula que NÃO encontrou ninguém
        when(artistaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> { //Deve lançar erro ao tentar buscar
            artistaService.buscarPorId(99L);
        });

        assertEquals("Artista não encontrado.", ex.getMessage()); //Verifica se a mensagem do erro está correta
    }

    // Cenário 4 – Atualizar nome do artista
    @Test
    void deveAtualizarNomeDoArtista() {
        Artista atualizado = new Artista(); //Cria artista atualizado
        atualizado.setIdArtista(1L);
        atualizado.setDcNome("Nome Atualizado");

        when(artistaRepository.save(atualizado)).thenReturn(atualizado); //Simula que o repositório vai salvar e devolver o atualizado
        when(artistaRepository.findByDcNome("Nome Atualizado")).thenReturn(Collections.emptyList()); //Simula que não há outro artista com o novo nome

        Artista retorno = artistaService.salvar(atualizado);

        assertEquals("Nome Atualizado", retorno.getDcNome()); //Validação
    }

    // Cenário 5 – Listar todos
    @Test
    void deveListarTodosOsArtistas() { //Simula que o banco contém apenas 1 artista
        when(artistaRepository.findAll()).thenReturn(Arrays.asList(artista));

        List<Artista> lista = artistaService.listarTodos();

        assertFalse(lista.isEmpty()); //Lista não deve estar vazia
        assertEquals(1, lista.size());
    }
}