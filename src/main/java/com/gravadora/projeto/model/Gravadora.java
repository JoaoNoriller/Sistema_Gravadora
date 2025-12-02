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
@Table(name = "Gravadora")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gravadora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idGravadora")
    private Long idGravadora;

    @Column(name = "dc_nome", length = 45, nullable = false)
    private String dcNome;

    @Column(name = "dc_endereco", length = 150, nullable = false)
    private String dcEndereco;

    @Column(name = "dc_telefone", length = 11, nullable = false)
    private String dcTelefone;

    @Column(name = "dc_pais", length = 50, nullable = false)
    private String dcPais;

    @Column(name = "dt_data_fundacao", nullable = false)
    private LocalDate dtDataFundacao;

    @Column(name = "dc_cnpj", length = 14, nullable = false)
    private String dcCnpj;
}
