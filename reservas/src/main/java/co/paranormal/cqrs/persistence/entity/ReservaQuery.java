package co.paranormal.cqrs.persistence.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidad del lado QUERY — se persiste en MongoDB.
 * Proyección de lectura optimizada para consultas.
 */
@Document(collection = "reservas_query")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservaQuery {

    @Id
    private String id;           // Usamos el id de PostgreSQL como string

    private String tourId;
    private String nombreCliente;
    private String emailCliente;
    private int cantidadPersonas;
    private LocalDateTime fechaTour;
    private String estado;
}
