package com.aspiratest.imaginaryonlinegame.service;

import com.aspiratest.imaginaryonlinegame.model.core.GameCharacter;
import com.aspiratest.imaginaryonlinegame.repository.CharacterRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CharacterService {
  private final LevelConfigurationService levelConfigService;
  private final CharacterRepository characterRepository;

  public GameCharacter getCharacter(int id) {
    return characterRepository.computeIfAbsent(id, GameCharacter::defaultNew);
  }

  public GameCharacter addExperience(int id, int experienceAmount) {
    return characterRepository.compute(
        id,
        (characterId, character) -> calculateNewLevel(characterId, character, experienceAmount));
  }

  private GameCharacter calculateNewLevel(int id, GameCharacter character, int experienceAmount) {
    character = Optional.ofNullable(character).orElse(GameCharacter.defaultNew(id));
    int currentLevel = character.level();
    int currentExperience = character.experience() + experienceAmount;

    Optional<Integer> experienceRequired =
        levelConfigService.getExperienceRequiredForLevelUp(currentLevel);
    while (experienceRequired.isPresent() && currentExperience >= experienceRequired.get()) {
      currentExperience -= experienceRequired.get();
      currentLevel++;
      experienceRequired = levelConfigService.getExperienceRequiredForLevelUp(currentLevel);

      log.info("Character {} leveled up to {}", id, currentLevel);
    }

    return character.withLevelAndExp(currentLevel, currentExperience);
  }
}
