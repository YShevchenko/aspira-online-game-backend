package com.aspiratest.imaginaryonlinegame.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerIntegrationIT {

  @Autowired private MockMvc mockMvc;

  @Test
  void testGetDefaultNewCharacter() throws Exception {
    mockMvc
        .perform(get("/api/v1/characters/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.level").value(1))
        .andExpect(jsonPath("$.experience").value(0));
  }

  @Test
  void testAddExperienceNewCharacter() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/characters/2/experience")
                .content("{\"amount\": 150}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.level").value(2))
        .andExpect(jsonPath("$.experience").value(50));
  }

  @Test
  void testAddExperienceNewCharacterAndGet() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/characters/3/experience")
                .content("{\"amount\": 250}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/api/v1/characters/3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.level").value(3))
        .andExpect(jsonPath("$.experience").value(50));
  }
}
