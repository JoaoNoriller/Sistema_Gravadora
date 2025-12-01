package com.gravadora.projeto.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gravadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGravadora;
    private String dcNome;
    private String dcEndereco;
    private String dcTelefone;
    private String dcPais;
    private LocalDate dtDataFundacao; 
    private String dcCnpj;
}


