package com.gravadora.projeto.service.serviceIntegracao;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.GravadoraRepository;
import com.gravadora.projeto.service.GravadoraService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class GravadoraServiceIntegrationTest {

    @Autowired
    private GravadoraService gravadoraService;

    @Autowired
    private GravadoraRepository gravadoraRepository;

    @BeforeEach
    void limparBanco() {
        gravadoraRepository.deleteAll();
    }

    @Test
    public void deveSalvarGravadora() {

        Gravadora gravadora = new Gravadora();
        gravadora.setDcNome("Records");
        gravadora.setDcEndereco("Rua das Flores, 100");
        gravadora.setDcTelefone("11999999999");
        gravadora.setDcPais("Brasil");
        gravadora.setDtDataFundacao(LocalDate.of(2000, 1, 1));
        gravadora.setDcCnpj("12345678000199");

        Gravadora salva = gravadoraService.salvar(gravadora);

        assertNotNull(salva.getIdGravadora());
        assertEquals("Universal Music", salva.getDcNome());
    }

    @Test
    public void deveListarTodas() {

        Gravadora gravadora1 = new Gravadora();
        gravadora1.setDcNome("Sony");
        gravadora1.setDcPais("EUA");
        gravadora1.setDtDataFundacao(LocalDate.of(2000, 1, 1));
        gravadora1.setDcTelefone("11999999999");
        gravadora1.setDcEndereco("Rua das Flores, 100");
        gravadora1.setDcCnpj("12345678000100");

        Gravadora gravadora2 = new Gravadora();
        gravadora2.setDcNome("Warner");
        gravadora2.setDcPais("Brasil");
        gravadora2.setDtDataFundacao(LocalDate.of(1990, 1, 1));
        gravadora2.setDcTelefone("11999999988");
        gravadora2.setDcEndereco("Rua floresta, 80");
        gravadora2.setDcCnpj("12345678000109");

        gravadoraService.salvar(gravadora1);
        gravadoraService.salvar(gravadora2);

        List<Gravadora> lista = gravadoraService.listarTodos();

        assertEquals(2, lista.size());
    }

    @Test
    public void deveBuscarPorId() {

        Gravadora gravadora = new Gravadora();
        gravadora.setDcNome("Warner");
        gravadora.setDcPais("Brasil");
        gravadora.setDtDataFundacao(LocalDate.of(1990, 1, 1));
        gravadora.setDcTelefone("11999999988");
        gravadora.setDcEndereco("Rua floresta, 80");
        gravadora.setDcCnpj("12345678000109");

        Gravadora salvo = gravadoraService.salvar(gravadora);

        Gravadora encontrado = gravadoraService.buscarPorId(salvo.getIdGravadora());

        assertNotNull(encontrado);
        assertEquals("Warner", encontrado.getDcNome());
    }

    @Test
    public void deveExcluirGravadora() {

        Gravadora gravadora = new Gravadora();
        gravadora.setDcNome("Sony");
        gravadora.setDcPais("EUA");
        gravadora.setDtDataFundacao(LocalDate.of(2000, 1, 1));
        gravadora.setDcTelefone("11999999999");
        gravadora.setDcEndereco("Rua das Flores, 100");
        gravadora.setDcCnpj("12345678000100");

        Gravadora salvo = gravadoraService.salvar(gravadora);

        gravadoraService.excluir(salvo.getIdGravadora());

        assertEquals(0, gravadoraService.listarTodos().size());
    }

    @Test
    public void deveExcluirTodos() {

        Gravadora gravadora1 = new Gravadora();
        gravadora1.setDcNome("Sony");
        gravadora1.setDcPais("EUA");
        gravadora1.setDtDataFundacao(LocalDate.of(2000, 1, 1));
        gravadora1.setDcTelefone("11999999999");
        gravadora1.setDcEndereco("Rua das Flores, 100");
        gravadora1.setDcCnpj("12345678000100");

        Gravadora gravadora2 = new Gravadora();
        gravadora2.setDcNome("Warner");
        gravadora2.setDcPais("Brasil");
        gravadora2.setDtDataFundacao(LocalDate.of(1990, 1, 1));
        gravadora2.setDcTelefone("11999999988");
        gravadora2.setDcEndereco("Rua floresta, 80");
        gravadora2.setDcCnpj("12345678000109");

        gravadoraService.salvar(gravadora1);
        gravadoraService.salvar(gravadora2);

        gravadoraService.excluirTodos();

        assertEquals(0, gravadoraService.listarTodos().size());
    }

}