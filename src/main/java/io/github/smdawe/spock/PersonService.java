package io.github.smdawe.spock;

import java.util.Random;

public class PersonService {


  private static final Random RANDOM = new Random();
  private static final String ID_START_CHAR = "P";

  private PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public String save(Person person) {
    return this.personRepository.save(person).getId();
  }

  public Person get(String id) {
    if (!validateId(id)) {
      throw new RuntimeException("ID " + id + " is not valid");
    }

    return this.personRepository.get(id);
  }

  // valid ids ust start with P followed by 4 digits



  private static String generateId() {
    String id = String.format("%04d", RANDOM.nextInt(10000));
    return ID_START_CHAR + id;
  }
}
