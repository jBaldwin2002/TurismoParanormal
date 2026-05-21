package co.paranormal.cqrs.service;

import co.paranormal.cqrs.dto.IngresoEvent;
import co.paranormal.cqrs.dto.PagoEvent;
import co.paranormal.cqrs.persistence.entity.IngresoCommand;
import co.paranormal.cqrs.persistence.repository.IngresoCommandRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * COMMAND SERVICE — controla el ingreso físico al tour paranormal.
 * Escucha PagoConfirmado, genera QR y registra el ingreso.
 * Implementa RegistrarIngresoCommand.
 */
@Service
@AllArgsConstructor
@Log4j2
public class IngresoCommandService {

    private static final String TOPIC_INGRESO = "ingreso-event-topic";

    private final IngresoCommandRepository ingresoCommandRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * RegistrarIngresoCommand: valida el código QR y registra la entrada al tour.
     */
    public IngresoCommand registrarIngreso(String codigoQr) {
        // Validación 1: verificar que el código QR existe y está HABILITADO
        IngresoCommand ingreso = ingresoCommandRepository.findByCodigoQr(codigoQr)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Código QR no válido o no registrado: " + codigoQr));

        // Validación 2: verificar pago aprobado (solo acceden reservas HABILITADAS)
        if (!"HABILITADO".equals(ingreso.getEstado())) {
            String motivo = "INGRESADO".equals(ingreso.getEstado())
                    ? "Este código QR ya fue utilizado. No se permiten múltiples ingresos."
                    : "El acceso ha sido rechazado. Estado: " + ingreso.getEstado();
            ingreso.setEstado("RECHAZADO");
            ingreso.setMotivoRechazo(motivo);
            ingresoCommandRepository.save(ingreso);
            throw new IllegalStateException(motivo);
        }

        // Validación 3: confirmar fecha y horario del tour
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime ventanaInicio = ingreso.getFechaTour().minusMinutes(30);
        LocalDateTime ventanaFin = ingreso.getFechaTour().plusMinutes(15);
        if (ahora.isBefore(ventanaInicio) || ahora.isAfter(ventanaFin)) {
            String motivo = "Fuera del horario permitido de ingreso. El tour es a las "
                    + ingreso.getFechaTour();
            ingreso.setEstado("RECHAZADO");
            ingreso.setMotivoRechazo(motivo);
            ingresoCommandRepository.save(ingreso);
            throw new IllegalStateException(motivo);
        }

        // Registrar ingreso exitoso
        ingreso.setEstado("INGRESADO");
        ingreso.setMomentoIngreso(ahora);
        IngresoCommand ingresoGuardado = ingresoCommandRepository.save(ingreso);

        // Publicar IngresoRegistrado en Kafka
        IngresoEvent evento = new IngresoEvent("IngresoRegistrado", ingresoGuardado);
        kafkaTemplate.send(TOPIC_INGRESO, evento);
        log.info("Evento publicado: IngresoRegistrado para reservaId={} codigoQr={}",
                ingresoGuardado.getReservaId(), codigoQr);

        return ingresoGuardado;
    }

    /**
     * Escucha PagoConfirmado desde Kafka y genera el código QR de acceso.
     */
    @KafkaListener(
            topics = "pago-event-topic",
            groupId = "ingreso-command-group"
    )
    public void recibirPagoConfirmado(PagoEvent evento) {
        if (!"PagoConfirmado".equals(evento.getEventType())) return;

        PagoEvent.PagoData pago = evento.getPago();
        log.info("Recibido PagoConfirmado en IngresosCommandService: reservaId={}", pago.getReservaId());

        // Generar código QR único para el acceso
        String codigoQr = "QR-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        IngresoCommand nuevoIngreso = IngresoCommand.builder()
                .reservaId(pago.getReservaId())
                .tourId(pago.getTourId())
                .emailCliente(pago.getEmailCliente())
                .codigoQr(codigoQr)
                .estado("HABILITADO")
                .build();

        ingresoCommandRepository.save(nuevoIngreso);
        log.info("Acceso HABILITADO creado para reservaId={} codigoQr={}", pago.getReservaId(), codigoQr);
    }
}
