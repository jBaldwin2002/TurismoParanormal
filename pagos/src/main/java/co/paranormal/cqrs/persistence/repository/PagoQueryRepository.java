package co.paranormal.cqrs.persistence.repository;

import co.paranormal.cqrs.persistence.entity.PagoQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PagoQueryRepository extends MongoRepository<PagoQuery, String> {

    Optional<PagoQuery> findByReservaId(Long reservaId);

    List<PagoQuery> findByEmailCliente(String emailCliente);

    List<PagoQuery> findByEstado(String estado);
}
