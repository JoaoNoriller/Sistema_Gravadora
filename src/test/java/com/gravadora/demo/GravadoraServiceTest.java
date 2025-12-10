package com.gravadora.demo;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @BeforeEach // Executado antes de cada teste para inicializar uma gravadora padrão
    void setup() {
        
        gravadora = new Gravadora();
        gravadora.setIdGravadora(1L);
        gravadora.setDcNome("Gravadora Teste");
        gravadora.setDcEndereco("Rua ABC");
    }

    @Test
    void deveCadastrarGravadoraComSucesso() {         // Simula o comportamento do save, retornando a mesma gravadora
        when(gravadoraRepository.save(gravadora)).thenReturn(gravadora);

        Gravadora salvo = gravadoraService.salvar(gravadora); // chama o service
        //validações
        assertNotNull(salvo); // verifica se não é nulo
        assertEquals("Gravadora Teste", salvo.getDcNome());  // Nome foi mantido
        verify(gravadoraRepository, times(1)).save(gravadora); // Confirma que save foi chamado 1 vez
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
    void deveAtualizarGravadora() {    //Atualiza nome da Gravadora
        gravadora.setDcNome("Nova Gravadora"); 
        when(gravadoraRepository.save(gravadora)).thenReturn(gravadora); // retorna a gravadora atualizada

        Gravadora atualizado = gravadoraService.salvar(gravadora);
        assertEquals("Nova Gravadora", atualizado.getDcNome()); // gravadora salva com o novo nome
    }

    @Test
    void deveExcluirGravadora() { // simula excluir 
        doNothing().when(gravadoraRepository).deleteById(1L);

        gravadoraService.excluir(1L); // chama o método
        verify(gravadoraRepository, times(1)).deleteById(1L); // Verifica se deleteById foi executado exatamente 1 vez
    }
}
