package co.paranormal.cqrs.controller;

import co.paranormal.cqrs.persistence.entity.PagoCommand;
import co.paranormal.cqrs.service.PagoCommandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * COMMAND CONTROLLER — endpoints de escritura para pagos.
 * Puerto 8632
 */
@RestController
@RequestMapping("/api/pagos/commands")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PagoCommandController {

    private final PagoCommandService pagoCommandService;

    /**
     * ProcesarPagoCommand: procesa el pago de una reserva.
     * Publica PagoConfirmado en Kafka.
     *
     * Body: { "metodoPago": "TARJETA_CREDITO" }
     */
    @PostMapping("/{reservaId}")
    public ResponseEntity<?> procesarPago(
            @PathVariable Long reservaId,
            @RequestParam String metodoPago) {
        try {
            PagoCommand pago = pagoCommandService.procesarPago(reservaId, metodoPago);
            return ResponseEntity.ok(pago);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
