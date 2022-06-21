package io.github.smdawe.spock

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Files

class PersonFileRepositorySpec extends Specification {
    // shared allows us to persist state of objects across tests
    // useful for clients, directories or anything we don't want to recreate each time
    @Shared
    File testDir

    PersonFileRepository personFileRepository

    // called at the start of the tests and only run once
    void setupSpec() {
        testDir = new File(System.currentTimeMillis().toString());
        testDir.mkdir();
    }

    // called at the end of the tests
    void cleanupSpec() {
        for (File file : Objects.requireNonNull(testDir.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
        testDir.deleteDir()
    }

    // called before each test
    void setup() {
        personFileRepository = new PersonFileRepository(testDir)
    }

    void 'save person'() {
        given: // tap lets us set properties on an and return the object at the end
            Person person = new Person().tap {id = UUID.randomUUID().toString() }

        when:
            personFileRepository.save(person)

        then:
            loadPerson(person.getId())
    }

    void 'overwrite person'() {
        given: // tap lets us set properties on an and return the object at the end
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
        given: // tap lets us set properties on an and return the object at the end
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

        then: // groovy truthy null can be tested as being false
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
