package test_Technique_stage.test_Technique_stage.handle;

import java.util.Map;

public record ErrorResponse(
        Map<String, String> errors
) {

}