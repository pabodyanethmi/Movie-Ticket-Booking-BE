package lk.ijse.backend.controller;

import jakarta.validation.Valid;
import lk.ijse.backend.dto.AuthRequestDTO;
import lk.ijse.backend.dto.AuthResponseDTO;
import lk.ijse.backend.dto.RegisterRequestDTO;
import lk.ijse.backend.dto.UserDTO;
import lk.ijse.backend.service.AuthService;
import lk.ijse.backend.service.UserService;
import lk.ijse.backend.util.StandardResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService,
                          UserService userService,
                          AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    private ResponseCookie createJwtCookie(String token, long maxAgeSeconds) {
        return ResponseCookie.from("jwt_token", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofSeconds(maxAgeSeconds))
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<StandardResponse<AuthResponseDTO>> login(@Valid @RequestBody AuthRequestDTO authRequest) {
        logger.info("POST /api/v1/auth/login received for email: '{}'", authRequest.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getEmail() != null ? authRequest.getEmail().trim().toLowerCase() : "",
                            authRequest.getPassword()
                    )
            );
            logger.info("AuthenticationManager successfully authenticated email: '{}'", authentication.getName());
        } catch (Exception e) {
            logger.warn("AuthenticationManager failed for email '{}': {}", authRequest.getEmail(), e.getMessage());
        }

        AuthResponseDTO response = authService.login(authRequest);
        ResponseCookie cookie = createJwtCookie(response.getToken(), 24 * 60 * 60);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new StandardResponse<>(HttpStatus.OK.value(), "User authenticated successfully", response));
    }

    @PostMapping("/register")
    public ResponseEntity<StandardResponse<AuthResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        logger.info("POST /api/v1/auth/register received for email: '{}'", registerRequest.getEmail());
        AuthResponseDTO response = authService.register(registerRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StandardResponse<>(HttpStatus.CREATED.value(), "User registered successfully", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<StandardResponse<String>> logout() {
        logger.info("POST /api/v1/auth/logout requested");
        ResponseCookie clearCookie = createJwtCookie("", 0);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .body(new StandardResponse<>(HttpStatus.OK.value(), "Logged out successfully", null));
    }

    @GetMapping("/me")
    public ResponseEntity<StandardResponse<UserDTO>> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            logger.warn("GET /api/v1/auth/me called with null Authentication object");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        logger.info("GET /api/v1/auth/me requested by user: '{}'", authentication.getName());
        UserDTO user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(
                new StandardResponse<>(HttpStatus.OK.value(), "Current user profile retrieved", user)
        );
    }
}
