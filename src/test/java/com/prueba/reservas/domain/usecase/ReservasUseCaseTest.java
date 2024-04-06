package com.prueba.reservas.domain.usecase;

import com.prueba.reservas.domain.model.BussinessException;
import com.prueba.reservas.domain.model.reservas.Reserva;
import com.prueba.reservas.repository.ReservasRepository;
import com.prueba.reservas.repository.dao.ReservasDao;
import com.prueba.reservas.services.dto.ReservaDto;
import com.prueba.reservas.services.dto.ResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class ReservasUseCaseTest {

    @InjectMocks
    ReservasUseCase reservasUseCase;

    @Mock
    ReservasRepository reservasRepository;

    @Mock
    SimpleDateFormat simpleDateFormat;

    ReservasDao reservaDao = ReservasDao.builder()
            .nombre("nombreTest")
            .documento("123456")
            .comensales(2)
            .fecha_reserva("06/05/2024 16:00:00")
            .build();

    ReservaDto reservaDto = ReservaDto.builder()
            .nombre("nombreTest")
            .documento("123456")
            .comensales(2)
            .fecha_reserva("06/05/2024 16:00:00")
            .build();

    @BeforeEach
    public void setUp()
    {
    }


    @Test
    void crearReserva(){

        Mockito.when(reservasRepository.save(any())).thenReturn(Mono.just(reservaDao));
        Mockito.when(reservasRepository.findByFechaReserva(any())).thenReturn(Flux.just(reservaDao));

        Mono<ResponseDto> reservaMono = reservasUseCase.crearReserva(reservaDto);

        StepVerifier.create(reservaMono).consumeNextWith(responseDto -> {
            Assertions.assertEquals("nombreTest", responseDto.getReserva().getNombre());
        }).verifyComplete();

    }

    @Test
    void errorCrearReservaPorMasDe10Reservas(){

        List<ReservasDao> reservasDaoList = new ArrayList<>();

        for (int index =0; index < 12; index++ ) {
            reservaDao.setNombre("nombreTest"+index);
            reservasDaoList.add(reservaDao);
        }

        Mockito.when(reservasRepository.save(any())).thenReturn(Mono.just(reservaDao));
        Mockito.when(reservasRepository.findByFechaReserva(any())).thenReturn(Flux.fromIterable(reservasDaoList));

        Mono<ResponseDto> reservaMono = reservasUseCase.crearReserva(reservaDto);

        StepVerifier.create(reservaMono).expectErrorMessage("Se ha llegado al limite de reservas totales al dia").verify();

    }

    @Test
    void errorCrearReservaPorUnNombreConMas2Reservas(){

        List<ReservasDao> reservasDaoList = new ArrayList<>();

        for (int index =0; index < 3; index++ ) {
            reservaDao.setNombre("nombreTest");
            reservasDaoList.add(reservaDao);
        }

        Mockito.when(reservasRepository.save(any())).thenReturn(Mono.just(reservaDao));
        Mockito.when(reservasRepository.findByFechaReserva(any())).thenReturn(Flux.fromIterable(reservasDaoList));

        Mono<ResponseDto> reservaMono = reservasUseCase.crearReserva(reservaDto);

        StepVerifier.create(reservaMono).expectErrorMessage("Una persona no puede realizar mas de dos reservas al dia").verify();

    }

    @Test
    void actualizarReserva() {
    }
}