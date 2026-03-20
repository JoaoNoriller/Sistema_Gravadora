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

import com.gravadora.projeto.dto.GravadoraDTO;
import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.service.GravadoraService;

/**
 * Controller responsável por receber as requisições HTTP relacionadas às gravadoras
 * Ele não contém lógica de negócio, apenas recebe a requisição,
 * repassa ao service e devolve a resposta adequada ao cliente
 */
@RestController
@RequestMapping("/gravadora")
public class GravadoraController {

    @Autowired
    private GravadoraService gravadoraService;

    /**
     * POST /gravadora
     * Cria uma nova gravadora no sistema
     */
    @PostMapping
    public ResponseEntity<?> criarGravadora(@RequestBody GravadoraDTO gravadoraDTO) {
        try {
            Gravadora nova = gravadoraService.salvar(gravadoraDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nova);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * GET /gravadora
     * Retorna a lista completa de gravadoras cadastradas no sistema
     */
    @GetMapping
    public ResponseEntity<List<Gravadora>> listarTodas() {
        return ResponseEntity.ok(gravadoraService.listarTodos());
    }

    /**
     * GET /gravadora/{id}
     * Busca e retorna uma gravadora específica pelo seu ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Gravadora gravadora = gravadoraService.buscarPorId(id);
            return ResponseEntity.ok(gravadora);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    /**
     * PUT /gravadora/{id}
     * Atualiza os dados de uma gravadora existente identificada pelo ID na URL
     * O ID da gravadora não precisa ser informado no body, ele vem da própria URL
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody GravadoraDTO gravadoraDTO) {
        try {
            Gravadora atualizada = gravadoraService.atualizar(id, gravadoraDTO);
            return ResponseEntity.ok(atualizada);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /**
     * DELETE /gravadora/{id}
     * Remove uma gravadora do sistema pelo seu ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            gravadoraService.excluir(id);
            return ResponseEntity.ok("Gravadora removida com sucesso!");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}