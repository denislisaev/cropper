package com.dlisaev.cropper.auth;

import com.dlisaev.cropper.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignInRequestsTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void signInShouldReturnToken() throws Exception {

        String url = "http://localhost:" + port + "/api/auth/signin";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject userObject = new JSONObject();
        userObject.put("username", "test@mail.ru");
        userObject.put("password", "test123");

        HttpEntity<String> request =
                new HttpEntity<String>(userObject.toString(), headers);

        String signupResultAsJsonStr = this.restTemplate.postForObject(url, request, String.class);

        JsonNode root = objectMapper.readTree(signupResultAsJsonStr);

        assertNotNull(signupResultAsJsonStr);
        assertNotNull(root);
        assertThat(root.path("success").asText()).contains("true");
        assertThat(root.path("token").asText()).contains("Bearer");

    }
}
