package co.vinni.cqrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private Long id;
    private String usuario;
    private String tourId;
    private String nombreTour;
    private LocalDate fecha;
    private String horario;
    private Integer personas;
    private Double valorTotal;
    private String estado;
    private LocalDateTime fechaCreacion;
}
