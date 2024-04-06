package com.prueba.reservas.domain.model.reservas;

import com.prueba.reservas.services.dto.ReservaDto;
import com.prueba.reservas.services.dto.ResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReservasDominioRespository {

    Mono<ResponseDto> crearReserva(ReservaDto reservaDto);

    Mono<String> actualizarReserva(ReservaDto reservaDto, Integer id);

    Flux<ReservaDto> consultarReservas(String fecha);

    Mono<Integer> consultarDisponibilidad(String fecha);
}
