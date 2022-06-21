package io.github.smdawe.spock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PersonFileRepository implements PersonRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PersonFileRepository.class);

    ObjectMapper objectMapper = new ObjectMapper();

    private File baseDir;

    public PersonFileRepository(File baseDir) {
        this.baseDir = baseDir;
    }

    @Override
    public Person save(Person person) {
        try {
            File file = new File(baseDir, person.getId());
            Files.write(file.toPath(), objectMapper.writeValueAsBytes(person));
            return person;
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public Person get(String id) {
        try {
            byte[] json = Files.readAllBytes(new File(baseDir, id).toPath());
            return objectMapper.readValue(json, Person.class);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
}
