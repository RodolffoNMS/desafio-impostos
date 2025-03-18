package com.zup.desafio_imposto.exceptions;

public class DuplicateTaxNameException extends RuntimeException{
    public DuplicateTaxNameException(String message) {
        super(message);
    }
}
