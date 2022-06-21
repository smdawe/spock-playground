package io.github.smdawe.spock

import spock.lang.Specification
import spock.lang.Unroll

class PersonServiceSpec extends Specification {

  PersonRepository personRepository
  PersonService personService

  void setup() {
    // uses its own mock no mockito BS
    personRepository = Mock(PersonRepository)
    personService = new PersonService(personRepository)
  }

  void 'save a person'() {
    given:
      Person person = new Person()
      // when the save method is called with a person return the person
      // { person } is a groovy closure
      personRepository.save(person) >> { person }

    when:
      String result = personService.save(person)

    then: // just vaidate that the person is not null
      result
  }

  void 'get a person - invalid id'() {
    when:
      personService.get('blah')

    then: // call the thrown method with the exception we expect
      // in groovy the last parameter to a method does not need to be in brackets
      // could also be thrown(PersonException) there is also no need for .class on the end, groovy hey!
      thrown PersonException
      // verify that the get method was never called, _ denotes any object
      0 * personRepository.get(_)
  }

  void 'save a person - throw exception'() {
    given:
      Person person = new Person()
      // rather than return an object we now have a closure that throws an exception
      personRepository.save(person) >> { throw new RuntimeException() }

    when:
      personService.save(person)

    then:
      thrown Exception
  }

  // unroll lets us report each invocation as a separate result
  @Unroll("#id results in #result")
  void 'valid person id'() {
    expect:
      PersonService.validateId(id) == result

    where: // the arguments to the above test
      id        | result
      'P1234'   | true
      'P2134'   | true
      'P2351'   | true
      'P12345'  | false
      ''        | false
      '1234P'   | false
      'A1234'   | false
      null      | false

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
}
