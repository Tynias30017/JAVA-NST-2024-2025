package org.example.projectmanagerapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerIntegrationTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        mvc.perform(get("/api/users/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenDeleteUserNotFound() throws Exception {
        mvc.perform(delete("/api/users/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenUpdateUserNotFound() throws Exception {
        User user = new User();
        user.setName("test");
        String json = mapper.writeValueAsString(user);
        mvc.perform(put("/api/users/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenCreateUserWithInvalidData() throws Exception {
        User user = new User(); // brak wymaganych pól
        String json = mapper.writeValueAsString(user);
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}

