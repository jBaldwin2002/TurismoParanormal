package co.paranormal.cqrs.dto;

import co.paranormal.cqrs.persistence.entity.IngresoCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Evento publicado por Control de Ingresos una vez registrado el acceso.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngresoEvent {
    private String eventType;   // IngresoRegistrado
    private IngresoCommand ingreso;
}
