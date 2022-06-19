package io.github.smdawe.spock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;


@SpringBootApplication
@RestController
@RequestMapping(value = "/person")
public class PersonController {

    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);

    public static void main(String[] args) {
        SpringApplication.run(PersonController.class, args);
    }

    @Autowired
    private PersonService personService;

    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<Void> save(@RequestBody Person person) {
        try {
            String id = personService.save(person);
            return ResponseEntity.created(new URI("/person/" + id)).build();
        } catch (Throwable t) {
            LOG.error(t.getLocalizedMessage(), t);
            return ResponseEntity.internalServerError().build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Person> get(@PathVariable String id) {
        try {
            Optional<Person> person = Optional.ofNullable(personService.get(id));
            return person.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } catch (Throwable t) {
            LOG.error(t.getLocalizedMessage(), t);
            return ResponseEntity.internalServerError().build();
        }
    }
}