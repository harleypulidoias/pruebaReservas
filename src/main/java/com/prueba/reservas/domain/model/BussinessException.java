package com.prueba.reservas.domain.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class BussinessException extends RuntimeException{

    private String message;
    private HttpStatus code;

    public BussinessException(String message, HttpStatus code) {
        super(message);
        this.message = message;
        this.code = code;
    }
}
