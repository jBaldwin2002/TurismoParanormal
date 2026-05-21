package co.paranormal.cqrs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Evento entrante desde el topic pago-event-topic.
 * Este componente sólo necesita saber que el pago fue confirmado.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoEvent {
    private String eventType;
    private PagoData pago;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PagoData {
        private Long id;
        private Long reservaId;
        private String tourId;
        private String emailCliente;
        private BigDecimal montoTotal;
        private String metodoPago;
        private String estado;
        private LocalDateTime fechaPago;
        private String comprobante;
    }
}
