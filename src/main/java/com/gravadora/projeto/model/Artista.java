package com.gravadora.projeto.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Artista")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idArtista")
    private Long idArtista;

    @Column(name = "dc_nome", length = 45, nullable = false)
    private String dcNome;

    @Column(name = "dc_endereco", length = 150, nullable = true)
    private String dcEndereco;

    @Column(name = "dt_nascimento", nullable = true)
    private LocalDate dtNascimento;

    @Column(name = "dc_nacionalidade", length = 45, nullable = true)
    private String dcNacionalidade;

    @Column(name = "dc_genero_musical", length = 100, nullable = true)
    private String dcGeneroMusical;

}
