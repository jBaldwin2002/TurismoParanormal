package co.paranormal.cqrs.service;

import co.paranormal.cqrs.dto.PagoEvent;
import co.paranormal.cqrs.dto.ReservaEvent;
import co.paranormal.cqrs.persistence.entity.PagoCommand;
import co.paranormal.cqrs.persistence.repository.PagoCommandRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * COMMAND SERVICE — maneja el procesamiento de pagos.
 * Escucha ReservaCreada desde Kafka y expone ProcesarPagoCommand.
 * Publica PagoConfirmado para que lo consuma Control de Ingresos.
 */
@Service
@AllArgsConstructor
@Log4j2
public class PagoCommandService {

    private static final String TOPIC_PAGO = "pago-event-topic";
    // Precio base por persona por tour paranormal
    private static final BigDecimal PRECIO_POR_PERSONA = new BigDecimal("75000");
    private static final List<String> METODOS_VALIDOS =
            List.of("TARJETA_CREDITO", "TARJETA_DEBITO", "PSE", "EFECTIVO");

    private final PagoCommandRepository pagoCommandRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * ProcesarPagoCommand: valida y procesa el pago de una reserva.
     */
    public PagoCommand procesarPago(Long reservaId, String metodoPago) {
        // Validación 1: verificar que la reserva exista en este contexto
        // (en producción se haría una llamada al servicio de reservas)
        if (reservaId == null || reservaId <= 0) {
            throw new IllegalArgumentException("El ID de reserva no es válido.");
        }

        // Validación 2: validar método de pago
        if (!METODOS_VALIDOS.contains(metodoPago)) {
            throw new IllegalArgumentException(
                    "Método de pago no válido. Use: " + METODOS_VALIDOS);
        }

        // Validación 3: detectar pagos duplicados
        if (pagoCommandRepository.existsByReservaIdAndEstado(reservaId, "CONFIRMADO")) {
            throw new IllegalStateException(
                    "La reserva " + reservaId + " ya tiene un pago confirmado.");
        }

        // Calcular monto total (recuperado del evento guardado previamente)
        PagoCommand pagoExistente = pagoCommandRepository.findByReservaId(reservaId)
                .orElseThrow(() -> new RuntimeException(
                        "No se encontró registro de pago para la reserva: " + reservaId));

        // Validación 4: confirmar monto correcto
        BigDecimal montoEsperado = PRECIO_POR_PERSONA
                .multiply(BigDecimal.valueOf(pagoExistente.getMontoTotal().longValue())); // ya calculado
        // Procesar pago (simulación — en prod se integraría con pasarela)
        pagoExistente.setMetodoPago(metodoPago);
        pagoExistente.setEstado("CONFIRMADO");
        pagoExistente.setFechaPago(LocalDateTime.now());
        pagoExistente.setComprobante("COMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        PagoCommand pagoGuardado = pagoCommandRepository.save(pagoExistente);

        // Publicar PagoConfirmado → Kafka → lo consume Control de Ingresos
        PagoEvent evento = new PagoEvent("PagoConfirmado", pagoGuardado);
        kafkaTemplate.send(TOPIC_PAGO, evento);
        log.info("Evento publicado: PagoConfirmado para reservaId={} comprobante={}",
                reservaId, pagoGuardado.getComprobante());

        return pagoGuardado;
    }

    /**
     * Escucha ReservaCreada desde Kafka y crea el registro de pago pendiente.
     */
    @KafkaListener(
            topics = "reserva-event-topic",
            groupId = "pago-command-group"
    )
    public void recibirReservaCreada(ReservaEvent evento) {
        if (!"ReservaCreada".equals(evento.getEventType())) return;

        ReservaEvent.ReservaData reserva = evento.getReserva();
        log.info("Recibida ReservaCreada en PagoCommandService: reservaId={}", reserva.getId());

        // Calcular monto total
        BigDecimal montoTotal = PRECIO_POR_PERSONA
                .multiply(BigDecimal.valueOf(reserva.getCantidadPersonas()));

        PagoCommand nuevoPago = PagoCommand.builder()
                .reservaId(reserva.getId())
                .tourId(reserva.getTourId())
                .emailCliente(reserva.getEmailCliente())
                .montoTotal(montoTotal)
                .estado("PENDIENTE")
                .build();

        pagoCommandRepository.save(nuevoPago);
        log.info("Registro de pago PENDIENTE creado para reservaId={} monto={}",
                reserva.getId(), montoTotal);
    }
}
