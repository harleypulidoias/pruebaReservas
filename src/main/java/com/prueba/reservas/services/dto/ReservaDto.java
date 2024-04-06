package com.prueba.reservas.services.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReservaDto {
    private String nombre;
    private String documento;
    private String tipo_documento;
    private Integer comensales;
    private String observaciones;
    private String fecha_reserva;
}
