package com.gravadora.projeto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gravadora.projeto.dto.ArtistaDTO;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.repository.ArtistaRepository;

@Service
public class ArtistaService {

    private final ArtistaRepository artistaRepository;

    public ArtistaService(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    // SALVAR
    public Artista salvar(ArtistaDTO artistaDTO) {

        // Regra: Nome obrigatório
        if (artistaDTO.dcNome() == null || artistaDTO.dcNome().trim().isEmpty()) {
            throw new RuntimeException("O nome do artista é obrigatório.");
        }

        // Regra: Nome único
        List<Artista> artistasComMesmoNome = artistaRepository.findByDcNome(artistaDTO.dcNome());
        if (!artistasComMesmoNome.isEmpty()) {
            throw new RuntimeException("Já existe um artista cadastrado com esse nome.");
        }

        Artista artista = new Artista();
        artista.setDcNome(artistaDTO.dcNome());
        artista.setDcEndereco(artistaDTO.dcEndereco());
        artista.setDtNascimento(artistaDTO.dtNascimento());
        artista.setDcNacionalidade(artistaDTO.dcNacionalidade());
        artista.setDcGeneroMusical(artistaDTO.dcGeneroMusical());

        return artistaRepository.save(artista);
    }

    // ATUALIZAR
    public Artista atualizar(Long id, ArtistaDTO artistaDTO) {

        // Regra: Nome obrigatório
        if (artistaDTO.dcNome() == null || artistaDTO.dcNome().trim().isEmpty()) {
            throw new RuntimeException("O nome do artista é obrigatório.");
        }

        // Busca o artista existente
        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado."));

        // Regra: Nome único (ignora o próprio)
        List<Artista> artistasComMesmoNome = artistaRepository.findByDcNome(artistaDTO.dcNome());
        boolean nomeEmUso = artistasComMesmoNome.stream()
                .anyMatch(a -> !a.getIdArtista().equals(id));

        if (nomeEmUso) {
            throw new RuntimeException("Já existe um artista cadastrado com esse nome.");
        }

        // Atualiza todos os campos
        artista.setDcNome(artistaDTO.dcNome());
        artista.setDcEndereco(artistaDTO.dcEndereco());
        artista.setDtNascimento(artistaDTO.dtNascimento());
        artista.setDcNacionalidade(artistaDTO.dcNacionalidade());
        artista.setDcGeneroMusical(artistaDTO.dcGeneroMusical());

        return artistaRepository.save(artista);
    }

    // LISTAR TODOS
    public List<Artista> listarTodos() {
        return artistaRepository.findAll();
    }

    // BUSCAR POR ID
    public Artista buscarPorId(Long id) {
        return artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado."));
    }

    // EXCLUIR POR ID
    public void excluir(Long id) {
        artistaRepository.deleteById(id);
    }

    // EXCLUIR TODOS
    public void excluirTodos() {
        artistaRepository.deleteAll();
    }
}