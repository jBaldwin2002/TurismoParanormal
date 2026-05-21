package co.paranormal.cqrs.persistence.repository;

import co.paranormal.cqrs.persistence.entity.PagoCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagoCommandRepository extends JpaRepository<PagoCommand, Long> {

    // Detectar pagos duplicados: misma reserva ya confirmada
    boolean existsByReservaIdAndEstado(Long reservaId, String estado);

    Optional<PagoCommand> findByReservaId(Long reservaId);
}
