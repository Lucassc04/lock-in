package lucas.lockIn.lockIn_backend.auth.dto.response;

import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;

public record TokenResponse(String accessToken, UserPrincipal userPrincipal) {
}
