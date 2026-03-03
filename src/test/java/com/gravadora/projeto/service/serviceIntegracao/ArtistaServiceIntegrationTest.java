package com.gravadora.projeto.service.serviceIntegracao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.repository.ArtistaRepository;
import com.gravadora.projeto.service.ArtistaService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class ArtistaServiceIntegrationTest {

    @Autowired
    private ArtistaService artistaService;

    @Autowired
    private ArtistaRepository artistaRepository;

    @BeforeEach
    void limparBanco() {
        artistaRepository.deleteAll(); // Tinha dados em meu banco, com isso ele limpa ele para realizar o teste
    }

    @Test
    public void deveSalvarArtista() {

        Artista artista = new Artista();
        artista.setDcNome("Coldplay");

        Artista salvo = artistaService.salvar(artista);

        assertNotNull(salvo.getDcNome());
        assertEquals("Coldplay", salvo.getDcNome());
    }

    @Test
    public void naoDevePermitirNomeDuplicado() {

        Artista artista1 = new Artista();
        artista1.setDcNome("Coldplay");
        artistaService.salvar(artista1);

        Artista artista2 = new Artista();
        artista2.setDcNome("Coldplay");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> artistaService.salvar(artista2));

        assertEquals("Já existe um artista cadastrado com esse nome.", ex.getMessage());
    }

    @Test
    public void deveListarTodos() {

        Artista a1 = new Artista();
        a1.setDcNome("Coldplay");

        Artista a2 = new Artista();
        a2.setDcNome("Adele");

        artistaService.salvar(a1);
        artistaService.salvar(a2);

        List<Artista> lista = artistaService.listarTodos();

        assertEquals(2, lista.size());
    }

    @Test
    public void deveBuscarPorId() {

        Artista artista = new Artista();
        artista.setDcNome("Coldplay");

        Artista salvo = artistaService.salvar(artista);

        Artista encontrado = artistaService.buscarPorId(salvo.getIdArtista());

        assertNotNull(encontrado);
        assertEquals("Coldplay", encontrado.getDcNome());
    }

    @Test
    public void deveExcluirArtista() {

        Artista artista = new Artista();
        artista.setDcNome("Coldplay");

        Artista salvo = artistaService.salvar(artista);

        artistaService.excluir(salvo.getIdArtista());

        assertEquals(0, artistaService.listarTodos().size());
    }

    @Test
    public void deveExcluirTodos() {

        Artista a1 = new Artista();
        a1.setDcNome("Coldplay");

        Artista a2 = new Artista();
        a2.setDcNome("Adele");

        artistaService.salvar(a1);
        artistaService.salvar(a2);

        artistaService.excluirTodos();

        assertEquals(0, artistaService.listarTodos().size());
    }
}
