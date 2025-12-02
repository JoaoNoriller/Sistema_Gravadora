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

import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.service.GravadoraService;

@RestController
@RequestMapping("/gravadora")
public class GravadoraController {

    @Autowired
    private GravadoraService gravadoraService;

    //CRIAR GRAVADORA
    @PostMapping
    public ResponseEntity<?> criarGravadora(@RequestBody Gravadora gravadora) {
        try {
            Gravadora nova = gravadoraService.salvar(gravadora);
            return ResponseEntity.status(HttpStatus.CREATED).body(nova);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //LISTAR TODAS
    @GetMapping
    public ResponseEntity<List<Gravadora>> listarTodas() {
        return ResponseEntity.ok(gravadoraService.listarTodos());
    }

    //BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Gravadora gravadora = gravadoraService.buscarPorId(id);
            return ResponseEntity.ok(gravadora);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //ATUALIZAR GRAVADORA
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Gravadora gravadora) {
        try {
            gravadora.setIdGravadora(id);
            Gravadora atualizada = gravadoraService.salvar(gravadora);
            return ResponseEntity.ok(atualizada);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //DELETAR GRAVADORA
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
