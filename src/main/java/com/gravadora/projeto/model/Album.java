package com.gravadora.projeto.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlbum;
    private String dcTitulo;
    private LocalDate dtAnoLancamento;
    private String dcStatus;

    @ManyToOne
    private Artista artista;   //relacionamento: Muitos Álbum → 1 Artista

    @ManyToOne
    private Gravadora gravadora; //relacionamento: Muitos Álbum → 1 Gravadora
}
