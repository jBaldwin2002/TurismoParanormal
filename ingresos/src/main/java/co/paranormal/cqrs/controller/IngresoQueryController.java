package co.paranormal.cqrs.controller;

import co.paranormal.cqrs.persistence.entity.IngresoQuery;
import co.paranormal.cqrs.service.IngresoQueryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * QUERY CONTROLLER — endpoints de lectura para ingresos.
 * Puerto 8635
 */
@RestController
@RequestMapping("/api/ingresos/queries")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class IngresoQueryController {

    private final IngresoQueryService ingresoQueryService;

    /**
     * ConsultarIngresoQuery: lista todos los registros de ingreso desde MongoDB.
     */
    @GetMapping("/")
    public List<IngresoQuery> obtenerTodos() {
        return ingresoQueryService.obtenerTodos();
    }

    @GetMapping("/qr/{codigoQr}")
    public ResponseEntity<IngresoQuery> obtenerPorQr(@PathVariable String codigoQr) {
        return ingresoQueryService.obtenerPorQr(codigoQr)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tour/{tourId}")
    public List<IngresoQuery> obtenerPorTour(@PathVariable String tourId) {
        return ingresoQueryService.obtenerPorTour(tourId);
    }

    @GetMapping("/estado/{estado}")
    public List<IngresoQuery> obtenerPorEstado(@PathVariable String estado) {
        return ingresoQueryService.obtenerPorEstado(estado);
    }

    @GetMapping("/cliente/{email}")
    public List<IngresoQuery> obtenerPorCliente(@PathVariable String email) {
        return ingresoQueryService.obtenerPorCliente(email);
    }
}
