package co.vinni.cqrs.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearReservaCommand {
    private String usuario;
    private String tourId;
    private String nombreTour;
    private LocalDate fecha;
    private String horario;
    private Integer personas;
    private Double valorTotal;
}
