package com.prueba.reservas.domain.model.reservas;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Reserva {

    private String nombre;
    private String documento;
    private String tipoDocumento;
    private Integer cantidadComensales;
    private String observaciones;
}

