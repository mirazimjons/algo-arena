package uz.thejaver.algoarena;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class AlgoArenaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlgoArenaApplication.class, args);
    }

}
