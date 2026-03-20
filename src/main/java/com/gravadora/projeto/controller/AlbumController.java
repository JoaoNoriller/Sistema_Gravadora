package com.gravadora.projeto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gravadora.projeto.dto.AlbumDTO;
import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.service.AlbumService;

/**
 * Controller responsável por receber as requisições HTTP relacionadas aos álbuns.
 * Ele não contém lógica de negócio, apenas recebe a requisição,
 * repassa ao service e devolve a resposta adequada ao cliente.
 */
@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    /**
     * POST /album
     * Cria um novo álbum no sistema.
     */
    @PostMapping
    public ResponseEntity<?> criarAlbum(@RequestBody AlbumDTO albumDTO) {
        try {
            Album novoAlbum = albumService.salvarAlbum(albumDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAlbum);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * GET /album
     * Retorna a lista completa de álbuns cadastrados no sistema.
     */
    @GetMapping
    public ResponseEntity<List<Album>> listar() {
        return ResponseEntity.ok(albumService.listar());
    }

    /**
     * GET /album/{id}
     * Busca e retorna um álbum específico pelo seu ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Album album = albumService.buscarPorId(id);
            return ResponseEntity.ok(album);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * GET /album/artista/{idArtista}
     * Retorna todos os álbuns vinculados a um artista específico.
     * Útil para visualizar o catálogo de um artista e verificar o limite de álbuns por ano.
     */
    @GetMapping("/artista/{idArtista}")
    public ResponseEntity<?> listarPorArtista(@PathVariable Long idArtista) {
        try {
            List<Album> albuns = albumService.listarPorArtista(idArtista);
            return ResponseEntity.ok(albuns);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * PUT /album/{id}
     * Atualiza os dados de um álbum existente identificado pelo ID na URL.
     * O ID do álbum não precisa ser informado no body — ele vem da própria URL.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody AlbumDTO albumDTO) {
        try {
            Album atualizado = albumService.atualizarAlbum(id, albumDTO);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * DELETE /album/{id}
     * Remove um álbum do sistema pelo seu ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            albumService.deletar(id);
            return ResponseEntity.ok("Álbum removido com sucesso!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}