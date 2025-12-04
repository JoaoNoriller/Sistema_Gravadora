package com.gravadora.demo;

import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.GravadoraRepository;
import com.gravadora.projeto.service.GravadoraService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito para os testes
class GravadoraServiceTest {

    @Mock // Repositório falso
    private GravadoraRepository gravadoraRepository;

    @InjectMocks // Service real usando repositório mockado
    private GravadoraService gravadoraService;

    private Gravadora gravadora;

    @BeforeEach // Executado antes de cada teste para inicializar uma gravadora padrão
    void setup() {
        // Inicializa uma gravadora antes de cada teste
        gravadora = new Gravadora();
        gravadora.setIdGravadora(1L);
        gravadora.setDcNome("Gravadora Teste");
        gravadora.setDcEndereco("Rua ABC");
    }

    @Test
    void deveCadastrarGravadoraComSucesso() {
        when(gravadoraRepository.save(gravadora)).thenReturn(gravadora);

        Gravadora salvo = gravadoraService.salvar(gravadora);

        assertNotNull(salvo);
        assertEquals("Gravadora Teste", salvo.getDcNome());
        verify(gravadoraRepository, times(1)).save(gravadora);
    }

    @Test
    void deveBuscarGravadoraPorId() {
        when(gravadoraRepository.findById(1L)).thenReturn(Optional.of(gravadora));

        Gravadora resultado = gravadoraService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Gravadora Teste", resultado.getDcNome());
    }

    @Test
    void deveListarTodasAsGravadoras() {
        when(gravadoraRepository.findAll()).thenReturn(Arrays.asList(gravadora));

        var lista = gravadoraService.listarTodos();

        assertEquals(1, lista.size());
        verify(gravadoraRepository, times(1)).findAll();
    }

    @Test
    void deveAtualizarGravadora() {
        // Mudança antes de salvar
        gravadora.setDcNome("Nova Gravadora");

        when(gravadoraRepository.save(gravadora)).thenReturn(gravadora);

        Gravadora atualizado = gravadoraService.salvar(gravadora);

        assertEquals("Nova Gravadora", atualizado.getDcNome());
    }

    @Test
    void deveExcluirGravadora() {
        doNothing().when(gravadoraRepository).deleteById(1L);

        gravadoraService.excluir(1L);

        verify(gravadoraRepository, times(1)).deleteById(1L);
    }
}
