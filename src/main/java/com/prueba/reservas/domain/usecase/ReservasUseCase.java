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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@RequiredArgsConstructor
public class ReservasUseCase implements ReservasDominioRespository {

    private final ReservasRepository reservasRepository;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Override
    public Mono<ResponseDto> crearReserva(ReservaDto reservaDto) {

        if(reservaDto.getNombre().length() > 60){
            return Mono.error(new BussinessException("El nombre no cumple con la cantidad de caracteres", HttpStatus.BAD_REQUEST));
        }
        if (reservaDto.getComensales() > 4 ){
            return Mono.error(new BussinessException("La cantidad de comensales no puede ser mayor a 4", HttpStatus.BAD_REQUEST));
        }
        if(!validarFecha(reservaDto.getFecha_reserva())){
            return Mono.error(new BussinessException("La fecha debe manejar el formato 'dd/MM/yyyy HH:mm:ss'", HttpStatus.BAD_REQUEST));
        }

        ReservasDao reservasDao = ReservasDao.builder()
                .nombre(reservaDto.getNombre())
                .documento(reservaDto.getDocumento())
                .tipo_documento(reservaDto.getTipo_documento())
                .comensales(reservaDto.getComensales())
                .observaciones(reservaDto.getObservaciones())
                .fecha_reserva(reservaDto.getFecha_reserva())
                .build();


        return validarDisponibilidad(reservasDao)
                .flatMap(empty -> reservasRepository.save(reservasDao))
                .flatMap(reservaDto1 -> {
                    ResponseDto responseDto = ResponseDto.builder()
                            .reserva(ReservaDto.builder()
                                    .nombre(reservaDto1.getNombre())
                                    .documento(reservaDto1.getDocumento())
                                    .tipo_documento(reservaDto1.getTipo_documento())
                                    .comensales(reservaDto1.getComensales())
                                    .observaciones(reservaDto1.getObservaciones())
                                    .fecha_reserva(reservaDto1.getFecha_reserva())
                                    .build())
                            .message("Reserva creada correctamente")
                            .status(HttpStatus.ACCEPTED)
                            .build();
                    return Mono.just(responseDto);
                })
                .switchIfEmpty(Mono.error(new BussinessException("No se pudo crear la reserva", HttpStatus.NOT_FOUND)));
    }

    @Override
    public Mono<String> actualizarReserva(ReservaDto reservaDto, Integer id) {

        ReservasDao reservasDao = ReservasDao.builder()
                .id(Long.valueOf(id))
                .nombre(reservaDto.getNombre())
                .documento(reservaDto.getDocumento())
                .tipo_documento(reservaDto.getTipo_documento())
                .comensales(reservaDto.getComensales())
                .observaciones(reservaDto.getObservaciones())
                .fecha_reserva(reservaDto.getFecha_reserva())
                .build();

        return validarDisponibilidad(reservasDao).flatMap(empty -> reservasRepository.findById(Long.valueOf(id)))
                .switchIfEmpty(Mono.error(new BussinessException("No se ha encontrado una reserva con el id ("+id+")", HttpStatus.BAD_REQUEST)))
                .flatMap(reservasDao1 -> reservasRepository.updateReserva(
                        reservasDao.getId(),
                        reservasDao.getNombre(),
                        reservasDao.getDocumento(),
                        reservasDao.getTipo_documento(),
                        reservasDao.getComensales(),
                        reservasDao.getObservaciones(),
                        reservasDao.getFecha_reserva()
                        ))
                .flatMap(unused -> Mono.just("Se ha actualizado la reserva correctamente"))
                .doOnError(throwable -> Mono.error(new BussinessException("Error al actualizar la reserva", HttpStatus.BAD_REQUEST)));
    }

    @Override
    public Flux<ReservaDto> consultarReservas(String fecha) {
        return reservasRepository.findByFechaReserva(convertirFechaSinHora(fecha))
                .flatMap(reservasDao1 -> {
                    List<ReservaDto> reservaDtoList = new ArrayList<>();
                    ReservaDto reservaDto = ReservaDto.builder()
                            .nombre(reservasDao1.getNombre())
                            .documento(reservasDao1.getDocumento())
                            .tipo_documento(reservasDao1.getTipo_documento())
                            .comensales(reservasDao1.getComensales())
                            .observaciones(reservasDao1.getObservaciones())
                            .fecha_reserva(reservasDao1.getFecha_reserva())
                            .build();
                    reservaDtoList.add(reservaDto);
                    return Flux.fromIterable(reservaDtoList);
                });
    }

    @Override
    public Mono<Integer> consultarDisponibilidad(String fecha) {
        return reservasRepository.findByFechaReserva(convertirFechaSinHora(fecha))
                .collectList()
                .flatMap(reservasDaoList -> {
                    Integer reservasDisponibles = 10 - reservasDaoList.size();
                    if(reservasDisponibles == 0){
                        return Mono.error(new BussinessException("Ya no es posible hacer mas reservas para la fecha "+fecha, HttpStatus.BAD_REQUEST));
                    }
                    return Mono.just(reservasDisponibles);
                });
    }


    private boolean validarFecha (String fecha){
        try {
            formatter.parse(fecha);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private String convertirFechaSinHora(String fecha){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String fechaSinHora = fecha;
        try {
            Date fechaDate = formatter.parse(fecha);
            fechaSinHora = formatter.format(fechaDate);
            fechaSinHora = fechaSinHora+'%';
        } catch (ParseException e) {
            throw new BussinessException("Error al consultar por fecha", HttpStatus.NOT_FOUND);
        }
        return fechaSinHora;
    }

    private Mono<Integer> validarDisponibilidad( ReservasDao reservaDto){

        return reservasRepository.findByFechaReserva(convertirFechaSinHora(reservaDto.getFecha_reserva()))
                .collectList()
                .flatMapMany(listadoPorDia -> {
                    if(listadoPorDia.size() >= 10){
                        return Mono.error(new BussinessException("Se ha llegado al limite de reservas totales al dia", HttpStatus.CONFLICT));
                    }
                    return Flux.fromIterable(listadoPorDia);
                })
                .filter(listadoPorNombre -> Objects.equals(listadoPorNombre.getNombre(), reservaDto.getNombre()))
                .collectList()
                .flatMap(reservasDaoList -> {
                    if(reservasDaoList.size() >= 2){
                        return Mono.error(new BussinessException("Una persona no puede realizar mas de dos reservas al dia", HttpStatus.CONFLICT));
                    }
                    return Mono.just(reservasDaoList.size());
                });
    }

}
