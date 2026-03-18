package com.gravadora.projeto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gravadora.projeto.model.Gravadora;

public interface GravadoraRepository extends JpaRepository<Gravadora, Long> {

     List<Gravadora> findByDcNome(String dcNome);   // ← validação de nome duplicado

    List<Gravadora> findByDcCnpj(String dcCnpj);   // ← validação de CNPJ duplicado
}


