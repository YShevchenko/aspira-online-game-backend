package com.aspiratest.imaginaryonlinegame.repository;

import com.aspiratest.imaginaryonlinegame.model.core.GameCharacter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import org.springframework.stereotype.Repository;

@Repository
public class CharacterRepository {
  private final ConcurrentHashMap<Integer, GameCharacter> characters = new ConcurrentHashMap<>();

  public GameCharacter computeIfAbsent(int id, IntFunction<GameCharacter> creator) {
    return characters.computeIfAbsent(id, creator::apply);
  }

  public GameCharacter compute(int id, BiFunction<Integer, GameCharacter, GameCharacter> updater) {
    return characters.compute(id, updater);
  }

  public void dropAll() {
    characters.clear();
  }
}
