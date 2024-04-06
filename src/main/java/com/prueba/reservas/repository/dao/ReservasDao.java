package com.prueba.reservas.repository.dao;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("reservas")
public class ReservasDao {

    @Id
    private Long id;
    private String nombre;
    private String documento;
    private String tipo_documento;
    private Integer comensales;
    private String observaciones;
}
