package com.prueba.reservas.services;

import com.prueba.reservas.domain.model.reservas.Reserva;
import com.prueba.reservas.domain.usecase.ReservasUseCase;
import com.prueba.reservas.services.dto.ReservaDto;
import com.prueba.reservas.services.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
@Log
public class ReservasService {

    private final ReservasUseCase reservasUseCase;

    @PostMapping()
    public Mono<ResponseDto> crearReserva(@RequestBody ReservaDto reservaDto){

        return reservasUseCase.crearReserva(reservaDto);
    }

    @PutMapping()
    public Mono<String> actualizarReserva(){
        log.info("actualizarReserva");
        return Mono.empty();
    }

    @GetMapping()
    public Flux<String> consultarReservas(){

        log.info("actualizarReserva");
        return Flux.empty();
    }


}
