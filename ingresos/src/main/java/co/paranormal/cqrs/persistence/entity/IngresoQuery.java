package co.paranormal.cqrs.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidad lado QUERY — persiste en MongoDB.
 * Vista de lectura para reportes de asistencia al tour.
 */
@Document(collection = "ingresos_query")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IngresoQuery {

    @Id
    private String id;

    private Long reservaId;
    private String tourId;
    private String emailCliente;
    private String codigoQr;
    private LocalDateTime fechaTour;
    private LocalDateTime momentoIngreso;
    private String estado;
    private String motivoRechazo;
}
