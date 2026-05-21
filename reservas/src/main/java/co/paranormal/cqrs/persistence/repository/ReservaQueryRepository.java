package co.paranormal.cqrs.persistence.repository;

import co.paranormal.cqrs.persistence.entity.ReservaQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReservaQueryRepository extends MongoRepository<ReservaQuery, String> {

    List<ReservaQuery> findByEmailCliente(String emailCliente);

    List<ReservaQuery> findByTourId(String tourId);

    List<ReservaQuery> findByEstado(String estado);
}
