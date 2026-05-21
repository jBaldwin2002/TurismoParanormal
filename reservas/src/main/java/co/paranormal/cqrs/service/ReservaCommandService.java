package co.paranormal.cqrs.service;

import co.paranormal.cqrs.dto.ReservaEvent;
import co.paranormal.cqrs.persistence.entity.ReservaCommand;
import co.paranormal.cqrs.persistence.repository.ReservaCommandRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * COMMAND SERVICE — maneja escrituras y publica eventos en Kafka.
 * Implementa CrearReservaCommand.
 */
@Service
@AllArgsConstructor
@Log4j2
public class ReservaCommandService {

    private static final String TOPIC = "reserva-event-topic";
    private static final int CUPO_MAXIMO_TOUR = 20;

    private final ReservaCommandRepository reservaCommandRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * CrearReservaCommand: valida y persiste la reserva, luego publica ReservaCreada.
     */
    public ReservaCommand crearReserva(ReservaCommand reserva) {
        // Validación 1: verificar que el tour exista (simulado)
        validarTourExiste(reserva.getTourId());

        // Validación 2: evitar reservas duplicadas
        if (reservaCommandRepository.existsByEmailClienteAndTourIdAndFechaTour(
                reserva.getEmailCliente(), reserva.getTourId(), reserva.getFechaTour())) {
            throw new IllegalStateException(
                    "Ya existe una reserva para este cliente en el mismo tour y fecha.");
        }

        // Validación 3: verificar cupos disponibles
        List<ReservaCommand> reservasExistentes = reservaCommandRepository
                .findByTourIdAndFechaTour(reserva.getTourId(), reserva.getFechaTour());
        int cuposOcupados = reservasExistentes.stream()
                .mapToInt(ReservaCommand::getCantidadPersonas).sum();
        if (cuposOcupados + reserva.getCantidadPersonas() > CUPO_MAXIMO_TOUR) {
            throw new IllegalStateException(
                    "No hay suficientes cupos disponibles para el tour seleccionado.");
        }

        // Guardar reserva en PostgreSQL con estado inicial
        reserva.setEstado("PENDIENTE_PAGO");
        ReservaCommand reservaGuardada = reservaCommandRepository.save(reserva);

        // Publicar evento ReservaCreada → Kafka → lo consumen Pagos e Ingresos
        ReservaEvent evento = new ReservaEvent("ReservaCreada", reservaGuardada);
        kafkaTemplate.send(TOPIC, evento);
        log.info("Evento publicado en Kafka: ReservaCreada para reservaId={}", reservaGuardada.getId());

        return reservaGuardada;
    }

    /**
     * Cancela una reserva existente y publica ReservaCancelada.
     */
    public ReservaCommand cancelarReserva(Long id) {
        ReservaCommand reserva = reservaCommandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada: " + id));

        reserva.setEstado("CANCELADA");
        ReservaCommand reservaActualizada = reservaCommandRepository.save(reserva);

        ReservaEvent evento = new ReservaEvent("ReservaCancelada", reservaActualizada);
        kafkaTemplate.send(TOPIC, evento);
        log.info("Evento publicado en Kafka: ReservaCancelada para reservaId={}", id);

        return reservaActualizada;
    }

    // Simulación de verificación de tour (en producción se llamaría a un servicio externo)
    private void validarTourExiste(String tourId) {
        List<String> toursDisponibles = List.of("TOUR-001", "TOUR-002", "TOUR-003", "TOUR-004");
        if (!toursDisponibles.contains(tourId)) {
            throw new IllegalArgumentException("El tour especificado no existe: " + tourId);
        }
    }
}
