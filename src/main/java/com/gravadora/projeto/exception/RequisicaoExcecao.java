package com.gravadora.projeto.exception;

import lombok.Getter;

@Getter
public class RequisicaoExcecao extends RuntimeException{
    private final String erroCode;

    public RequisicaoExcecao(String erroCode, String message){
        super(message);
        this.erroCode = erroCode;
    }
}