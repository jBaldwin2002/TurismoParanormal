package co.paranormal.cqrs.dto;

import co.paranormal.cqrs.persistence.entity.PagoCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Evento publicado en Kafka por el componente de Pagos.
 * Lo consume el Componente 3 (Control de Ingresos).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoEvent {
    private String eventType;   // PagoConfirmado | PagoRechazado
    private PagoCommand pago;
}
