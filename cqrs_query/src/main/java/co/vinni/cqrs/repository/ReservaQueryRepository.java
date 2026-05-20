package co.vinni.cqrs.repository;

import co.vinni.cqrs.model.ReservaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaQueryRepository extends MongoRepository<ReservaDocument, String> {
    List<ReservaDocument> findByUsuario(String usuario);
    List<ReservaDocument> findByTourId(String tourId);
    List<ReservaDocument> findByEstado(String estado);
}
