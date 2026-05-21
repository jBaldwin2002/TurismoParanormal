package co.paranormal.cqrs.service;

import co.paranormal.cqrs.dto.IngresoEvent;
import co.paranormal.cqrs.persistence.entity.IngresoCommand;
import co.paranormal.cqrs.persistence.entity.IngresoQuery;
import co.paranormal.cqrs.persistence.repository.IngresoQueryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * QUERY SERVICE — proyección de lectura de ingresos en MongoDB.
 * Implementa ConsultarIngresoQuery.
 */
@Service
@AllArgsConstructor
@Log4j2
public class IngresoQueryService {

    private final IngresoQueryRepository ingresoQueryRepository;

    public List<IngresoQuery> obtenerTodos() {
        return ingresoQueryRepository.findAll();
    }

    public Optional<IngresoQuery> obtenerPorQr(String codigoQr) {
        return ingresoQueryRepository.findByCodigoQr(codigoQr);
    }

    public List<IngresoQuery> obtenerPorTour(String tourId) {
        return ingresoQueryRepository.findByTourId(tourId);
    }

    public List<IngresoQuery> obtenerPorEstado(String estado) {
        return ingresoQueryRepository.findByEstado(estado);
    }

    public List<IngresoQuery> obtenerPorCliente(String emailCliente) {
        return ingresoQueryRepository.findByEmailCliente(emailCliente);
    }

    /**
     * Escucha IngresoRegistrado y actualiza la proyección MongoDB.
     */
    @KafkaListener(
            topics = "ingreso-event-topic",
            groupId = "ingreso-query-group"
    )
    public void procesarEventoIngreso(IngresoEvent evento) {
        log.info("Evento recibido en IngresoQueryService: tipo={}", evento.getEventType());
        IngresoCommand ingreso = evento.getIngreso();

        IngresoQuery proyeccion = IngresoQuery.builder()
                .id(String.valueOf(ingreso.getId()))
                .reservaId(ingreso.getReservaId())
                .tourId(ingreso.getTourId())
                .emailCliente(ingreso.getEmailCliente())
                .codigoQr(ingreso.getCodigoQr())
                .fechaTour(ingreso.getFechaTour())
                .momentoIngreso(ingreso.getMomentoIngreso())
                .estado(ingreso.getEstado())
                .motivoRechazo(ingreso.getMotivoRechazo())
                .build();

        ingresoQueryRepository.save(proyeccion);
        log.info("Proyección de ingreso actualizada en MongoDB: reservaId={} estado={}",
                ingreso.getReservaId(), ingreso.getEstado());
    }
}
