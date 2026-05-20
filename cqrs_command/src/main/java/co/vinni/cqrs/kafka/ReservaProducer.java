package co.vinni.cqrs.kafka;

import co.vinni.cqrs.dto.ReservaDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReservaProducer {

    private static final Logger log = LogManager.getLogger(ReservaProducer.class);
    private static final String TOPIC = "reservas-topic";

    private final KafkaTemplate<String, ReservaDTO> kafkaTemplate;

    public ReservaProducer(KafkaTemplate<String, ReservaDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarReservaCreada(ReservaDTO dto) {
        kafkaTemplate.send(TOPIC, dto.getTourId(), dto);
        log.info("Evento ReservaCreada publicado -> topic='{}' tourId={}", TOPIC, dto.getTourId());
    }
}
