package ToDo.example.controller;

import ToDo.example.DTO.UserDto;
import ToDo.example.service.AuthService;
import ToDo.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto) {
        authService.register(userDto);
        return ResponseEntity.ok("회원가입에 성공했습니다.");
    }
}
