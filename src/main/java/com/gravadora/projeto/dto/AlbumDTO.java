package com.gravadora.projeto.dto;

import java.sql.Time;
import java.time.LocalDate;


public record AlbumDTO(
    
    long idAlbum,
    String dcTitulo,
    LocalDate dtAnoLancamento,
    int qtdMusica,
    Time tmDuracao,
    long idArtista,
    Long idGravadora

) {}
