package co.vinni.cqrs.service;

import co.vinni.cqrs.model.ReservaDocument;
import co.vinni.cqrs.repository.ReservaQueryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaQueryService {

    private static final Logger log = LogManager.getLogger(ReservaQueryService.class);

    private final ReservaQueryRepository repository;

    public ReservaQueryService(ReservaQueryRepository repository) {
        this.repository = repository;
    }

    // ConsultarReservaQuery — por id interno de MongoDB
    public Optional<ReservaDocument> consultarPorId(String id) {
        log.info("ConsultarReservaQuery id={}", id);
        return repository.findById(id);
    }

    // ConsultarReservaQuery — por usuario
    public List<ReservaDocument> consultarPorUsuario(String usuario) {
        log.info("ConsultarReservaQuery usuario={}", usuario);
        return repository.findByUsuario(usuario);
    }

    // ConsultarReservaQuery — todas las reservas
    public List<ReservaDocument> consultarTodas() {
        log.info("ConsultarReservaQuery - todas");
        return repository.findAll();
    }

    // ConsultarReservaQuery — por estado
    public List<ReservaDocument> consultarPorEstado(String estado) {
        log.info("ConsultarReservaQuery estado={}", estado);
        return repository.findByEstado(estado);
    }

    // ConsultarReservaQuery — por tour
    public List<ReservaDocument> consultarPorTour(String tourId) {
        log.info("ConsultarReservaQuery tourId={}", tourId);
        return repository.findByTourId(tourId);
    }
}
