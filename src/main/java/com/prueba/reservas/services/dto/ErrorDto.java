package com.prueba.reservas.services.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorDto {

    private String data;
    private HttpStatus status;
    private String message;
}
