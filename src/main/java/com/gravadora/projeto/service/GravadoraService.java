package com.gravadora.projeto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gravadora.projeto.dto.GravadoraDTO;
import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.GravadoraRepository;

/**
 * Camada de serviço responsável por toda a lógica de negócio relacionada às gravadoras
 * Aqui ficam as validações e regras antes de qualquer acesso ao banco de dados
 */
@Service
public class GravadoraService {

    private final GravadoraRepository gravadoraRepository;

    public GravadoraService(GravadoraRepository gravadoraRepository) {
        this.gravadoraRepository = gravadoraRepository;
    }

    // ===================================================================
    // CRIAR GRAVADORA — chamado pelo POST /gravadora
    // ===================================================================

    /**
     * Cria uma nova gravadora após passar por todas as validações de negócio
     * O ID é gerado automaticamente pelo banco, não precisa ser informado
     */
    public Gravadora salvar(GravadoraDTO gravadoraDTO) {

        // RN-01: O nome da gravadora é obrigatório
        // Não permitimos cadastrar uma gravadora sem nome ou com nome em branco
        if (gravadoraDTO.dcNome() == null || gravadoraDTO.dcNome().trim().isEmpty()) {
            throw new RuntimeException("O nome da gravadora é obrigatório.");
        }

        // RN-02: O CNPJ é obrigatório
        // Não permitimos cadastrar uma gravadora sem CNPJ
        if (gravadoraDTO.dcCnpj() == null || gravadoraDTO.dcCnpj().trim().isEmpty()) {
            throw new RuntimeException("O CNPJ da gravadora é obrigatório.");
        }

        // RN-03: O CNPJ deve ter exatamente 14 dígitos numéricos (sem formatação)
        if (gravadoraDTO.dcCnpj().trim().length() != 14) {
            throw new RuntimeException("O CNPJ deve conter 14 dígitos.");
        }

        // RN-04: O nome da gravadora deve ser único no sistema
        // Buscamos se já existe alguma gravadora com esse nome antes de salvar
        boolean nomeExiste = gravadoraRepository.findByDcNome(gravadoraDTO.dcNome())
                .stream()
                .anyMatch(g -> true);

        if (nomeExiste) {
            throw new RuntimeException("Já existe uma gravadora cadastrada com esse nome.");
        }

        // RN-05: O CNPJ da gravadora deve ser único no sistema
        // Cada empresa possui um CNPJ único, não permitimos duplicar
        boolean cnpjExiste = gravadoraRepository.findByDcCnpj(gravadoraDTO.dcCnpj())
                .stream()
                .anyMatch(g -> true);

        if (cnpjExiste) {
            throw new RuntimeException("Já existe uma gravadora cadastrada com esse CNPJ.");
        }

        // Todas as validações passaram, montamos e salvamos a gravadora
        Gravadora gravadora = new Gravadora();
        gravadora.setDcNome(gravadoraDTO.dcNome());
        gravadora.setDcEndereco(gravadoraDTO.dcEndereco());
        gravadora.setDcTelefone(gravadoraDTO.dcTelefone());
        gravadora.setDcPais(gravadoraDTO.dcPais());
        gravadora.setDtDataFundacao(gravadoraDTO.dtDataFundacao());
        gravadora.setDcCnpj(gravadoraDTO.dcCnpj());

        return gravadoraRepository.save(gravadora);
    }

    // ===================================================================
    // ATUALIZAR GRAVADORA — chamado pelo PUT /gravadora/{id}
    // ===================================================================

    /**
     * Atualiza os dados de uma gravadora existente identificada pelo ID informado na URL
     * Aplica as mesmas regras do cadastro, mas ignora a própria gravadora
     * nas verificações de nome e CNPJ duplicados, permitindo editar outros campos
     * sem precisar mudar o nome ou o CNPJ
     */
    public Gravadora atualizar(Long id, GravadoraDTO gravadoraDTO) {

        // RN-01: Nome obrigatório também no update
        if (gravadoraDTO.dcNome() == null || gravadoraDTO.dcNome().trim().isEmpty()) {
            throw new RuntimeException("O nome da gravadora é obrigatório.");
        }

        // RN-02: CNPJ obrigatório também no update
        if (gravadoraDTO.dcCnpj() == null || gravadoraDTO.dcCnpj().trim().isEmpty()) {
            throw new RuntimeException("O CNPJ da gravadora é obrigatório.");
        }

        // RN-03: CNPJ deve ter 14 dígitos também no update
        if (gravadoraDTO.dcCnpj().trim().length() != 14) {
            throw new RuntimeException("O CNPJ deve conter 14 dígitos.");
        }

        // Buscamos a gravadora que será atualizada
        // Se não existir no banco, lançamos uma exceção informando que não foi encontrada
        Gravadora gravadora = gravadoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gravadora não encontrada."));

        // RN-04: Nome único, mas ignora a própria gravadora sendo editada
        // Isso permite editar outros campos sem precisar trocar o nome,
        // mas bloqueia se outra gravadora diferente já usar esse nome
        boolean nomeEmUso = gravadoraRepository.findByDcNome(gravadoraDTO.dcNome())
                .stream()
                .anyMatch(g -> !g.getIdGravadora().equals(id));

        if (nomeEmUso) {
            throw new RuntimeException("Já existe uma gravadora cadastrada com esse nome.");
        }

        // RN-05: CNPJ único, mas ignora a própria gravadora sendo editada
        // Mesmo raciocínio do nome, permite manter o CNPJ atual sem conflito
        boolean cnpjEmUso = gravadoraRepository.findByDcCnpj(gravadoraDTO.dcCnpj())
                .stream()
                .anyMatch(g -> !g.getIdGravadora().equals(id));

        if (cnpjEmUso) {
            throw new RuntimeException("Já existe uma gravadora cadastrada com esse CNPJ.");
        }

        // Atualiza todos os campos da gravadora existente, não cria um novo registro
        gravadora.setDcNome(gravadoraDTO.dcNome());
        gravadora.setDcEndereco(gravadoraDTO.dcEndereco());
        gravadora.setDcTelefone(gravadoraDTO.dcTelefone());
        gravadora.setDcPais(gravadoraDTO.dcPais());
        gravadora.setDtDataFundacao(gravadoraDTO.dtDataFundacao());
        gravadora.setDcCnpj(gravadoraDTO.dcCnpj());

        return gravadoraRepository.save(gravadora);
    }

    // ===================================================================
    // DEMAIS OPERAÇÕES
    // ===================================================================

    /**
     * Retorna todas as gravadoras cadastradas no sistema.
     */
    public List<Gravadora> listarTodos() {
        return gravadoraRepository.findAll();
    }

    /**
     * Busca uma gravadora específica pelo seu ID
     * Lança exceção caso não seja encontrada
     * Também é utilizado internamente pelo AlbumService para validar
     * se a gravadora informada no álbum realmente existe
     */
    public Gravadora buscarPorId(Long id) {
        return gravadoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gravadora não encontrada."));
    }

    /**
     * Remove uma gravadora do sistema pelo seu ID
     * Valida se a gravadora existe antes de tentar deletar
     */
    public void excluir(Long id) {
        if (!gravadoraRepository.existsById(id)) {
            throw new RuntimeException("Gravadora não encontrada para exclusão.");
        }
        gravadoraRepository.deleteById(id);
    }

    /**
     * Remove todas as gravadoras do sistema de uma vez
     * Este método não possui endpoint na API, é utilizado exclusivamente
     * nos testes de integração para garantir um banco limpo antes de cada teste
     */
    public void excluirTodos() {
        gravadoraRepository.deleteAll();
    }
}