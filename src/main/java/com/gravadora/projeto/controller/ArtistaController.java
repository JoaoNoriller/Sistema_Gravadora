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

import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.service.ArtistaService;

@RestController
@RequestMapping("/artista")
public class ArtistaController {

    @Autowired
    private ArtistaService artistaService;

    //CRIAR ARTISTA
    @PostMapping
    public ResponseEntity<?> criarArtista(@RequestBody Artista artista) {
        try {
            Artista novoArtista = artistaService.salvar(artista);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoArtista);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Artista>> listarTodos() {
        return ResponseEntity.ok(artistaService.listarTodos());
    }

    //BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Artista artista = artistaService.buscarPorId(id);
            return ResponseEntity.ok(artista);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Artista artista) {
        try {
            artista.setIdArtista(id);
            Artista atualizado = artistaService.salvar(artista);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //DELETAR
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
