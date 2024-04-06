package com.prueba.reservas.services.dto;

import com.prueba.reservas.domain.model.reservas.Reserva;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResponseDto {
    private ReservaDto reserva;
    private HttpStatus status;
    private String message;
}