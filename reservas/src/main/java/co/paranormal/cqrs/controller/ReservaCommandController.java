package co.paranormal.cqrs.controller;

import co.paranormal.cqrs.persistence.entity.ReservaCommand;
import co.paranormal.cqrs.service.ReservaCommandService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * COMMAND CONTROLLER — expone los endpoints de escritura.
 * Puerto 8630
 */
@RestController
@RequestMapping("/api/reservas/commands")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ReservaCommandController {

    private final ReservaCommandService reservaCommandService;

    /**
     * CrearReservaCommand: registra una nueva reserva de tour paranormal.
     * Publica evento ReservaCreada en Kafka.
     */
    @PostMapping("/")
    public ResponseEntity<?> crearReserva(@RequestBody ReservaCommand reserva) {
        try {
            ReservaCommand creada = reservaCommandService.crearReserva(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(creada);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Cancela una reserva existente.
     * Publica evento ReservaCancelada en Kafka.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable Long id) {
        try {
            ReservaCommand cancelada = reservaCommandService.cancelarReserva(id);
            return ResponseEntity.ok(cancelada);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
