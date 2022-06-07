package io.github.smdawe.spock

import spock.lang.Specification

class PersonServiceSpec extends Specification {

  void 'validate person id'() {
    expect:
      PersonService.validateId('P0001')
      !PersonService.validateId('')
      !PersonService.validateId(null)

  }


  void "null value of"() {
    given:
      Object o = null

    when:
      String r = String.valueOf(o)

    then:
      r == 'null'
  }
}
