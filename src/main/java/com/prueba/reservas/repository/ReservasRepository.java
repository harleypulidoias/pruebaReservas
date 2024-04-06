package com.prueba.reservas.repository;

import com.prueba.reservas.repository.dao.ReservasDao;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservasRepository extends R2dbcRepository<ReservasDao, Long> {

}
