package test_Technique_stage.test_Technique_stage.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentException extends RuntimeException {

    private final String msg;
}
