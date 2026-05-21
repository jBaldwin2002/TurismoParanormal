package co.paranormal.cqrs.service;

import co.paranormal.cqrs.dto.ReservaEvent;
import co.paranormal.cqrs.persistence.entity.ReservaCommand;
import co.paranormal.cqrs.persistence.entity.ReservaQuery;
import co.paranormal.cqrs.persistence.repository.ReservaQueryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * QUERY SERVICE — escucha eventos de Kafka y actualiza la proyección MongoDB.
 * Implementa ConsultarReservaQuery.
 */
@Service
@AllArgsConstructor
@Log4j2
public class ReservaQueryService {

    private final ReservaQueryRepository reservaQueryRepository;

    // ======================== QUERIES ========================

    public List<ReservaQuery> obtenerTodas() {
        return reservaQueryRepository.findAll();
    }

    public ReservaQuery obtenerPorId(String id) {
        return reservaQueryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada: " + id));
    }

    public List<ReservaQuery> obtenerPorCliente(String emailCliente) {
        return reservaQueryRepository.findByEmailCliente(emailCliente);
    }

    public List<ReservaQuery> obtenerPorTour(String tourId) {
        return reservaQueryRepository.findByTourId(tourId);
    }

    // ======================== KAFKA CONSUMER ========================

    /**
     * Escucha eventos del topic reserva-event-topic y sincroniza la vista de lectura MongoDB.
     */
    @KafkaListener(
            topics = "reserva-event-topic",
            groupId = "reserva-query-group"
    )
    public void procesarEventoReserva(ReservaEvent evento) {
        log.info("Evento recibido en ReservaQueryService: tipo={}", evento.getEventType());
        ReservaCommand reserva = evento.getReserva();

        switch (evento.getEventType()) {
            case "ReservaCreada" -> {
                ReservaQuery proyeccion = ReservaQuery.builder()
                        .id(String.valueOf(reserva.getId()))
                        .tourId(reserva.getTourId())
                        .nombreCliente(reserva.getNombreCliente())
                        .emailCliente(reserva.getEmailCliente())
                        .cantidadPersonas(reserva.getCantidadPersonas())
                        .fechaTour(reserva.getFechaTour())
                        .estado(reserva.getEstado())
                        .build();
                reservaQueryRepository.save(proyeccion);
                log.info("Proyección creada en MongoDB para reservaId={}", reserva.getId());
            }
            case "ReservaCancelada", "ReservaPagada" -> {
                reservaQueryRepository.findById(String.valueOf(reserva.getId()))
                        .ifPresent(r -> {
                            r.setEstado(reserva.getEstado());
                            reservaQueryRepository.save(r);
                            log.info("Estado actualizado en MongoDB: reservaId={} estado={}",
                                    reserva.getId(), reserva.getEstado());
                        });
            }
            default -> log.warn("Tipo de evento desconocido: {}", evento.getEventType());
        }
    }
}
