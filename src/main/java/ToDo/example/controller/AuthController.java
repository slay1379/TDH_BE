package ToDo.example.controller;

import ToDo.example.DTO.UserDto;
import ToDo.example.authentication.JwtUtil;
import ToDo.example.authentication.TokenExtractor;
import ToDo.example.service.AuthService;
import ToDo.example.service.TokenBlacklistService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserDto userDto) {
        authService.register(userDto);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AuthResponse("회원가입에 성공했습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserDto userDto) {
        String accessToken = authService.login(userDto);
        String refreshToken = jwtUtil.generateRefreshToken(userDto.getUsername());
        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        String jwt = TokenExtractor.extract(refreshToken);
        if (jwtUtil.validateToken(jwt)) {
            String username = jwtUtil.extractUsername(jwt);
            String newAccessToken = jwtUtil.generateAccessToken(username);
            return ResponseEntity.ok(new AuthResponse(newAccessToken, jwt));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Refresh 토큰이 유효하지 않습니다."));
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(@RequestHeader("Authorization") String token) {
        String jwt = TokenExtractor.extract(token);
        tokenBlacklistService.addToBlacklist(jwt, jwtUtil.getExpirationFromToken(jwt));
        return ResponseEntity.ok(new AuthResponse("로그아웃 되었습니다."));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class AuthResponse {
        private String message;
        private String accessToken;
        private String refreshToken;

        public AuthResponse(String message) {
            this.message = message;
        }

        public AuthResponse(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }
}
