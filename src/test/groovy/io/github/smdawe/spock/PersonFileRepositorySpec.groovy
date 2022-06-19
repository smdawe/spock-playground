package io.github.smdawe.spock

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Files

class PersonFileRepositorySpec extends Specification {
    @Shared
    File testDir

    PersonFileRepository personFileRepository

    void setupSpec() {
        testDir = new File(System.currentTimeMillis().toString());
        testDir.mkdir();
    }


    void cleanupSpec() {
        for (File file : Objects.requireNonNull(testDir.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }

    void setup() {
        personFileRepository = new PersonFileRepository(testDir)
    }

    void 'save person'() {
        given:
            Person person = new Person().tap {id = UUID.randomUUID().toString() }

        when:
            personFileRepository.save(person)

        then:
            loadPerson(person.getId())
    }

    void 'overwrite person'() {
        given:
            Person person = new Person().tap {id = UUID.randomUUID().toString() }

        when:
            personFileRepository.save(person)

        and:
            person.setName('bob')
             personFileRepository.save(person)

        then:
            loadPerson(person.getId()).name == 'bob'
    }

    void 'load person'() {
        given:
            Person person = new Person().tap {id = UUID.randomUUID().toString() }
            savePerson(person)

        when:
            Person result = personFileRepository.get(person.getId())

        then:
            result.id == person.id
    }

    void 'load person - does not exist'() {
        when:
            Person result = personFileRepository.get('random')

        then:
            !result
    }

    private Person loadPerson(String id) {
        byte[] json = Files.readAllBytes(new File(testDir, id).toPath())
        return new ObjectMapper().readValue(json, Person.class)
    }

    private void savePerson(Person person) {
        File file = new File(testDir, person.getId())
        Files.write(file.toPath(), new ObjectMapper().writeValueAsBytes(person))
    }
}
