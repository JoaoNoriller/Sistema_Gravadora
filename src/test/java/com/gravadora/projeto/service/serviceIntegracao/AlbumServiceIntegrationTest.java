package com.gravadora.projeto.service.serviceIntegracao;

import java.sql.Time;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gravadora.projeto.dto.AlbumDTO;
import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.ArtistaRepository;
import com.gravadora.projeto.repository.GravadoraRepository;
import com.gravadora.projeto.service.AlbumService;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class AlbumServiceIntegrationTest {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private GravadoraRepository gravadoraRepository;

    private AlbumDTO albumDTOValido;
    private Artista artista;
    private Gravadora gravadora;

    @BeforeEach
    void setup() {
        artista = new Artista();
        artista.setDcNome("Coldplay");
        artista = artistaRepository.save(artista);

        gravadora = new Gravadora();
        gravadora.setDcNome("Records");
        gravadora.setDcEndereco("Rua das Flores, 100");
        gravadora.setDcTelefone("11999999999");
        gravadora.setDcPais("Brasil");
        gravadora.setDtDataFundacao(LocalDate.of(2000, 1, 1));
        gravadora.setDcCnpj("12345678000199");
        gravadora = gravadoraRepository.save(gravadora);

        // DTO base válido — passa em todas as regras
        albumDTOValido = new AlbumDTO(
                null,
                "Parachutes",
                LocalDate.of(2000, 7, 10),
                null,                        // status é gerado automaticamente pelo service
                10,
                Time.valueOf("01:30:00"),
                artista.getIdArtista(),
                gravadora.getIdGravadora()
        );
    }

    // ---------------------------------------------------------------
    // SALVAR COM SUCESSO
    // ---------------------------------------------------------------

    @Test
    public void deveSalvarAlbumComSucesso() {
        Album salvo = albumService.salvarAlbum(albumDTOValido);

        assertNotNull(salvo.getIdAlbum());
        assertEquals("Parachutes", salvo.getDcTitulo());
        assertEquals("COMPLETO", salvo.getDcStatus()); // 10 músicas = COMPLETO
    }

    // ---------------------------------------------------------------
    // REGRA 1 — quantidade de músicas deve ser > 5
    // ---------------------------------------------------------------

    @Test
    public void naoDeveSalvarAlbumComMenosDe5Musicas() {
        AlbumDTO dtoInvalido = new AlbumDTO(
                null,
                "Parachutes",
                LocalDate.of(2000, 7, 10),
                null,
                5,                           // ← inválido (deve ser > 5)
                Time.valueOf("01:30:00"),
                artista.getIdArtista(),
                gravadora.getIdGravadora()
        );

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> albumService.salvarAlbum(dtoInvalido));

        assertEquals("O álbum deve ter mais de 5 músicas.", ex.getMessage());
    }

    // ---------------------------------------------------------------
    // REGRA 2 — título deve ter ao menos 3 caracteres
    // ---------------------------------------------------------------

    @Test
    public void naoDeveSalvarTituloInvalido() {
        AlbumDTO dtoInvalido = new AlbumDTO(
                null,
                "AB",                        // ← inválido (< 3 chars)
                LocalDate.of(2000, 7, 10),
                null,
                10,
                Time.valueOf("01:30:00"),
                artista.getIdArtista(),
                gravadora.getIdGravadora()
        );

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> albumService.salvarAlbum(dtoInvalido));

        assertEquals("O título do álbum deve ter no mínimo 3 caracteres.", ex.getMessage());
    }

    // ---------------------------------------------------------------
    // REGRA 5 — duração máxima de 2 horas
    // ---------------------------------------------------------------

    @Test
    public void naoDeveSalvarComDuracaoMaiorQue2Horas() {
        AlbumDTO dtoInvalido = new AlbumDTO(
                null,
                "Parachutes",
                LocalDate.of(2000, 7, 10),
                null,
                10,
                Time.valueOf("02:01:00"),    // ← inválido (> 7200s)
                artista.getIdArtista(),
                gravadora.getIdGravadora()
        );

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> albumService.salvarAlbum(dtoInvalido));

        assertEquals("A duração total do álbum não pode ultrapassar 2 horas.", ex.getMessage());
    }

    // ---------------------------------------------------------------
    // REGRA 3 — título único por artista
    // ---------------------------------------------------------------

    @Test
    public void naoDeveSalvarAlbumComTituloDuplicadoParaMesmoArtista() {
        albumService.salvarAlbum(albumDTOValido);

        AlbumDTO dtoDuplicado = new AlbumDTO(
                null,
                "Parachutes",               // ← mesmo título
                LocalDate.of(2001, 1, 1),
                null,
                10,
                Time.valueOf("01:00:00"),
                artista.getIdArtista(),     // ← mesmo artista
                gravadora.getIdGravadora()
        );

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> albumService.salvarAlbum(dtoDuplicado));

        assertEquals("Este artista já possui um álbum com esse título.", ex.getMessage());
    }

    // ---------------------------------------------------------------
    // REGRA 4 — máximo 10 álbuns por ano por artista
    // ---------------------------------------------------------------

    @Test
    public void naoDeveSalvarMaisDe10AlbunsPorAnoParaMesmoArtista() {
        LocalDate ano = LocalDate.of(2000, 1, 1);

        for (int i = 1; i <= 10; i++) {
            AlbumDTO dto = new AlbumDTO(
                    null,
                    "Album " + i,
                    ano,
                    null,
                    10,
                    Time.valueOf("01:00:00"),
                    artista.getIdArtista(),
                    gravadora.getIdGravadora()
            );
            albumService.salvarAlbum(dto);
        }

        AlbumDTO dto11 = new AlbumDTO(
                null,
                "Album 11",
                ano,
                null,
                10,
                Time.valueOf("01:00:00"),
                artista.getIdArtista(),
                gravadora.getIdGravadora()
        );

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> albumService.salvarAlbum(dto11));

        assertEquals("O artista já lançou o máximo de 10 álbuns neste ano.", ex.getMessage());
    }

    // ---------------------------------------------------------------
    // STATUS — álbum com menos de 10 músicas deve ser INCOMPLETO
    // ---------------------------------------------------------------

    @Test
    public void deveDefinirStatusIncompletoParaAlbumComMenosDe10Musicas() {
        AlbumDTO dtoIncompleto = new AlbumDTO(
                null,
                "Ghost Stories",
                LocalDate.of(2014, 5, 19),
                null,
                7,                           // ← entre 6 e 9 = INCOMPLETO
                Time.valueOf("00:45:00"),
                artista.getIdArtista(),
                gravadora.getIdGravadora()
        );

        Album salvo = albumService.salvarAlbum(dtoIncompleto);

        assertEquals("INCOMPLETO", salvo.getDcStatus());
    }
}