package com.gravadora.projeto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.GravadoraRepository;

@Service
public class GravadoraService {

    private final GravadoraRepository gravadoraRepository;

    public GravadoraService(GravadoraRepository gravadoraRepository) {
        this.gravadoraRepository = gravadoraRepository;
    }

    //SALVAR
    public Gravadora salvar(Gravadora gravadoraDTO) {
        return gravadoraRepository.save(gravadoraDTO);
    }

    //LISTAR
    public List<Gravadora> listarTodos() {
        return gravadoraRepository.findAll();
    }

    //BUSCAR POR ID
    public Gravadora buscarPorId(Long id) {
        return gravadoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gravadora não encontrada."));
    }

    //EXCLUIR POR ID
    public void excluir(Long id) {
        gravadoraRepository.deleteById(id);
    }

    //EXCLUIR TODOS
    public void excluirTodos() {
        gravadoraRepository.deleteAll();
    }
}
