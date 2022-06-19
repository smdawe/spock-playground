package io.github.smdawe.spock;

public interface PersonRepository {
    Person save(Person person);

    Person get(String id);
}
