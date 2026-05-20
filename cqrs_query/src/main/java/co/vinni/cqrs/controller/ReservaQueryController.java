package co.vinni.cqrs.controller;

import co.vinni.cqrs.model.ReservaDocument;
import co.vinni.cqrs.service.ReservaQueryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaQueryController {

    private static final Logger log = LogManager.getLogger(ReservaQueryController.class);
    private final ReservaQueryService service;

    public ReservaQueryController(ReservaQueryService service) {
        this.service = service;
    }

    @GetMapping
    public List<ReservaDocument> todas() {
        log.info("GET /reservas");
        return service.consultarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDocument> porId(@PathVariable String id) {
        log.info("GET /reservas/{}", id);
        return service.consultarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuario}")
    public List<ReservaDocument> porUsuario(@PathVariable String usuario) {
        log.info("GET /reservas/usuario/{}", usuario);
        return service.consultarPorUsuario(usuario);
    }

    @GetMapping("/estado/{estado}")
    public List<ReservaDocument> porEstado(@PathVariable String estado) {
        log.info("GET /reservas/estado/{}", estado);
        return service.consultarPorEstado(estado);
    }

    @GetMapping("/tour/{tourId}")
    public List<ReservaDocument> porTour(@PathVariable String tourId) {
        log.info("GET /reservas/tour/{}", tourId);
        return service.consultarPorTour(tourId);
    }
}
