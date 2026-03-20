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

import com.gravadora.projeto.dto.ArtistaDTO;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.service.ArtistaService;

/**
 * Controller responsável por receber as requisições HTTP relacionadas aos artistas
 * Ele não contém lógica de negócio, apenas recebe a requisição,
 * repassa ao service e devolve a resposta adequada ao cliente
 */
@RestController
@RequestMapping("/artista")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    /**
     * POST /artista
     * Cria um novo artista no sistema
     */
    @PostMapping
    public ResponseEntity<?> criarArtista(@RequestBody ArtistaDTO artistaDTO) {
        try {
            Artista novoArtista = artistaService.salvar(artistaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoArtista);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * GET /artista
     * Retorna a lista completa de artistas cadastrados no sistema
     */
    @GetMapping
    public ResponseEntity<List<Artista>> listarTodos() {
        return ResponseEntity.ok(artistaService.listarTodos());
    }

    /**
     * GET /artista/{id}
     * Busca e retorna um artista específico pelo seu ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Artista artista = artistaService.buscarPorId(id);
            return ResponseEntity.ok(artista);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * PUT /artista/{id}
     * Atualiza os dados de um artista existente identificado pelo ID na URL
     * O ID do artista não precisa ser informado no body, ele vem da própria URL
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody ArtistaDTO artistaDTO) {
        try {
            Artista atualizado = artistaService.atualizar(id, artistaDTO);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * DELETE /artista/{id}
     * Remove um artista do sistema pelo seu ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            artistaService.excluir(id);
            return ResponseEntity.ok("Artista removido com sucesso!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}