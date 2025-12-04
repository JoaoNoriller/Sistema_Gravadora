package com.gravadora.projeto.model;

import java.sql.Time;
import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "album")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_album")
    private Long idAlbum;

    @Column(name = "dc_titulo", length = 150, nullable = false)
    private String dcTitulo;

    @Column(name = "dt_ano_lancamento", nullable = false)
    private LocalDate dtAnoLancamento;

    @Column(name = "dc_status", length = 45)
    private String dcStatus;

    @Column(name = "qtd_musica", nullable = false)
    private int qtdMusica;

    @Column(name = "tm_tmduracao", nullable = false)
    private Time tmduracao;

    @ManyToOne
    @JoinColumn(name = "id_artista", nullable = false)
    private Artista artista;

    @ManyToOne
    @JoinColumn(name = "id_gravadora", nullable = false)
    private Gravadora gravadora;
}
