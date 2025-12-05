package com.aspiratest.imaginaryonlinegame.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerValidationIT {

  @Autowired private MockMvc mockMvc;

  @Test
  void testAddExperienceWithInvalidId() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/characters/0/experience")
                .content("{\"amount\": 100}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testAddExperienceWithInvalidAmount() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/characters/1/experience")
                .content("{\"amount\": 0}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testAddExperienceWithNegativeAmount() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/characters/1/experience")
                .content("{\"amount\": -10}")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
