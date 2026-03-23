package com.gravadora.projeto.service.serviceUnitario;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.gravadora.projeto.dto.GravadoraDTO;
import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.GravadoraRepository;
import com.gravadora.projeto.service.GravadoraService;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito para os testes
class GravadoraServiceTest {

    @Mock // Repositório falso que não acessa o banco de dados
    private GravadoraRepository gravadoraRepository;

    @InjectMocks // Cria uma instância real do Service, mas usando o Repository Mockado
    private GravadoraService gravadoraService;

    private Gravadora gravadora;
    private GravadoraDTO gravadoraDTO;

    @BeforeEach // Executado antes de cada teste para inicializar uma gravadora padrão
    void setup() {

        gravadora = new Gravadora();
        gravadora.setIdGravadora(1L);
        gravadora.setDcNome("Gravadora Teste");
        gravadora.setDcEndereco("Rua ABC");

        gravadoraDTO = new GravadoraDTO("Gravadora Teste", "Rua ABC", "47999999999", "Brasil",
                LocalDate.of(2000, 1, 1), "12345678000199");

    }

    @Test
    void deveCadastrarGravadoraComSucesso() {

        // Simula o comportamento do save do repository
        when(gravadoraRepository.save(any(Gravadora.class)))
                .thenReturn(gravadora);

        // Executa o método do service
        Gravadora salvo = gravadoraService.salvar(gravadoraDTO);

        // Validações
        assertNotNull(salvo);
        assertEquals("Gravadora Teste", salvo.getDcNome());

        // Verifica se o save foi chamado 1 vez
        verify(gravadoraRepository, times(1)).save(any(Gravadora.class));
    }

    @Test
    void deveBuscarGravadoraPorId() { // Simula o findById retornando uma Optional com a gravadora
        when(gravadoraRepository.findById(1L)).thenReturn(Optional.of(gravadora));

        Gravadora resultado = gravadoraService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Gravadora Teste", resultado.getDcNome());
    }

    @Test
    void deveListarTodasAsGravadoras() { // Simula findAll retornando uma lista com 1 elemento
        when(gravadoraRepository.findAll()).thenReturn(Arrays.asList(gravadora));

        var lista = gravadoraService.listarTodos();

        assertEquals(1, lista.size()); // Deve retornar uma lista com 1 item
        verify(gravadoraRepository, times(1)).findAll(); // Confirma execução
    }

    @Test
    void deveAtualizarGravadora() {

        // Atualiza nome da Gravadora
        GravadoraDTO dtoAtualizado = new GravadoraDTO("Nova Gravadora", "Rua ABC", "47999999999", "Brasil",
                LocalDate.of(2000, 1, 1), "12345678000100");

        Gravadora gravadoraAtualizada = new Gravadora();
        gravadoraAtualizada.setIdGravadora(1L);
        gravadoraAtualizada.setDcNome("Nova Gravadora");

        when(gravadoraRepository.save(any(Gravadora.class)))
                .thenReturn(gravadoraAtualizada);

        Gravadora atualizado = gravadoraService.salvar(dtoAtualizado);

        assertEquals("Nova Gravadora", atualizado.getDcNome());
    }

    @Test
    void deveExcluirGravadora() {

        // Simula que a gravadora existe no banco
        when(gravadoraRepository.existsById(1L)).thenReturn(true);

        // Simula o deleteById sem fazer nada
        doNothing().when(gravadoraRepository).deleteById(1L);

        gravadoraService.excluir(1L);

        // Verifica se deleteById foi executado exatamente 1 vez
        verify(gravadoraRepository, times(1)).deleteById(1L);
    }
}
