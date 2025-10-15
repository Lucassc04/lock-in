package lucas.lockIn.lockIn_backend.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lucas.lockIn.lockIn_backend.auth.dto.LoginRequest;
import lucas.lockIn.lockIn_backend.auth.dto.RegisterRequest;
import lucas.lockIn.lockIn_backend.auth.entity.User;
import lucas.lockIn.lockIn_backend.auth.entity.UserPrincipal;
import lucas.lockIn.lockIn_backend.auth.service.AuthenticationService;
import lucas.lockIn.lockIn_backend.auth.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid @NotNull RegisterRequest registerRequest){

        UserPrincipal userPrincipal = authenticationService.registerUser(registerRequest);
        return ResponseEntity.status(200)
                .body("You have been registered successfully! Please, log in" + userPrincipal);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid @NotNull LoginRequest loginRequest, HttpServletResponse response){
        String jwtToken = authenticationService.loginUser(loginRequest);

        User user = userService.findByEmail(loginRequest.email());
        ResponseCookie cookie = authenticationService.requestRefreshCookie(
                new UserPrincipal(user));

        response.addHeader(HttpHeaders.COOKIE, cookie.toString());
        return ResponseEntity.ok().body(jwtToken);

    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name="REFRESH") String cookie){
        String jwtToken = authenticationService.requestAccessToken(cookie);

        return ResponseEntity.ok().body(jwtToken);
    }
}
