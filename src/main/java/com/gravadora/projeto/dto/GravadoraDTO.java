package com.gravadora.projeto.dto;

import java.time.LocalDate;

public record GravadoraDTO(

    String dcNome,
    String dcEndereco,
    String dcTelefone,
    String dcPais,
    LocalDate dtDataFundacao,
    String dcCnpj

) {}
