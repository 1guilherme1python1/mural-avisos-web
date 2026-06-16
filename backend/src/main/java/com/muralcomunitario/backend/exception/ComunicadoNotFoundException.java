package com.muralcomunitario.backend.exception;

public class ComunicadoNotFoundException extends RuntimeException {

    public ComunicadoNotFoundException(Long id) {
        super("Comunicado com id " + id + " nao encontrado.");
    }
}
