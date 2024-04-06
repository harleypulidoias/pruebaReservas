package com.prueba.reservas.domain.model.reservas;

import com.prueba.reservas.services.dto.ReservaDto;
import com.prueba.reservas.services.dto.ResponseDto;
import reactor.core.publisher.Mono;

public interface ReservasDominioRespository {

    Mono<ResponseDto> crearReserva(ReservaDto reservaDto);

    Mono<Reserva> actualizarReserva(ReservaDto reservaDto);
}
