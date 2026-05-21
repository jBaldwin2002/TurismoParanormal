package co.paranormal.cqrs.controller;

import co.paranormal.cqrs.persistence.entity.IngresoCommand;
import co.paranormal.cqrs.service.IngresoCommandService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * COMMAND CONTROLLER — endpoints de escritura para ingresos.
 * Puerto 8634
 */
@RestController
@RequestMapping("/api/ingresos/commands")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class IngresoCommandController {

    private final IngresoCommandService ingresoCommandService;

    /**
     * RegistrarIngresoCommand: valida QR y registra el ingreso al tour.
     * Publica IngresoRegistrado en Kafka.
     *
     * @param codigoQr código QR escaneado en la entrada del tour
     */
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarIngreso(@RequestParam String codigoQr) {
        try {
            IngresoCommand ingreso = ingresoCommandService.registrarIngreso(codigoQr);
            return ResponseEntity.ok(ingreso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}
