package com.aspiratest.imaginaryonlinegame.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aspiratest.imaginaryonlinegame.model.core.GameCharacter;
import org.junit.jupiter.api.Test;

class CharacterCreationTest {

  @Test
  void testValid() {
    GameCharacter c = new GameCharacter(1, 1, 0);
    assertEquals(1, c.id());
    assertEquals(1, c.level());
    assertEquals(0, c.experience());
  }

  @Test
  void testInvalidId() {
    assertThrows(IllegalArgumentException.class, () -> new GameCharacter(0, 1, 0));
  }

  @Test
  void testInvalidLevel() {
    assertThrows(IllegalArgumentException.class, () -> new GameCharacter(1, 0, 0));
  }

  @Test
  void testInvalidExperience() {
    assertThrows(IllegalArgumentException.class, () -> new GameCharacter(1, 1, -1));
  }

  @Test
  void testUpdateWithLevelAndExp() {
    // given
    GameCharacter character = new GameCharacter(1, 1, 0);

    // when
    var updated = character.withLevelAndExp(2, 50);

    // then
    assertEquals(2, updated.level());
    assertEquals(50, updated.experience());
  }
}
