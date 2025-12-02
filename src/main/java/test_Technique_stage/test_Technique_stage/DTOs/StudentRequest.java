package test_Technique_stage.test_Technique_stage.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import test_Technique_stage.test_Technique_stage.entity.Level;

public record StudentRequest(
        Long id,
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
        String username,
        Level level
) {
}
