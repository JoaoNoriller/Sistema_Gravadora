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

        // Regra: Nome do artista deve ser único
        List<Artista> artistasComMesmoNome = artistaRepository.findByDcNome(artistaDTO.dcNome());

        if (!artistasComMesmoNome.isEmpty()) {
            throw new RuntimeException("Já existe um artista cadastrado com esse nome.");
        }

        // Converter DTO -> Entidade com todos os campos
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

        // Busca o artista existente — lança exceção se não encontrar
        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado."));

        // Verifica se outro artista já usa esse nome (ignora o próprio)
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

    //LISTAR TODOS
    public List<Artista> listarTodos() {
        return artistaRepository.findAll();
    }

    //BUSCAR POR ID
    public Artista buscarPorId(Long id) {
        return artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado."));
    }

    //EXCLUIR POR ID
    public void excluir(Long id) {
        artistaRepository.deleteById(id);
    }

    //EXCLUIR TODOS
    public void excluirTodos() {
        artistaRepository.deleteAll();
    }

}
