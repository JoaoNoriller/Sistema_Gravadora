package com.gravadora.projeto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gravadora.projeto.dto.ArtistaDTO;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.repository.ArtistaRepository;

/**
 * Camada de serviço responsável por toda a lógica de negócio relacionada aos artistas
 * Aqui ficam as validações e regras antes de qualquer acesso ao banco de dados
 */
@Service
public class ArtistaService {

    private final ArtistaRepository artistaRepository;

    public ArtistaService(ArtistaRepository artistaRepository) {
        this.artistaRepository = artistaRepository;
    }

    // ===================================================================
    // CRIAR ARTISTA — chamado pelo POST /artista
    // ===================================================================

    /**
     * Cria um novo artista após passar pelas validações de negócio
     * O ID é gerado automaticamente pelo banco, não precisa ser informado
     */
    public Artista salvar(ArtistaDTO artistaDTO) {

        // RN-01: O nome do artista é obrigatório
        // Não permitimos cadastrar um artista sem nome ou com nome em branco
        if (artistaDTO.dcNome() == null || artistaDTO.dcNome().trim().isEmpty()) {
            throw new RuntimeException("O nome do artista é obrigatório.");
        }

        // RN-02: O nome do artista deve ser único no sistema
        // Buscamos se já existe algum artista com esse nome antes de salvar
        List<Artista> artistasComMesmoNome = artistaRepository.findByDcNome(artistaDTO.dcNome());
        if (!artistasComMesmoNome.isEmpty()) {
            throw new RuntimeException("Já existe um artista cadastrado com esse nome.");
        }

        // Todas as validações passaram, montamos e salvamos o artista
        Artista artista = new Artista();
        artista.setDcNome(artistaDTO.dcNome());
        artista.setDcEndereco(artistaDTO.dcEndereco());
        artista.setDtNascimento(artistaDTO.dtNascimento());
        artista.setDcNacionalidade(artistaDTO.dcNacionalidade());
        artista.setDcGeneroMusical(artistaDTO.dcGeneroMusical());

        return artistaRepository.save(artista);
    }

    // ===================================================================
    // ATUALIZAR ARTISTA — chamado pelo PUT /artista/{id}
    // ===================================================================

    /**
     * Atualiza os dados de um artista existente identificado pelo ID informado na URL
     * Aplica as mesmas regras do cadastro, mas ignora o próprio artista
     * na verificação de nome duplicado, permitindo editar outros campos
     * sem precisar mudar o nome
     */
    public Artista atualizar(Long id, ArtistaDTO artistaDTO) {

        // RN-01: Nome obrigatório também no update
        if (artistaDTO.dcNome() == null || artistaDTO.dcNome().trim().isEmpty()) {
            throw new RuntimeException("O nome do artista é obrigatório.");
        }

        // Buscamos o artista que será atualizado
        // Se não existir no banco, lançamos uma exceção informando que não foi encontrado
        Artista artista = artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado."));

        // RN-02: Nome único, mas ignora o próprio artista sendo editado
        // Isso permite editar outros campos sem precisar trocar o nome,
        // mas bloqueia se outro artista diferente já usar esse nome
        List<Artista> artistasComMesmoNome = artistaRepository.findByDcNome(artistaDTO.dcNome());
        boolean nomeEmUso = artistasComMesmoNome.stream()
                .anyMatch(a -> !a.getIdArtista().equals(id));

        if (nomeEmUso) {
            throw new RuntimeException("Já existe um artista cadastrado com esse nome.");
        }

        // Atualiza todos os campos do artista existente, não cria um novo registro
        artista.setDcNome(artistaDTO.dcNome());
        artista.setDcEndereco(artistaDTO.dcEndereco());
        artista.setDtNascimento(artistaDTO.dtNascimento());
        artista.setDcNacionalidade(artistaDTO.dcNacionalidade());
        artista.setDcGeneroMusical(artistaDTO.dcGeneroMusical());

        return artistaRepository.save(artista);
    }

    // ===================================================================
    // DEMAIS OPERAÇÕES
    // ===================================================================

    /**
     * Retorna todos os artistas cadastrados no sistema
     */
    public List<Artista> listarTodos() {
        return artistaRepository.findAll();
    }

    /**
     * Busca um artista específico pelo seu ID
     * Lança exceção caso não seja encontrado
     * Também é utilizado internamente pelo AlbumService para validar
     * se o artista informado no álbum realmente existe
     */
    public Artista buscarPorId(Long id) {
        return artistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado."));
    }

    /**
     * Remove um artista do sistema pelo seu ID
     */
    public void excluir(Long id) {
        artistaRepository.deleteById(id);
    }

    /**
     * Remove todos os artistas do sistema de uma vez
     * Este método não possui endpoint na API, é Utilizado exclusivamente
     * nos testes de integração para garantir um banco limpo antes de cada teste
     */
    public void excluirTodos() {
        artistaRepository.deleteAll();
    }
}