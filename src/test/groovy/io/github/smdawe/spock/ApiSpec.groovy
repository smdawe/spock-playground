package io.github.smdawe.spock

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import java.net.http.HttpResponse

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ApiSpec extends Specification {

    @Autowired
    MockMvc mvc

    void 'get - person does not exist'() {
        expect:
            mvc.perform(MockMvcRequestBuilders.get('/person/P1234')).andExpect(status().isNotFound())
    }

    void 'save and get person'() {
        given:
            String name = 'brian'

        when:
            MvcResult post = mvc.perform(MockMvcRequestBuilders.post('/person')
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
                    .content(JsonOutput.toJson([name: name]))).andReturn()

        then:
            post.response.status == 201

        when:
            String location = post.response.getHeader('Location')
            MvcResult get = mvc.perform(MockMvcRequestBuilders.get(location)).andReturn()

        then:
            get.response.status == 200
            Map<String, ?> body = new JsonSlurper().parseText(get.response.contentAsString)
            body.name == name
    }
}