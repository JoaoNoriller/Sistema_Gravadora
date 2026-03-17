package com.gravadora.projeto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gravadora.projeto.dto.AlbumDTO;
import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.AlbumRepository;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistaService artistaService;   // ← só service, sem repository

    @Autowired
    private GravadoraService gravadoraService; // ← só service, sem repository

    public Album salvarAlbum(AlbumDTO albumDTO) {

        // ----- Validar ARTISTA via service -----
        Artista artista = artistaService.buscarPorId(albumDTO.idArtista());

        // ----- Validar GRAVADORA via service -----
        Gravadora gravadora = gravadoraService.buscarPorId(albumDTO.idGravadora());

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
                .anyMatch(a -> !a.getIdAlbum().equals(albumDTO.idAlbum()));

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

        // ----- Status automático baseado na quantidade de músicas -----
        String status = albumDTO.qtdMusica() >= 10 ? "COMPLETO" : "INCOMPLETO";

        // ----- Montar entidade -----
        Album album = new Album();
        album.setArtista(artista);
        album.setGravadora(gravadora);
        album.setDcTitulo(albumDTO.dcTitulo());
        album.setDtAnoLancamento(albumDTO.dtAnoLancamento());
        album.setQtdMusica(albumDTO.qtdMusica());
        album.setTmDuracao(albumDTO.tmDuracao());
        album.setDcStatus(status);

        return albumRepository.save(album);
    }

    public List<Album> listar() {
        return albumRepository.findAll();
    }

    public Album buscarPorId(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado."));
    }

    public void deletar(Long id) {
        if (!albumRepository.existsById(id)) {
            throw new RuntimeException("Álbum não encontrado para exclusão.");
        }
        albumRepository.deleteById(id);
    }
}