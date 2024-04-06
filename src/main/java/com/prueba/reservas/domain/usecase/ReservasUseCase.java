package com.prueba.reservas.domain.usecase;

import com.prueba.reservas.domain.model.BussinessException;
import com.prueba.reservas.domain.model.reservas.Reserva;
import com.prueba.reservas.domain.model.reservas.ReservasDominioRespository;
import com.prueba.reservas.repository.ReservasRepository;
import com.prueba.reservas.repository.dao.ReservasDao;
import com.prueba.reservas.services.dto.ReservaDto;
import com.prueba.reservas.services.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReservasUseCase implements ReservasDominioRespository {

    private final ReservasRepository reservasRepository;


    @Override
    public Mono<ResponseDto> crearReserva(ReservaDto reservaDto) {

        if(reservaDto.getNombre().length() > 60){
            return Mono.error(new BussinessException("El nombre no cumple con la cantidad de caracteres", HttpStatus.BAD_REQUEST));
        }
        if (reservaDto.getComensales() > 4 ){
            return Mono.error(new BussinessException("La cantidad de comensales no puede ser mayor a 4", HttpStatus.BAD_REQUEST));
        }

        ReservasDao reservasDao = ReservasDao.builder()
                .nombre(reservaDto.getNombre())
                .documento(reservaDto.getDocumento())
                .tipo_documento(reservaDto.getTipo_documento())
                .comensales(reservaDto.getComensales())
                .observaciones(reservaDto.getObservaciones())
                .build();

        ResponseDto responseDto = ResponseDto.builder()
                .reserva(ReservaDto.builder()
                        .nombre(reservaDto.getNombre())
                        .documento(reservaDto.getDocumento())
                        .tipo_documento(reservaDto.getTipo_documento())
                        .comensales(reservaDto.getComensales())
                        .observaciones(reservaDto.getObservaciones())
                        .build())
                .message("success")
                .status(HttpStatus.ACCEPTED)
                .build();

        return reservasRepository.save(reservasDao)
                .flatMap(reservaDto1 -> Mono.just(responseDto));
    }

    @Override
    public Mono<Reserva> actualizarReserva(ReservaDto reservaDto) {
        return null;
    }
}
