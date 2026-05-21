package co.paranormal.cqrs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Evento entrante desde el topic reserva-event-topic.
 * Contiene los datos mínimos de la reserva que este componente necesita.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaEvent {
    private String eventType;
    private ReservaData reserva;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReservaData {
        private Long id;
        private String tourId;
        private String nombreCliente;
        private String emailCliente;
        private int cantidadPersonas;
        private LocalDateTime fechaTour;
        private String estado;
    }
}
