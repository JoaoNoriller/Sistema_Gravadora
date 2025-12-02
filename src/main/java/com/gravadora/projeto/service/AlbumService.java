package com.gravadora.projeto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

   
    //SALVAR ÁLBUM COM VALIDAÇÕES
    public Album salvarAlbum(Album album) {

        Artista artista = album.getArtista();

        if (artista == null || artista.getIdArtista() == null) {
            throw new RuntimeException("O álbum deve estar vinculado a um artista válido.");
        }

        artista = artistaRepository.findById(artista.getIdArtista())
                .orElseThrow(() -> new RuntimeException("Artista não encontrado."));

        //Regra 1: Album só pode ser criado se tiver MAIS de 5 músicas
        if (album.getQtdMusica() <= 5) {
            throw new RuntimeException("O álbum deve ter mais de 5 músicas.");
        }

        //Regra 2: Título deve ter ao menos 3 caracteres
        if (album.getDcTitulo() == null || album.getDcTitulo().length() < 3) {
            throw new RuntimeException("O título do álbum deve ter no mínimo 3 caracteres.");
        }

        //Regra 3: Título do álbum deve ser único por artista
        List<Album> albunsDoArtista = albumRepository.findByArtista(artista);

        boolean tituloRepetido = albunsDoArtista.stream()
                .anyMatch(a -> a.getDcTitulo().equalsIgnoreCase(album.getDcTitulo()));

        if (tituloRepetido) {
            throw new RuntimeException("Este artista já possui um álbum com esse título.");
        }

        //Regra 4: Máximo 10 álbuns por ano
        int ano = album.getDtAnoLancamento().getYear();

        long totalAno = albunsDoArtista.stream()
                .filter(a -> a.getDtAnoLancamento().getYear() == ano)
                .count();

        if (totalAno >= 10) {
            throw new RuntimeException("O artista já lançou o máximo de 10 álbuns neste ano.");
        }

        //Regra 5: Duração total do álbum não pode passar de 2 horas
        if (album.getDuracao() != null) {
            int duracaoSegundos = (int) album.getDuracao().toLocalTime().toSecondOfDay();
            if (duracaoSegundos > 7200) {
                throw new RuntimeException("A duração total do álbum não pode ultrapassar 2 horas.");
            }
        }

        // Se tudo passou, salva
        return albumRepository.save(album);
    }
   
    // LISTAR
    public List<Album> listar() {
        return albumRepository.findAll();
    }
   
    // BUSCAR POR ID
    public Album buscarPorId(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado."));
    }

    // DELETAR
    public void deletar(Long id) {
        if (!albumRepository.existsById(id)) {
            throw new RuntimeException("Álbum não encontrado para exclusão.");
        }
        albumRepository.deleteById(id);
    }
}
