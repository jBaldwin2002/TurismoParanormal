package co.paranormal.cqrs.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad del lado COMMAND — se persiste en PostgreSQL.
 * Registra la reserva de un tour paranormal.
 */
@Entity
@Table(name = "reservas_command")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservaCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tourId;           // Identificador del tour paranormal
    private String nombreCliente;
    private String emailCliente;
    private int cantidadPersonas;
    private LocalDateTime fechaTour;

    // Estado: PENDIENTE_PAGO, PAGADA, CANCELADA
    private String estado;
}
