package com.prueba.reservas.config;

import com.prueba.reservas.domain.model.BussinessException;
import com.prueba.reservas.services.ReservasService;
import com.prueba.reservas.services.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptions {


    @ExceptionHandler(value = BussinessException.class)
    private ResponseEntity<ErrorDto> handlerException(BussinessException exception){
        ErrorDto bussinessException = ErrorDto.builder()
                .message(exception.getMessage())
                .data(null)
                .status(HttpStatus.CONFLICT)
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(bussinessException);
    }
}
