package jug.ua.json.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jug.ua.json.rest.domain.Person;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Oleg Tsal-Tsalko
 */
public class RestClient {

    @Test
    public void getPersonViaRestUsingRestTemplate() throws Exception {
        RestTemplate template = new RestTemplate();
        Person person = template.getForObject("http://localhost:8080/users/553749c43004e7e2736bbdba", Person.class);
        assertThat(person.getFirstName(), is("Oleg"));
    }

    @Test
    public void getPersonViaRestUsingHttpClient() throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://localhost:8080/users/553749c43004e7e2736bbdba");
        Person person = httpClient.execute(get,
                httpResponse ->
                new ObjectMapper().readValue(httpResponse.getEntity().getContent(), Person.class));
        assertThat(person.getFirstName(), is("Oleg"));
    }

    @Test
    public void useJsonAssertToValidateJsonResponseReceivedFromRESTEndpoint() throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://localhost:8080/users/search/findByLastName?name=Tsal-Tsalko");
        HttpResponse response = httpClient.execute(get);

        String json = IOUtils.toString(response.getEntity().getContent());
        String expectedJSON = IOUtils.toString(RestClient.class.getClassLoader().getResourceAsStream("findByLastNameResponse.json"));
        JSONAssert.assertEquals(expectedJSON, json, false);
    }

    @Test
    public void parseComplexJsonStructureResponseFromRestAPIUsingJackson() throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet("http://localhost:8080/users/search/findByLastName?name=Tsal-Tsalko");
        HttpResponse response = httpClient.execute(get);

        JsonNode node = new ObjectMapper().readTree(response.getEntity().getContent());
        assertThat(node.at("/_embedded/users/0/firstName").asText(), is("Oleg"));
    }

    @Test
    public void updatePersonInRepositoryViaRestUsingRestTemplate() throws Exception {
        RestTemplate template = new RestTemplate();
        template.put("http://localhost:8080/users/55374c8f30040235598a7a0d",
                new Person("Andrii", "Rodionov"));
    }

    @Test
    public void addPersonToRepositoryViaRestUsingRestTemplate() throws Exception {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8080/users");
        String json = new ObjectMapper().writeValueAsString(
                new Person("Lena", "Syrota"));
        post.setEntity(new StringEntity(json));
        post.setHeader("Content-Type", "application/json;charset=UTF-8");
        HttpResponse response = httpClient.execute(post);
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_CREATED));
    }
}
