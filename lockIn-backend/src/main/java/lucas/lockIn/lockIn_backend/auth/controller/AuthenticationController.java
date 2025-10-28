package lucas.lockIn.lockIn_backend.auth.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.dto.request.LoginRequest;
import lucas.lockIn.lockIn_backend.auth.dto.request.RegisterRequest;
import lucas.lockIn.lockIn_backend.auth.dto.response.TokenResponse;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import lucas.lockIn.lockIn_backend.auth.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest registerRequest){

        UserPrincipal userPrincipal = authenticationService.registerUser(registerRequest);
        return ResponseEntity.status(200)
                .body(String.format(
                        "You have been registered successfully! Please, log in.\nUsername: %s;\nUser Id: %s",
                        userPrincipal.getUsername(),
                        userPrincipal.getUserId()
                ));


    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest){
        TokenResponse tokenResponse = authenticationService.loginUser(loginRequest);

        ResponseCookie cookie = authenticationService.requestRefreshCookie(
                tokenResponse.userPrincipal());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(tokenResponse);

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name="REFRESH", required = false) String refreshToken) {

        if (refreshToken == null) {
            return ResponseEntity.status(401).body("Refresh token missing");
        }

        TokenResponse tokenResponse = authenticationService.requestAccessToken(refreshToken);

        return ResponseEntity.ok().body(tokenResponse);
    }
}
