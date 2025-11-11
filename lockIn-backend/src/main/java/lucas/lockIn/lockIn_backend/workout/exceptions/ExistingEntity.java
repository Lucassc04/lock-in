package lucas.lockIn.lockIn_backend.workout.exceptions;

import jakarta.validation.constraints.NotBlank;

public class ExistingEntity extends RuntimeException {
    public ExistingEntity(@NotBlank String s) {
        super(s);
    }
}
