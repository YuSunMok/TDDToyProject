package sample.cafediosk.spring;

import jakarta.persistence.EntityListeners;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CafedioskApplication {

    public static void main(String[] args) {
        SpringApplication.run(CafedioskApplication.class, args);
    }

}
