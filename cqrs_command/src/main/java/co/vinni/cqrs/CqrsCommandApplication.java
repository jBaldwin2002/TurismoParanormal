package co.vinni.cqrs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CqrsCommandApplication {
    private static final Logger log = LogManager.getLogger(CqrsCommandApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CqrsCommandApplication.class, args);
        log.info("Turismo Paranormal - COMMAND service started");
    }
}
