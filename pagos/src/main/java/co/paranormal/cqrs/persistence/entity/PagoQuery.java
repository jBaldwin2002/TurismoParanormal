package co.paranormal.cqrs.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad lado QUERY — persiste en MongoDB.
 * Vista de lectura optimizada para consultas de pagos.
 */
@Document(collection = "pagos_query")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagoQuery {

    @Id
    private String id;

    private Long reservaId;
    private String tourId;
    private String emailCliente;
    private BigDecimal montoTotal;
    private String metodoPago;
    private String estado;
    private LocalDateTime fechaPago;
    private String comprobante;
}
