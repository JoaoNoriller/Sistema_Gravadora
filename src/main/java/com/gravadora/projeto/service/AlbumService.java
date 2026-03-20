package com.gravadora.projeto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gravadora.projeto.dto.AlbumDTO;
import com.gravadora.projeto.model.Album;
import com.gravadora.projeto.model.Artista;
import com.gravadora.projeto.model.Gravadora;
import com.gravadora.projeto.repository.AlbumRepository;

/**
 * Camada de serviço responsável por toda a lógica de negócio relacionada aos álbuns.
 * Aqui ficam as validações, regras e operações antes de qualquer acesso ao banco de dados.
 */
@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    // Injetamos os services de Artista e Gravadora para validar
    // se eles existem antes de salvar o álbum — evitando acesso direto aos repositories
    @Autowired
    private ArtistaService artistaService;

    @Autowired
    private GravadoraService gravadoraService;

    // ===================================================================
    // CRIAR ÁLBUM — chamado pelo POST /album
    // ===================================================================

    /**
     * Cria um novo álbum após passar por todas as validações de negócio.
     * Nenhum campo de ID é necessário — o banco gera automaticamente.
     */
    public Album salvarAlbum(AlbumDTO albumDTO) {

        // Verifica se o artista informado existe no sistema
        // Se não existir, o service de Artista já lança uma exceção automaticamente
        Artista artista = artistaService.buscarPorId(albumDTO.idArtista());

        // Mesma verificação para a gravadora
        Gravadora gravadora = gravadoraService.buscarPorId(albumDTO.idGravadora());

        // RN-01: O álbum precisa ter mais de 5 músicas para ser cadastrado
        if (albumDTO.qtdMusica() <= 5) {
            throw new RuntimeException("O álbum deve ter mais de 5 músicas.");
        }

        // RN-02: O título não pode ser nulo, vazio ou ter menos de 3 caracteres
        if (albumDTO.dcTitulo() == null || albumDTO.dcTitulo().trim().length() < 3) {
            throw new RuntimeException("O título do álbum deve ter no mínimo 3 caracteres.");
        }

        // RN-03: Um artista não pode ter dois álbuns com o mesmo título
        // Buscamos todos os álbuns desse artista com o mesmo título,
        // se encontrar qualquer um, o cadastro será bloqueado
        List<Album> albumsComMesmoTitulo = albumRepository
                .findByDcTituloAndArtista_IdArtista(albumDTO.dcTitulo(), artista.getIdArtista());

        if (!albumsComMesmoTitulo.isEmpty()) {
            throw new RuntimeException("Este artista já possui um álbum com esse título.");
        }

        // RN-04: Um artista não pode lançar mais de 10 álbuns no mesmo ano
        int totalAno = albumRepository.countByArtista_IdArtistaAndDtAnoLancamento(
                artista.getIdArtista(), albumDTO.dtAnoLancamento());

        if (totalAno >= 10) {
            throw new RuntimeException("O artista já lançou o máximo de 10 álbuns neste ano.");
        }

        // RN-05: A duração total do álbum não pode ultrapassar 2 horas (7200 segundos)
        // Esse campo é opcional — só validamos se for informado
        if (albumDTO.tmDuracao() != null) {
            int duracaoSegundos = albumDTO.tmDuracao().toLocalTime().toSecondOfDay();
            if (duracaoSegundos > 7200) {
                throw new RuntimeException("A duração total do álbum não pode ultrapassar 2 horas.");
            }
        }

        // RN-06: O status é definido automaticamente com base na quantidade de músicas
        // 10 ou mais músicas = COMPLETO | menos de 10 = INCOMPLETO
        String status = albumDTO.qtdMusica() >= 10 ? "COMPLETO" : "INCOMPLETO";

        // Todas as validações passaram — montamos e salvamos o álbum
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

    // ===================================================================
    // ATUALIZAR ÁLBUM — chamado pelo PUT /album/{id}
    // ===================================================================

    /**
     * Atualiza um álbum existente identificado pelo ID informado na URL.
     * Aplica as mesmas regras de negócio do cadastro, com exceção da
     * verificação de título duplicado — que ignora o próprio álbum sendo editado.
     */
    public Album atualizarAlbum(Long id, AlbumDTO albumDTO) {

        // Primeiro buscamos o álbum que será atualizado
        // Se não existir, lançamos uma exceção informando que não foi encontrado
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado."));

        // Validamos artista e gravadora da mesma forma que no cadastro
        Artista artista = artistaService.buscarPorId(albumDTO.idArtista());
        Gravadora gravadora = gravadoraService.buscarPorId(albumDTO.idGravadora());

        // RN-01: Mais de 5 músicas obrigatório
        if (albumDTO.qtdMusica() <= 5) {
            throw new RuntimeException("O álbum deve ter mais de 5 músicas.");
        }

        // RN-02: Título com no mínimo 3 caracteres
        if (albumDTO.dcTitulo() == null || albumDTO.dcTitulo().trim().length() < 3) {
            throw new RuntimeException("O título do álbum deve ter no mínimo 3 caracteres.");
        }

        // RN-03: Título único por artista — mas agora ignoramos o próprio álbum
        // Isso permite editar outros campos (ex: quantidade de músicas) sem precisar
        // mudar o título, pois o título já pertence a esse mesmo álbum
        List<Album> albumsComMesmoTitulo = albumRepository
                .findByDcTituloAndArtista_IdArtista(albumDTO.dcTitulo(), artista.getIdArtista());

        for (Album a : albumsComMesmoTitulo) {
            // Se encontrou um álbum com esse título mas é diferente do que estamos editando
            // significa que outro álbum já usa esse título — será bloqueado
            if (!a.getIdAlbum().equals(id)) {
                throw new RuntimeException("Este artista já possui um álbum com esse título.");
            }
        }

        // RN-04: Máximo 10 álbuns por ano
        // Como estamos editando um álbum que já existe, descontamos ele da contagem
        // para não bloquear indevidamente a edição
        int totalAno = albumRepository.countByArtista_IdArtistaAndDtAnoLancamento(
                artista.getIdArtista(), albumDTO.dtAnoLancamento());

        if (album.getDtAnoLancamento().equals(albumDTO.dtAnoLancamento())) {
            totalAno--; // desconta o próprio álbum — ele já estava contado
        }

        if (totalAno >= 10) {
            throw new RuntimeException("O artista já lançou o máximo de 10 álbuns neste ano.");
        }

        // RN-05: Duração máxima de 2 horas
        if (albumDTO.tmDuracao() != null) {
            int duracaoSegundos = albumDTO.tmDuracao().toLocalTime().toSecondOfDay();
            if (duracaoSegundos > 7200) {
                throw new RuntimeException("A duração total do álbum não pode ultrapassar 2 horas.");
            }
        }

        // RN-06: Recalcula o status com base na nova quantidade de músicas informada
        String status = albumDTO.qtdMusica() >= 10 ? "COMPLETO" : "INCOMPLETO";

        // Atualiza os campos do álbum existente — não cria um novo registro
        album.setArtista(artista);
        album.setGravadora(gravadora);
        album.setDcTitulo(albumDTO.dcTitulo());
        album.setDtAnoLancamento(albumDTO.dtAnoLancamento());
        album.setQtdMusica(albumDTO.qtdMusica());
        album.setTmDuracao(albumDTO.tmDuracao());
        album.setDcStatus(status);

        return albumRepository.save(album);
    }

    // ===================================================================
    // DEMAIS OPERAÇÕES
    // ===================================================================

    /**
     * Retorna todos os álbuns cadastrados no sistema.
     */
    public List<Album> listar() {
        return albumRepository.findAll();
    }

    /**
     * Busca um álbum específico pelo seu ID.
     * Lança exceção caso não seja encontrado.
     */
    public Album buscarPorId(Long id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado."));
    }

    /**
     * Retorna todos os álbuns vinculados a um artista específico.
     * Útil para verificar quantos álbuns um artista possui.
     */
    public List<Album> listarPorArtista(Long idArtista) {
        // Valida se o artista existe antes de buscar os álbuns
        artistaService.buscarPorId(idArtista);
        return albumRepository.findByArtista_IdArtista(idArtista);
    }

    /**
     * Remove um álbum pelo ID.
     * Valida se o álbum existe antes de tentar deletar.
     */
    public void deletar(Long id) {
        if (!albumRepository.existsById(id)) {
            throw new RuntimeException("Álbum não encontrado para exclusão.");
        }
        albumRepository.deleteById(id);
    }
}