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

@RestController
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    //CRIAR ÁLBUM
    @PostMapping
    public ResponseEntity<?> criarAlbum(@RequestBody AlbumDTO albumDTO) {
        try {
            Album novoAlbum = albumService.salvarAlbum(albumDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoAlbum);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //LISTAR TODOS
    @GetMapping
    public ResponseEntity<List<Album>> listar() {
        return ResponseEntity.ok(albumService.listar());
    }


    //BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Album album = albumService.buscarPorId(id);
            return ResponseEntity.ok(album);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
 
    //ATUALIZAR ÁLBUM
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody AlbumDTO albumDTO) {
        try {
            
             // cria novo DTO com id para update // permite que o service trate as validações
            AlbumDTO dtoAtualizado = new AlbumDTO(
                id,
                albumDTO.dcTitulo(),
                albumDTO.dtAnoLancamento(),
                albumDTO.qtdMusica(),
                albumDTO.tmDuracao(),
                albumDTO.idArtista(),
                albumDTO.idGravadora()
            );
            Album atualizado = albumService.salvarAlbum(albumDTO);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //DELETAR ÁLBUM
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
