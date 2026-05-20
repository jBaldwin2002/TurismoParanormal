package co.vinni.cqrs.service;

import co.vinni.cqrs.command.CrearReservaCommand;
import co.vinni.cqrs.dto.ReservaDTO;
import co.vinni.cqrs.entity.Reserva;
import co.vinni.cqrs.kafka.ReservaProducer;
import co.vinni.cqrs.repository.ReservaRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ReservaCommandService {

    private static final Logger log = LogManager.getLogger(ReservaCommandService.class);

    private final ReservaRepository reservaRepository;
    private final ReservaProducer reservaProducer;

    public ReservaCommandService(ReservaRepository reservaRepository, ReservaProducer reservaProducer) {
        this.reservaRepository = reservaRepository;
        this.reservaProducer = reservaProducer;
    }

    @Transactional
    public ReservaDTO crearReserva(CrearReservaCommand cmd) {
        if (cmd.getFecha() == null || !cmd.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del tour debe ser futura");
        }
        if (cmd.getPersonas() == null || cmd.getPersonas() <= 0) {
            throw new IllegalArgumentException("La cantidad de personas debe ser mayor a cero");
        }
        if (reservaRepository.existsByUsuarioAndTourIdAndFecha(cmd.getUsuario(), cmd.getTourId(), cmd.getFecha())) {
            throw new IllegalStateException("Ya existe una reserva para este usuario en el mismo tour y fecha");
        }

        Reserva reserva = Reserva.builder()
                .usuario(cmd.getUsuario())
                .tourId(cmd.getTourId())
                .nombreTour(cmd.getNombreTour())
                .fecha(cmd.getFecha())
                .horario(cmd.getHorario())
                .personas(cmd.getPersonas())
                .valorTotal(cmd.getValorTotal())
                .estado("PENDIENTE")
                .fechaCreacion(LocalDateTime.now())
                .build();

        reserva = reservaRepository.save(reserva);
        log.info("Reserva guardada id={} usuario={}", reserva.getId(), reserva.getUsuario());

        ReservaDTO dto = toDTO(reserva);
        reservaProducer.publicarReservaCreada(dto);
        return dto;
    }

    private ReservaDTO toDTO(Reserva r) {
        return ReservaDTO.builder()
                .id(r.getId()).usuario(r.getUsuario()).tourId(r.getTourId())
                .nombreTour(r.getNombreTour()).fecha(r.getFecha()).horario(r.getHorario())
                .personas(r.getPersonas()).valorTotal(r.getValorTotal())
                .estado(r.getEstado()).fechaCreacion(r.getFechaCreacion())
                .build();
    }
}
