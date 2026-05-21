package co.paranormal.cqrs.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad lado COMMAND — persiste en PostgreSQL.
 * Registra el pago asociado a una reserva de tour paranormal.
 */
@Entity
@Table(name = "pagos_command")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagoCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reservaId;          // FK lógica a reservas_command
    private String tourId;
    private String emailCliente;
    private BigDecimal montoTotal;
    private String metodoPago;       // TARJETA_CREDITO | TARJETA_DEBITO | PSE | EFECTIVO
    private String estado;           // PENDIENTE | CONFIRMADO | RECHAZADO
    private LocalDateTime fechaPago;
    private String comprobante;      // Código único de comprobante
}
