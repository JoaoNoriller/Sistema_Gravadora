package com.gravadora.projeto.dto;

import java.time.LocalDate;

public record ArtistaDTO(

    String dcNome,
    String dcEndereco,
    LocalDate dtNascimento,
    String dcNacionalidade,
    String dcGeneroMusical
    
) {}
