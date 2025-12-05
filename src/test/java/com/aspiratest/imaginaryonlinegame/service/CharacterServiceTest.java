package com.aspiratest.imaginaryonlinegame.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.aspiratest.imaginaryonlinegame.model.core.GameCharacter;
import com.aspiratest.imaginaryonlinegame.repository.CharacterRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class CharacterServiceTest {

  @Autowired CharacterService characterService;
  @Autowired CharacterRepository characterRepository;
  @MockitoBean LevelConfigurationService levelConfigService;

  @BeforeEach
  void setUp() {
    characterRepository.dropAll();
  }

  @Test
  void testGetNewCharacter() {
    // given
    var character = characterService.getCharacter(1);

    // then
    assertEquals(1, character.id());
    assertEquals(1, character.level());
    assertEquals(0, character.experience());
  }

  @Test
  void testAddExperienceNoLevelUp() {
    // given
    when(levelConfigService.getExperienceRequiredForLevelUp(1)).thenReturn(Optional.of(100));

    // when
    var character = characterService.addExperience(1, 50);

    // then
    assertEquals(1, character.level());
    assertEquals(50, character.experience());
  }

  @Test
  void testAddExperienceLevelUp() {
    // given
    when(levelConfigService.getExperienceRequiredForLevelUp(1)).thenReturn(Optional.of(100));
    when(levelConfigService.getExperienceRequiredForLevelUp(2)).thenReturn(Optional.of(110));

    // when
    var character = characterService.addExperience(1, 120);

    // then
    assertEquals(2, character.level());
    assertEquals(20, character.experience());
  }

  @Test
  void testAddExperienceMultipleLevelUps() {
    // given
    when(levelConfigService.getExperienceRequiredForLevelUp(1)).thenReturn(Optional.of(100));
    when(levelConfigService.getExperienceRequiredForLevelUp(2)).thenReturn(Optional.of(110));
    when(levelConfigService.getExperienceRequiredForLevelUp(3)).thenReturn(Optional.of(120));

    // when
    var character = characterService.addExperience(1, 220);

    // then
    assertEquals(3, character.level());
    assertEquals(10, character.experience());
  }

  @Test
  void testAddExperienceNegativeAmount() {
    assertThrows(IllegalArgumentException.class, () -> characterService.addExperience(1, -10));
  }

  @Test
  void testAddExperienceMaxLevelReached() {
    // given
    when(levelConfigService.getExperienceRequiredForLevelUp(1)).thenReturn(Optional.of(100));
    when(levelConfigService.getExperienceRequiredForLevelUp(2)).thenReturn(Optional.empty());

    // when
    GameCharacter character = characterService.addExperience(1, 200);

    // then
    assertEquals(2, character.level());
    assertEquals(100, character.experience());
  }
}
