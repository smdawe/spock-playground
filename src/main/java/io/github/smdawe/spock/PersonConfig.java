package io.github.smdawe.spock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.UUID;

@Configuration
public class PersonConfig {

    @Bean
    PersonFileRepository personFileRepository() {
        File dir = new File(UUID.randomUUID().toString());
        dir.mkdir();
        return new PersonFileRepository(dir);
    }
}
