package com.aspiratest.imaginaryonlinegame.controller.v1;

import com.aspiratest.imaginaryonlinegame.dto.AddExperienceRequest;
import com.aspiratest.imaginaryonlinegame.model.core.GameCharacter;
import com.aspiratest.imaginaryonlinegame.service.CharacterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
@Validated
public class CharactersController {
  private static final String CHARACTER_ID_LENGTH_MESSAGE = "Character ID length must be >= 1";

  private final CharacterService characterService;

  @GetMapping("/{id}")
  @Operation(
      summary = "Get character by ID",
      description = "Retrieves a character or creates a new one if not exists")
  @ApiResponse(responseCode = "200", description = "Character found or created")
  @ApiResponse(responseCode = "400", description = "Invalid character ID")
  public ResponseEntity<GameCharacter> getCharacter(
      @PathVariable @Min(1) @Parameter(description = CHARACTER_ID_LENGTH_MESSAGE) int id) {
    return ResponseEntity.ok(characterService.getCharacter(id));
  }

  @PostMapping("/{id}/experience")
  @Operation(
      summary = "Add experience to character",
      description = "Adds experience points and handles leveling up")
  @ApiResponse(responseCode = "200", description = "Experience added successfully")
  @ApiResponse(responseCode = "400", description = "Invalid character ID or experience amount")
  public ResponseEntity<GameCharacter> addExperience(
      @PathVariable @Min(1) @Parameter(description = CHARACTER_ID_LENGTH_MESSAGE) int id,
      @RequestBody @Valid AddExperienceRequest requestBody) {
    return ResponseEntity.ok(characterService.addExperience(id, requestBody.getAmount()));
  }
}
