package co.paranormal.cqrs.persistence.repository;

import co.paranormal.cqrs.persistence.entity.IngresoQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IngresoQueryRepository extends MongoRepository<IngresoQuery, String> {

    Optional<IngresoQuery> findByCodigoQr(String codigoQr);

    List<IngresoQuery> findByTourId(String tourId);

    List<IngresoQuery> findByEstado(String estado);

    List<IngresoQuery> findByEmailCliente(String emailCliente);
}
