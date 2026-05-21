package co.paranormal.cqrs.dto;

import co.paranormal.cqrs.persistence.entity.ReservaCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Evento que viaja a través de Kafka entre Command y Query.
 * Representa una acción ocurrida sobre una Reserva.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaEvent {
    private String eventType;          // CrearReserva | CancelarReserva
    private ReservaCommand reserva;
}
