package com.havrysh.edgesight.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record DetectionDto(@NotBlank String uniqueObjectId,
                           @NotBlank String objectType,
                           @NotNull Double confidence,
                           @NotNull LocalDateTime timestamp,
                           @NotBlank String source,
                           @NotBlank String location) {}