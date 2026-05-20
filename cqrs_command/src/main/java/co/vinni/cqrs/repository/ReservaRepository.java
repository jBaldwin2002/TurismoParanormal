package co.vinni.cqrs.repository;

import co.vinni.cqrs.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByUsuarioAndTourIdAndFecha(String usuario, String tourId, LocalDate fecha);
}
