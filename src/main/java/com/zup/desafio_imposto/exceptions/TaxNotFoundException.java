package com.zup.desafio_imposto.exceptions;

public class TaxNotFoundException extends RuntimeException{
    public TaxNotFoundException(String message) {
        super(message);
    }
}
