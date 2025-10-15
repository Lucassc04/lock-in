package lucas.lockIn.lockIn_backend.auth.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.dto.LoginRequest;
import lucas.lockIn.lockIn_backend.auth.dto.RegisterRequest;
import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@AllArgsConstructor
@Service
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;

    public UserPrincipal registerUser(@Valid @NotNull RegisterRequest registerRequest) {
        return userService.createUser(registerRequest);
    }

    public String loginUser(@Valid @NotNull LoginRequest loginRequest) {
        if(userService.verifyLogIn(loginRequest)) {
           User user =  userService.findByEmail(loginRequest.email());

           return jwtService.generateToken(user.getUsername(), user.getId());
        }
        return null;
    }

    public String requestAccessToken(@NotNull String refreshCookie) {
        boolean validated = jwtService.validateToken(refreshCookie);
        final String token;

        if(validated){
            String username = jwtService.extractUsername(refreshCookie);
            Long userId =  jwtService.extractUserId(refreshCookie);

            return jwtService.generateToken(username, userId);
        }

        return null;
    }

    public ResponseCookie requestRefreshCookie(@Valid @NotNull UserPrincipal userPrincipal) {
        String cookieToken = jwtService.generateCookie(userPrincipal.getUsername(), userPrincipal.getUserId());
        Long duration = fetchTokenDuration(cookieToken);

        //Create secure cookie
        return ResponseCookie.from("REFRESH", cookieToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(duration)
                .sameSite(SameSiteCookies.STRICT.toString())
                .build();
    }

    private Long fetchTokenDuration(String token) {
        Date start = jwtService.extractIssuance(token);
        Date end = jwtService.extractExpiration(token);

        return Duration.between(
                start.toInstant(),
                end.toInstant()
        ).getSeconds();
    }

}
