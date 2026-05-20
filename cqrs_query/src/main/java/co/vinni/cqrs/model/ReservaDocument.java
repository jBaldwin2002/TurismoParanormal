package co.vinni.cqrs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "reservas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDocument {

    @Id
    private String id;

    private Long reservaId;
    private String usuario;
    private String tourId;
    private String nombreTour;
    private LocalDate fecha;
    private String horario;
    private Integer personas;
    private Double valorTotal;
    private String estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaRegistroQuery;
}
