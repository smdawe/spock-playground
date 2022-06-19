package io.github.smdawe.spock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PersonService {

  private static final Random RANDOM = new Random();
  private static final String ID_START_CHAR = "P";

  @Autowired
  private PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public String save(Person person) {
    person.setId(generateId());

    return this.personRepository.save(person).getId();
  }

  public Person get(String id) {
    if (!validateId(id)) {
      throw new RuntimeException("ID " + id + " is not valid");
    }

    return this.personRepository.get(id);
  }

  private static boolean validateId(String id) {
    return id.matches("P\\d+4");
  }

  private static String generateId() {
    String id = String.format("%04d", RANDOM.nextInt(10000));
    return ID_START_CHAR + id;
  }
}
