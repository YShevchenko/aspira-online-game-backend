package com.aspiratest.imaginaryonlinegame.model.core;

public record GameCharacter(int id, int level, int experience) {
  public GameCharacter {
    if (id <= 0) {
      throw new IllegalArgumentException("Character ID must be greater than zero.");
    }
    if (level < 1) {
      throw new IllegalArgumentException("Level must be at least 1.");
    }
    if (experience < 0) {
      throw new IllegalArgumentException("Experience cannot be negative.");
    }
  }

  public GameCharacter withLevelAndExp(int newLevel, int newExp) {
    return new GameCharacter(id, newLevel, newExp);
  }

  public static GameCharacter defaultNew(int id) {
    return new GameCharacter(id, 1, 0);
  }
}
