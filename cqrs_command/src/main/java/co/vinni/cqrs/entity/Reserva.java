package co.vinni.cqrs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String usuario;

    @Column(name = "tour_id", nullable = false)
    private String tourId;

    @Column(name = "nombre_tour", nullable = false)
    private String nombreTour;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private String horario;

    @Column(nullable = false)
    private Integer personas;

    @Column(nullable = false)
    private Double valorTotal;

    @Column(nullable = false)
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
