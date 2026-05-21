package co.paranormal.cqrs.service;

import co.paranormal.cqrs.dto.PagoEvent;
import co.paranormal.cqrs.persistence.entity.PagoCommand;
import co.paranormal.cqrs.persistence.entity.PagoQuery;
import co.paranormal.cqrs.persistence.repository.PagoQueryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * QUERY SERVICE — proyección de lectura de pagos en MongoDB.
 * Implementa ConsultarPagoQuery.
 */
@Service
@AllArgsConstructor
@Log4j2
public class PagoQueryService {

    private final PagoQueryRepository pagoQueryRepository;

    public List<PagoQuery> obtenerTodos() {
        return pagoQueryRepository.findAll();
    }

    public Optional<PagoQuery> obtenerPorReserva(Long reservaId) {
        return pagoQueryRepository.findByReservaId(reservaId);
    }

    public List<PagoQuery> obtenerPorCliente(String emailCliente) {
        return pagoQueryRepository.findByEmailCliente(emailCliente);
    }

    public List<PagoQuery> obtenerPorEstado(String estado) {
        return pagoQueryRepository.findByEstado(estado);
    }

    /**
     * Escucha PagoConfirmado / PagoRechazado y actualiza la vista de lectura.
     */
    @KafkaListener(
            topics = "pago-event-topic",
            groupId = "pago-query-group"
    )
    public void procesarEventoPago(PagoEvent evento) {
        log.info("Evento recibido en PagoQueryService: tipo={}", evento.getEventType());
        PagoCommand pago = evento.getPago();

        PagoQuery proyeccion = PagoQuery.builder()
                .id(String.valueOf(pago.getId()))
                .reservaId(pago.getReservaId())
                .tourId(pago.getTourId())
                .emailCliente(pago.getEmailCliente())
                .montoTotal(pago.getMontoTotal())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .fechaPago(pago.getFechaPago())
                .comprobante(pago.getComprobante())
                .build();

        pagoQueryRepository.save(proyeccion);
        log.info("Proyección de pago actualizada en MongoDB para reservaId={}", pago.getReservaId());
    }
}
