package com.gravadora.projeto.service.serviceIntegracao;

import java.time.LocalDate;

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

        assertNotNull(salva.getIdGravadora()); // ou getCdGravadora se for outro nome
        assertEquals("Universal Music", salva.getDcNome());
    }
    
}