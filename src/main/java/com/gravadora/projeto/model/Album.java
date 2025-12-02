package com.gravadora.projeto.model;

import java.sql.Time;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Album")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAlbum")
    private Long idAlbum;

    @Column(name = "dc_titulo", length = 150, nullable = false)
    private String dcTitulo;

    @Column(name = "dt_ano_lancamento", nullable = false)
    private LocalDate dtAnoLancamento;

    @Column(name = "dc_status", length = 45, nullable = true)
    private String dcStatus;

    @Column(name = "qtd_musica", nullable = false)
    private int qtdMusica;

    @Column(name = "tm_duracao", nullable = false)
    private Time duracao;

    @ManyToOne
    private Artista artista;   // Muitos álbuns → 1 artista

    @ManyToOne
    private Gravadora gravadora; // Muitos álbuns → 1 gravadora
}
