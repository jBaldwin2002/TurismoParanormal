package co.paranormal.cqrs.persistence.repository;

import co.paranormal.cqrs.persistence.entity.IngresoCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngresoCommandRepository extends JpaRepository<IngresoCommand, Long> {

    Optional<IngresoCommand> findByCodigoQr(String codigoQr);

    Optional<IngresoCommand> findByReservaId(Long reservaId);

    // Verificar si ya ingresó con este código QR
    boolean existsByCodigoQrAndEstado(String codigoQr, String estado);
}
