package lucas.lockIn.lockIn_backend.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(@NotNull String firstName, @NotNull String lastName, @NotNull String email, @Email String password) {
}
