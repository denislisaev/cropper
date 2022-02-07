package com.dlisaev.cropper.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignUpRequestsTest {
    @LocalServerPort
    private int port;

    @Autowired
    UserService userService;
    @Autowired
    private TestRestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void signUpShouldReturnDefaultMessage() throws Exception {

        try {
            userService.deleteUserByUsername("testusername");
        } catch (Exception ignored){}

        String url = "http://localhost:" + port + "/api/auth/signup";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject userObject = new JSONObject();
        userObject.put("email", "testemail@mail.ru");
        userObject.put("firstname", "testfirstname");
        userObject.put("lastname", "testlastname");
        userObject.put("username", "testusername");
        userObject.put("location", "testlocation");
        userObject.put("password", "testpassword");
        userObject.put("confirmPassword", "testpassword");

        HttpEntity<String> request =
                new HttpEntity<String>(userObject.toString(), headers);

        String signupResultAsJsonStr = this.restTemplate.postForObject(url, request, String.class);

        JsonNode root = objectMapper.readTree(signupResultAsJsonStr);

        assertNotNull(signupResultAsJsonStr);
        assertNotNull(root);
        assertThat(root.path("message").asText()).contains("Регистрация успешно завершена");

        userService.deleteUserByUsername("testusername");
    }
}
