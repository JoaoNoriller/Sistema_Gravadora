package com.gravadora.projeto.dto;

import java.sql.Time;
import java.time.LocalDate;

public record AlbumDTO(

    Long idAlbum,
    String dcTitulo,
    LocalDate dtAnoLancamento,
    String dcStatus,     
    Integer qtdMusica,
    Time tmDuracao,
    Long idArtista,
    Long idGravadora

) {}