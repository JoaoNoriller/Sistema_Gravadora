package com.gravadora.projeto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gravadora.projeto.dto.AlbumDTO;
import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.repository.AlbumRepository;
import com.gravadora.projeto.repository.ArtistaRepository;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private ArtistaService artistaService;

    //Salvar um álbum com validações de regras de negócio
     
    public Album salvarAlbum(AlbumDTO albumDTO) {

        // ----- Validar ARTISTA -----
        Artista artista = artistaService.buscarPorId(albumDTO.idArtista());

        if (artista == null || artista.getIdArtista() == null) {
            throw new RuntimeException("O álbum deve estar vinculado a um artista válido.");
        }

        artista = artistaRepository.findById(artista.getIdArtista())
                .orElseThrow(() -> new RuntimeException("Artista não encontrado."));

        // ----- Regra 1: Album precisa ter mais de 5 músicas -----
        if (albumDTO.qtdMusica() <= 5) {
            throw new RuntimeException("O álbum deve ter mais de 5 músicas.");
        }

        // ----- Regra 2: Título deve ter ao menos 3 caracteres -----
        if (albumDTO.dcTitulo() == null || albumDTO.dcTitulo().trim().length() < 3) {
            throw new RuntimeException("O título do álbum deve ter no mínimo 3 caracteres.");
        }

        // ----- Regra 3: Título único por artista -----
        boolean tituloExiste = albumRepository
                .findByDcTituloAndArtista_IdArtista(albumDTO.dcTitulo(), artista.getIdArtista())
                .stream()
                .anyMatch(a -> !a.getIdAlbum().equals(albumDTO.idAlbum())); // evita conflito ao editar

        if (tituloExiste) {
            throw new RuntimeException("Este artista já possui um álbum com esse título.");
        }

        // ----- Regra 4: Máximo 10 álbuns por ano -----
        int totalAno = albumRepository.countByArtista_IdArtistaAndDtAnoLancamento(
                artista.getIdArtista(),
                albumDTO.dtAnoLancamento()
        );

        if (totalAno >= 10) {
            throw new RuntimeException("O artista já lançou o máximo de 10 álbuns neste ano.");
        }

        // ----- Regra 5: Duração total do álbum <= 2 horas (7200s) -----
        if (albumDTO.tmDuracao() != null) {

            int duracaoSegundos = albumDTO.tmDuracao()
                    .toLocalTime()
                    .toSecondOfDay();

            if (duracaoSegundos > 7200) {
                throw new RuntimeException("A duração total do álbum não pode ultrapassar 2 horas.");
            }
        }
        
        Album album = new Album();
        album.setArtista(artista);
        album.setDcTitulo(albumDTO.dcTitulo());
        album.setDtAnoLancamento(albumDTO.dtAnoLancamento());
        album.setQtdMusica(albumDTO.qtdMusica());
        album.setTmDuracao(albumDTO.tmDuracao());
    
        // ----- Salvar -----
        return albumRepository.save(album);
    }

    // LISTAR TODOS
    public List<Album> listar() {
        return albumRepository.findAll();
    }

    // BUSCAR POR ID
    public Album buscarPorId(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado."));
    }

    // DELETAR POR ID
    public void deletar(Long id) {
        if (!albumRepository.existsById(id)) {
            throw new RuntimeException("Álbum não encontrado para exclusão.");
        }
        albumRepository.deleteById(id);
    }
}
