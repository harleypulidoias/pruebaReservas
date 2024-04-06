package com.prueba.reservas.services;

import com.prueba.reservas.domain.model.BussinessException;
import com.prueba.reservas.domain.model.reservas.Reserva;
import com.prueba.reservas.domain.usecase.ReservasUseCase;
import com.prueba.reservas.repository.ReservasRepository;
import com.prueba.reservas.repository.dao.ReservasDao;
import com.prueba.reservas.services.dto.ReservaDto;
import com.prueba.reservas.services.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
@Log
public class ReservasService {

    private final ReservasUseCase reservasUseCase;

    private final ReservasRepository reservasRepository;

    @PostMapping()
    public Mono<ResponseDto> crearReserva(@RequestBody ReservaDto reservaDto){
        return reservasUseCase.crearReserva(reservaDto);
    }

    @PutMapping()
    public Mono<ResponseDto> actualizarReserva(@RequestBody ReservaDto reservaDto, @RequestParam Integer id){
        return reservasUseCase.actualizarReserva(reservaDto, id)
                .flatMap(respuesta -> Mono.just(ResponseDto.builder()
                                .status(HttpStatus.ACCEPTED)
                                .message(respuesta)
                                .build()));
    }

    @GetMapping()
    public Flux<ReservaDto> consultarReservas(@RequestParam String fecha){
        return reservasUseCase.consultarReservas(fecha);
    }

    @GetMapping("availability")
    public Mono<ResponseDto> crearReserva(@RequestParam String fecha){
        return reservasUseCase.consultarDisponibilidad(fecha)
                .flatMap(disponibilidad -> Mono.just(ResponseDto.builder()
                        .message("Es posible realizar "+disponibilidad+" reservas")
                        .status(HttpStatus.ACCEPTED)
                        .build()))
                .switchIfEmpty(Mono.error(new BussinessException("Error en el servidor", HttpStatus.NOT_FOUND)));
    }


}
