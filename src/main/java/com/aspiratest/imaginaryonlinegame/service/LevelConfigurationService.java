package com.aspiratest.imaginaryonlinegame.service;

import com.aspiratest.imaginaryonlinegame.exception.config.InvalidConfigurationException;
import com.aspiratest.imaginaryonlinegame.model.levelconfig.LevelConfig;
import com.aspiratest.imaginaryonlinegame.model.levelconfig.LevelRange;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LevelConfigurationService {
  private final ObjectMapper mapper;

  @Value("classpath:levels-config.yaml")
  private Resource levelConfigResource;

  private final TreeMap<Integer, Integer> levelToRequiredExperienceMap = new TreeMap<>();

  public Optional<Integer> getExperienceRequiredForLevelUp(int level) {
    return Optional.ofNullable(levelToRequiredExperienceMap.ceilingEntry(level))
        .map(Map.Entry::getValue);
  }

  @PostConstruct
  public void init() {
    try {
      LevelConfig config =
          mapper.readValue(levelConfigResource.getInputStream(), LevelConfig.class);
      validateConfigNotEmpty(config);

      verifyFirstLevelExists(config.levels());
      var lastLevel = config.levels().removeLast();
      for (LevelRange range : config.levels()) {
        addRangeToConfigMap(range);
      }
      addLastLevelToConfigMap(lastLevel);
    } catch (IOException e) {
      throw new InvalidConfigurationException("Failed to load level configuration file", e);
    }
  }

  private void verifyFirstLevelExists(List<LevelRange> levels) {
    var firstLevelRange = levels.getFirst();
    var range = parseIntRange(firstLevelRange.range());

    if (range.getLeft() != 1) {
      throw new InvalidConfigurationException("Configuration should start from 1st level");
    }
  }

  private void validateConfigNotEmpty(LevelConfig config) {
    if (config == null || config.levels() == null || config.levels().isEmpty()) {
      throw new InvalidConfigurationException("Configuration cannot be empty");
    }
  }

  private void addRangeToConfigMap(LevelRange range) {
    if (range.experience() <= 0) {
      throw new InvalidConfigurationException("Experience could not be <=0");
    }

    Pair<Integer, Integer> intRange = parseIntRange(range.range());
    levelToRequiredExperienceMap.put(intRange.getLeft(), range.experience());
    levelToRequiredExperienceMap.put(intRange.getRight(), range.experience());
  }

  private void addLastLevelToConfigMap(LevelRange range) {
    try {
      var lastLevelStr = range.range().substring(0, range.range().indexOf("-"));
      var lastIntLevel = Integer.valueOf(lastLevelStr);
      levelToRequiredExperienceMap.put(lastIntLevel, null);
    } catch (NumberFormatException e) {
      throw new InvalidConfigurationException(
          "Last level should be integer value with no upper bound: " + range);
    }
  }

  Pair<Integer, Integer> parseIntRange(String strRange) {
    String[] rangeParts = strRange.split("-");
    if (rangeParts.length != 2) {
      throw new InvalidConfigurationException("Invalid range format: " + strRange);
    }
    int start, end;
    try {
      start = Integer.parseInt(rangeParts[0].trim());
      end = Integer.parseInt(rangeParts[1].trim());
    } catch (NumberFormatException e) {
      throw new InvalidConfigurationException("Levels must be integer numbers: " + strRange);
    }
    if (start > end) {
      throw new InvalidConfigurationException(
          "Invalid range: start must be less then end for " + strRange);
    }

    return Pair.of(start, end);
  }
}
