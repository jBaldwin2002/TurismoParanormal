package co.vinni.cqrs.controller;

import co.vinni.cqrs.command.CrearReservaCommand;
import co.vinni.cqrs.dto.ReservaDTO;
import co.vinni.cqrs.service.ReservaCommandService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ReservaCommandController {

    private static final Logger log = LogManager.getLogger(ReservaCommandController.class);
    private final ReservaCommandService service;

    public ReservaCommandController(ReservaCommandService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody CrearReservaCommand command) {
        log.info("CrearReservaCommand -> usuario={} tour={}", command.getUsuario(), command.getTourId());
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.crearReserva(command));
        } catch (IllegalArgumentException | IllegalStateException ex) {
            log.warn("Validacion fallida: {}", ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
