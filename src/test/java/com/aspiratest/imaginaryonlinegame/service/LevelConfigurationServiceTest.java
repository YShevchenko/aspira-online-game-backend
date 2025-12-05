package com.aspiratest.imaginaryonlinegame.service;

import static org.junit.jupiter.api.Assertions.*;

import com.aspiratest.imaginaryonlinegame.config.ObjectMapperConfig;
import com.aspiratest.imaginaryonlinegame.exception.config.InvalidConfigurationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest(classes = {ObjectMapperConfig.class})
class LevelConfigurationServiceTest {

  @Autowired ObjectMapper mapper;

  @Test
  void testValidConfig() {
    // given
    String configYaml =
        """
        levels:
          - range: "1-10"
            experience: 100
          - range: "11-20"
            experience: 200
          - range: "100-"
            experience: -1
        """;
    var levelConfigurationService = new LevelConfigurationService(mapper);
    mockConfig(levelConfigurationService, configYaml);

    // when
    levelConfigurationService.init();

    // then
    assertEquals(Optional.of(100), levelConfigurationService.getExperienceRequiredForLevelUp(1));
    assertEquals(Optional.of(200), levelConfigurationService.getExperienceRequiredForLevelUp(15));
  }

  @Test
  void testInitEmptyConfig() {
    // given
    String configYaml = "levels: []";
    var levelConfigurationService = new LevelConfigurationService(mapper);
    mockConfig(levelConfigurationService, configYaml);

    // then
    assertThrows(InvalidConfigurationException.class, levelConfigurationService::init);
  }

  @Test
  void testInitWithInvalidRange() {
    // given
    String configYaml =
        """
    levels:
      - range: "20-10"
        experience: 100
    """;
    var levelConfigurationService = new LevelConfigurationService(mapper);
    mockConfig(levelConfigurationService, configYaml);

    // then
    assertThrows(InvalidConfigurationException.class, levelConfigurationService::init);
  }

  @Test
  void testGetExpRequiredWithMaxLevel() {
    // given
    String configYaml =
        """
    levels:
      - range: "1-10"
        experience: 100
      - range: "11-"
        experience: 0
    """;
    var levelConfigurationService = new LevelConfigurationService(mapper);
    mockConfig(levelConfigurationService, configYaml);

    // when
    levelConfigurationService.init();

    // then
    assertEquals(Optional.of(100), levelConfigurationService.getExperienceRequiredForLevelUp(10));
    assertEquals(Optional.empty(), levelConfigurationService.getExperienceRequiredForLevelUp(12));
  }

  private void mockConfig(LevelConfigurationService levelConfigurationService, String configStr) {
    ReflectionTestUtils.setField(
        levelConfigurationService,
        "levelConfigResource",
        new ByteArrayResource(configStr.getBytes(StandardCharsets.UTF_8)));
  }
}
