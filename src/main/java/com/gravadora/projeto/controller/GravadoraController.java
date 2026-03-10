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

@RestController
@RequestMapping("/gravadora")
public class GravadoraController {

    @Autowired
    private GravadoraService gravadoraService;

    //CRIAR GRAVADORA
    @PostMapping
    public ResponseEntity<?> criarGravadora(@RequestBody GravadoraDTO gravadoraDTO) {
        try {
            Gravadora nova = gravadoraService.salvar(gravadoraDTO);
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
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody GravadoraDTO gravadoraDTO) {
        try {
            Gravadora gravadoraExistente = gravadoraService.buscarPorId(id);
            gravadoraExistente.setDcNome(gravadoraDTO.dcNome());
            gravadoraExistente.setDcEndereco(gravadoraDTO.dcEndereco());
            gravadoraExistente.setDcTelefone(gravadoraDTO.dcTelefone());
            gravadoraExistente.setDcPais(gravadoraDTO.dcPais());
            gravadoraExistente.setDtDataFundacao(gravadoraDTO.dtDataFundacao());
            gravadoraExistente.setDcCnpj(gravadoraDTO.dcCnpj());
            Gravadora atualizada = gravadoraService.salvar(gravadoraDTO);
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
