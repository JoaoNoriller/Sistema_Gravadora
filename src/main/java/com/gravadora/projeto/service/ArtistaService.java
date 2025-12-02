package com.gravadora.projeto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.repository.ArtistaRepository;

@Service
public class ArtistaService {

    private final ArtistaRepository artistaRepository;

    public ArtistaService(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    //SALVAR (com validação)
    public Artista salvar(Artista artista) {

        //Regra: Nome do artista deve ser único
        List<Artista> artistasComMesmoNome = artistaRepository.findByNome(artista.getDcNome());

        if (!artistasComMesmoNome.isEmpty()) {
            throw new RuntimeException("Já existe um artista cadastrado com esse nome.");
        }

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
