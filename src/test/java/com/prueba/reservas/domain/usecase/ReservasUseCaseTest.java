package com.prueba.reservas.domain.usecase;

import com.prueba.reservas.domain.model.reservas.Reserva;
import com.prueba.reservas.repository.ReservasRepository;
import com.prueba.reservas.repository.dao.ReservasDao;
import com.prueba.reservas.services.dto.ReservaDto;
import com.prueba.reservas.services.dto.ResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

//@WebFluxTest(ReservasUseCase.class)
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = ReservasUseCase.class)
class ReservasUseCaseTest {

    @InjectMocks
    ReservasUseCase reservasUseCase;

    @MockBean
    ReservasRepository reservasRepository;

    ReservasDao reservaDto = ReservasDao.builder()
            .nombre("nombreTest")
            .documento("123456")
            .comensales(2)
            .build();

    @BeforeEach
    public void setUp()
    {
//        Mockito.when(reservasRepository.save(any())).thenReturn(Mono.just(reservaDto));

        MockitoAnnotations.openMocks(this);

        // Configurar el servicio con el mock
        reservasUseCase = new ReservasUseCase(reservasRepository);
    }


    @Test
    void crearReserva() {

        Mockito.when(reservasRepository.save(any())).thenReturn(Mono.just(reservaDto));

        Mono<ResponseDto> reservaMono = reservasUseCase.crearReserva(reservaDto);

        StepVerifier.create(reservaMono).consumeNextWith(responseDto -> {
            Assertions.assertEquals("nombreTest", responseDto.getReserva().getNombre());
        }).verifyComplete();

    }

    @Test
    void actualizarReserva() {
    }
}