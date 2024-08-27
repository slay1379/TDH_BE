package ToDo.example.controller;

import ToDo.example.DTO.UserDto;
import ToDo.example.authentication.JwtUtil;
import ToDo.example.service.AuthService;
import ToDo.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto) {
        authService.register(userDto);
        return ResponseEntity.ok("회원가입에 성공했습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserDto userDto) {
        String token = authService.login(userDto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        if (jwtUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh 토큰이 만료되었습니다.");
        }
        String token = jwtUtil.generateAccessToken(jwtUtil.extractUsername(refreshToken));
        return ResponseEntity.ok(token);
    }
}
