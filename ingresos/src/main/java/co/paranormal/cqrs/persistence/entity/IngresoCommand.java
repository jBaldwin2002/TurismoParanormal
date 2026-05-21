package co.paranormal.cqrs.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad lado COMMAND — persiste en PostgreSQL.
 * Registra el acceso físico de un visitante al tour paranormal.
 */
@Entity
@Table(name = "ingresos_command")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngresoCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reservaId;
    private String tourId;
    private String emailCliente;
    private String codigoQr;           // Código QR único generado al confirmar pago
    private LocalDateTime fechaTour;
    private LocalDateTime momentoIngreso;
    private String estado;             // HABILITADO | INGRESADO | RECHAZADO
    private String motivoRechazo;      // Solo si estado = RECHAZADO
}
