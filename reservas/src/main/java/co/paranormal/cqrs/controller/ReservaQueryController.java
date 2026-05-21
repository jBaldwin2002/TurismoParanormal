package co.paranormal.cqrs.controller;

import co.paranormal.cqrs.persistence.entity.ReservaQuery;
import co.paranormal.cqrs.service.ReservaQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * QUERY CONTROLLER — expone los endpoints de lectura.
 * Puerto 8631
 */
@RestController
@RequestMapping("/api/reservas/queries")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ReservaQueryController {

    private final ReservaQueryService reservaQueryService;

    /**
     * ConsultarReservaQuery: obtiene todas las reservas desde MongoDB.
     */
    @GetMapping("/")
    public List<ReservaQuery> obtenerTodas() {
        return reservaQueryService.obtenerTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaQuery> obtenerPorId(@PathVariable String id) {
        try {
            return ResponseEntity.ok(reservaQueryService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/cliente/{email}")
    public List<ReservaQuery> obtenerPorCliente(@PathVariable String email) {
        return reservaQueryService.obtenerPorCliente(email);
    }

    @GetMapping("/tour/{tourId}")
    public List<ReservaQuery> obtenerPorTour(@PathVariable String tourId) {
        return reservaQueryService.obtenerPorTour(tourId);
    }
}
