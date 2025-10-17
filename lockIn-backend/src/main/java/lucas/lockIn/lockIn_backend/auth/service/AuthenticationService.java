package lucas.lockIn.lockIn_backend.auth.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.dto.request.LoginRequest;
import lucas.lockIn.lockIn_backend.auth.dto.request.RegisterRequest;
import lucas.lockIn.lockIn_backend.auth.dto.response.TokenResponse;
import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import lucas.lockIn.lockIn_backend.auth.exceptions.InvalidCredentialsException;
import lucas.lockIn.lockIn_backend.auth.exceptions.InvalidRefreshToken;
import lucas.lockIn.lockIn_backend.workout.exceptions.EntityNotFoundException;
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

    public TokenResponse loginUser(@Valid @NotNull LoginRequest loginRequest) {
        try {
            User user = userService.verifyLogIn(loginRequest);
            String token = jwtService.generateToken(user.getUsername(), user.getId());

            return new TokenResponse(token, new UserPrincipal(user));
        } catch (EntityNotFoundException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    public TokenResponse requestAccessToken(@NotNull String refreshCookie) {
        boolean validated = jwtService.validateToken(refreshCookie);
        final String token;

        if(validated){
            String username = jwtService.extractUsername(refreshCookie);
            Long userId =  jwtService.extractUserId(refreshCookie);

            token = jwtService.generateToken(username, userId);
            return new TokenResponse(token, new UserPrincipal(userId, username));
        }
        throw new InvalidRefreshToken("Refresh token is invalid! Please login again");
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
