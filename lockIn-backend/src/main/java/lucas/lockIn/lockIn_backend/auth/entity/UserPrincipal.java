package lucas.lockIn.lockIn_backend.auth.entity;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;

@Setter
@Getter
public class UserPrincipal implements Principal {
    private Long userId;
    private String username;

    public UserPrincipal(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }
}
