package com.prueba.reservas.repository;

import com.prueba.reservas.repository.dao.ReservasDao;
import com.prueba.reservas.services.dto.ReservaDto;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReservasRepository extends R2dbcRepository<ReservasDao, Long> {

    @Query("SELECT * FROM reservas WHERE fecha_reserva LIKE :fechaReserva")
    Flux<ReservasDao> findByFechaReserva(String fechaReserva);


    @Modifying
    @Query("UPDATE reservas SET " +
            "nombre = :nombre, " +
            "documento = :documento, " +
            "tipo_documento = :tipo_documento, " +
            "comensales = :comensales, " +
            "observaciones = :observaciones, " +
            "fecha_reserva = :fecha_reserva " +
            "WHERE id = :id")
    Mono<Void> updateReserva(Long id, String nombre, String documento, String tipo_documento, Integer comensales, String observaciones, String fecha_reserva);
}
