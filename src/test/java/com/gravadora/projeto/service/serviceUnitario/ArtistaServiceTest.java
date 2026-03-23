package com.gravadora.projeto.service.serviceUnitario;

import java.time.LocalDate;
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
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gravadora.projeto.dto.ArtistaDTO;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.repository.ArtistaRepository;
import com.gravadora.projeto.service.ArtistaService;

@SuppressWarnings("unused") //Deve ignorar avisos de código não utilizado
@ExtendWith(MockitoExtension.class) // Habilita o Mockito para os testes
class ArtistaServiceTest {

    @Mock
    private ArtistaRepository artistaRepository; // Cria um repositório falso de artista

    @InjectMocks
    private ArtistaService artistaService;  // Cria instância real do service usando os repositórios falsos

    private Artista artista;

    private ArtistaDTO artistaDTO;
    private ArtistaDTO artistaAtualizadoDTO;

    /* Executado antes de cada teste
     * Prepara os objetos base para evitar repetição de código
     */
    @BeforeEach 
    void setup() { 
        artista = new Artista();
        artista.setIdArtista(1L);
        artista.setDcNome("Xuxa");
        artista.setDtNascimento(LocalDate.of(1963, 3, 27));
        artista.setDcNacionalidade("Brasil");
        artista.setDcGeneroMusical("Pop");

        artistaDTO = new ArtistaDTO("Xuxa", "Barra da Tijuca", 
        LocalDate.of(1963, 3, 27), "Brasil", "Pop");

        artistaAtualizadoDTO = new ArtistaDTO("Neri", "Tijucas, 45", 
        LocalDate.of(1960, 2, 22), "Brasil", "Pop");
    }

    // Cenário 1 — Cadastro com sucesso
    @Test
    void deveCadastrarArtistaComSucesso() {

        when(artistaRepository.findByDcNome("Xuxa")).thenReturn(Collections.emptyList()); //verifica se não existe nenhum artista com esse nome
        when(artistaRepository.save(any(Artista.class))).thenReturn(artista); //Simula que o save retorna exatamente o artista enviado
        
        Artista salvo = artistaService.salvar(artistaDTO);

        assertNotNull(salvo);
        assertEquals("Xuxa", salvo.getDcNome());
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

    // Cenário 4 – Atualizar artista
    @Test
    void deveAtualizarArtistaComSucesso() {

        Artista artistaAtualizado = new Artista();
        artistaAtualizado.setIdArtista(1L);
        artistaAtualizado.setDcNome(artistaAtualizadoDTO.dcNome());
        artistaAtualizado.setDcEndereco(artistaAtualizadoDTO.dcEndereco());
        artistaAtualizado.setDtNascimento(artistaAtualizadoDTO.dtNascimento());
        artistaAtualizado.setDcNacionalidade(artistaAtualizadoDTO.dcNacionalidade());
        artistaAtualizado.setDcGeneroMusical(artistaAtualizadoDTO.dcGeneroMusical());

        // Simula que o artista existe no banco
        when(artistaRepository.findById(1L))
                .thenReturn(Optional.of(artista));

        // ← "Neri" é o nome do DTO de atualização — deve bater com o que o service chama
        when(artistaRepository.findByDcNome("Neri"))
                .thenReturn(Collections.emptyList());

        // Simula o save retornando o artista atualizado
        when(artistaRepository.save(any(Artista.class)))
                .thenReturn(artistaAtualizado);

        Artista retorno = artistaService.atualizar(1L, artistaAtualizadoDTO);

        assertNotNull(retorno);
        assertEquals("Neri", retorno.getDcNome());
        assertEquals("Tijucas, 45", retorno.getDcEndereco());
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