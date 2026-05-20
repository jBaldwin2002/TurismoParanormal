package co.vinni.cqrs.consumer;

import co.vinni.cqrs.dto.ReservaDTO;
import co.vinni.cqrs.model.ReservaDocument;
import co.vinni.cqrs.repository.ReservaQueryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservaConsumer {

    private static final Logger log = LogManager.getLogger(ReservaConsumer.class);

    private final ReservaQueryRepository repository;

    public ReservaConsumer(ReservaQueryRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "reservas-topic", groupId = "turismo-query-group")
    public void consumirReservaCreada(ReservaDTO dto) {
        log.info("Evento ReservaCreada consumido -> reservaId={} usuario={}", dto.getId(), dto.getUsuario());

        ReservaDocument doc = ReservaDocument.builder()
                .reservaId(dto.getId())
                .usuario(dto.getUsuario())
                .tourId(dto.getTourId())
                .nombreTour(dto.getNombreTour())
                .fecha(dto.getFecha())
                .horario(dto.getHorario())
                .personas(dto.getPersonas())
                .valorTotal(dto.getValorTotal())
                .estado(dto.getEstado())
                .fechaCreacion(dto.getFechaCreacion())
                .fechaRegistroQuery(LocalDateTime.now())
                .build();

        repository.save(doc);
        log.info("ReservaDocument persistido en MongoDB para reservaId={}", dto.getId());
    }
}
