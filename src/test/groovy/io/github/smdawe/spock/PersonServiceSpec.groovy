package io.github.smdawe.spock

import spock.lang.Specification
import spock.lang.Unroll

class PersonServiceSpec extends Specification {

  PersonRepository personRepository
  PersonService personService

  void setup() {
    personRepository = Mock(PersonRepository)
    personService = new PersonService(personRepository)
  }

  void 'save a person'() {
    given:
      Person person = new Person()
      personRepository.save(person) >> { new Person().tap {id = 'P1234'} }

    when:
      String result = personService.save(person)

    then:
      result
  }

  void 'save a person - throw exception'() {
    given:
      Person person = new Person()
      personRepository.save(person) >> { throw new RuntimeException() }

    when:
      personService.save(person)

    then:
      thrown Exception
  }

  @Unroll("#id results in #result")
  void 'valid person id'() {
    expect:
      PersonService.validateId(id) == result

    where:
      id        | result
      'P1234'   | true
      'P12345'  | false
      ''        | false
      '1234P'   | false
      'A1234'   | false
      'P2351'   | true
  }

  void 'generate id'() {
    when:
      String id = PersonService.generateId()

    then:
      id.length() == 5
      id.startsWith('P')
  }

  void "ensure id's are unique"() {
    expect:
      PersonService.generateId() != PersonService.generateId()
      PersonService.generateId() != PersonService.generateId()
      PersonService.generateId() != PersonService.generateId()
  }

  void 'blah'() {
    expect:
      [null, '', '11'].findAll().size()==1
  }
}
