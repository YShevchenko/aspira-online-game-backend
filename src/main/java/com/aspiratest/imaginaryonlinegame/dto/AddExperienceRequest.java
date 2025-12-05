package com.aspiratest.imaginaryonlinegame.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AddExperienceRequest {
  @Min(value = 1, message = "Experience amount must be positive")
  private int amount;
}
