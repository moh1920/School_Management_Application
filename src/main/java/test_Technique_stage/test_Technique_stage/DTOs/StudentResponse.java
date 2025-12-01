package test_Technique_stage.test_Technique_stage.DTOs;

import jakarta.persistence.*;
import test_Technique_stage.test_Technique_stage.entity.Level;

public record StudentResponse(
         Long id,
         String username ,
         Level level
) {


}
