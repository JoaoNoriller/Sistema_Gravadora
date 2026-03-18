package com.gravadora.projeto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gravadora.projeto.dto.GravadoraDTO;
import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.GravadoraRepository;

@Service
public class GravadoraService {

    private final GravadoraRepository gravadoraRepository;

    public GravadoraService(GravadoraRepository gravadoraRepository) {
        this.gravadoraRepository = gravadoraRepository;
    }

    // SALVAR
    public Gravadora salvar(GravadoraDTO gravadoraDTO) {

        // Regra 1: Nome único
        boolean nomeExiste = gravadoraRepository.findByDcNome(gravadoraDTO.dcNome())
                .stream()
                .anyMatch(g -> true);

        if (nomeExiste) {
            throw new RuntimeException("Já existe uma gravadora cadastrada com esse nome.");
        }

        // Regra 2: CNPJ único
        boolean cnpjExiste = gravadoraRepository.findByDcCnpj(gravadoraDTO.dcCnpj())
                .stream()
                .anyMatch(g -> true);

        if (cnpjExiste) {
            throw new RuntimeException("Já existe uma gravadora cadastrada com esse CNPJ.");
        }

        Gravadora gravadora = new Gravadora();
        gravadora.setDcNome(gravadoraDTO.dcNome());
        gravadora.setDcEndereco(gravadoraDTO.dcEndereco());
        gravadora.setDcTelefone(gravadoraDTO.dcTelefone());
        gravadora.setDcPais(gravadoraDTO.dcPais());
        gravadora.setDtDataFundacao(gravadoraDTO.dtDataFundacao());
        gravadora.setDcCnpj(gravadoraDTO.dcCnpj());

        return gravadoraRepository.save(gravadora);
    }

    // ATUALIZAR
    public Gravadora atualizar(Long id, GravadoraDTO gravadoraDTO) {

        // Busca a gravadora existente
        Gravadora gravadora = gravadoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gravadora não encontrada."));

        // Regra 1: Nome único (ignora a própria gravadora)
        boolean nomeEmUso = gravadoraRepository.findByDcNome(gravadoraDTO.dcNome())
                .stream()
                .anyMatch(g -> !g.getIdGravadora().equals(id));

        if (nomeEmUso) {
            throw new RuntimeException("Já existe uma gravadora cadastrada com esse nome.");
        }

        // Regra 2: CNPJ único (ignora a própria gravadora)
        boolean cnpjEmUso = gravadoraRepository.findByDcCnpj(gravadoraDTO.dcCnpj())
                .stream()
                .anyMatch(g -> !g.getIdGravadora().equals(id));

        if (cnpjEmUso) {
            throw new RuntimeException("Já existe uma gravadora cadastrada com esse CNPJ.");
        }

        // Atualiza todos os campos
        gravadora.setDcNome(gravadoraDTO.dcNome());
        gravadora.setDcEndereco(gravadoraDTO.dcEndereco());
        gravadora.setDcTelefone(gravadoraDTO.dcTelefone());
        gravadora.setDcPais(gravadoraDTO.dcPais());
        gravadora.setDtDataFundacao(gravadoraDTO.dtDataFundacao());
        gravadora.setDcCnpj(gravadoraDTO.dcCnpj());

        return gravadoraRepository.save(gravadora);
    }

    // LISTAR
    public List<Gravadora> listarTodos() {
        return gravadoraRepository.findAll();
    }

    // BUSCAR POR ID
    public Gravadora buscarPorId(Long id) {
        return gravadoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gravadora não encontrada."));
    }

    // EXCLUIR POR ID
    public void excluir(Long id) {
        if (!gravadoraRepository.existsById(id)) {
            throw new RuntimeException("Gravadora não encontrada para exclusão.");
        }
        gravadoraRepository.deleteById(id);
    }

    // EXCLUIR TODOS
    public void excluirTodos() {
        gravadoraRepository.deleteAll();
    }
}