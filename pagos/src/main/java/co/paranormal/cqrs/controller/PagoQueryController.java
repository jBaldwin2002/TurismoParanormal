package co.paranormal.cqrs.controller;

import co.paranormal.cqrs.persistence.entity.PagoQuery;
import co.paranormal.cqrs.service.PagoQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * QUERY CONTROLLER — endpoints de lectura para pagos.
 * Puerto 8633
 */
@RestController
@RequestMapping("/api/pagos/queries")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PagoQueryController {

    private final PagoQueryService pagoQueryService;

    /**
     * ConsultarPagoQuery: lista todos los pagos desde MongoDB.
     */
    @GetMapping("/")
    public List<PagoQuery> obtenerTodos() {
        return pagoQueryService.obtenerTodos();
    }

    @GetMapping("/reserva/{reservaId}")
    public ResponseEntity<PagoQuery> obtenerPorReserva(@PathVariable Long reservaId) {
        return pagoQueryService.obtenerPorReserva(reservaId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{email}")
    public List<PagoQuery> obtenerPorCliente(@PathVariable String email) {
        return pagoQueryService.obtenerPorCliente(email);
    }

    @GetMapping("/estado/{estado}")
    public List<PagoQuery> obtenerPorEstado(@PathVariable String estado) {
        return pagoQueryService.obtenerPorEstado(estado);
    }
}
