package co.paranormal.cqrs.persistence.repository;

import co.paranormal.cqrs.persistence.entity.ReservaCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaCommandRepository extends JpaRepository<ReservaCommand, Long> {

    // Validar reservas duplicadas: mismo cliente, tour y fecha
    boolean existsByEmailClienteAndTourIdAndFechaTour(
            String emailCliente, String tourId, LocalDateTime fechaTour);

    // Consultar cupos ocupados en un tour/fecha
    List<ReservaCommand> findByTourIdAndFechaTour(String tourId, LocalDateTime fechaTour);
}
