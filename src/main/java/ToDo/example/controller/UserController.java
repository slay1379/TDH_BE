package ToDo.example.controller;

import ToDo.example.DTO.UserDto;
import ToDo.example.authentication.JwtUtil;
import ToDo.example.domain.User;
import ToDo.example.service.TokenBlacklistService;
import ToDo.example.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    //회원정보 수정
    @PutMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> updateUser(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody UserDto userDto) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        userService.updateUser(userDto, token);

        return ResponseEntity.ok("회원 정보가 성공적으로 업데이트되었습니다.");
    }

    //비밀번호 잃어버림
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        userService.sendPasswordResetLink(email);
        return ResponseEntity.ok("비밀번호 재설정 링크를 이메일로 보냈습니다.");
    }

    //비밀번호 재설정
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        String email = jwtUtil.extractEmail(token);

        if (email != null && !jwtUtil.isTokenExpired(token)) {
            userService.resetPassword(email, newPassword);
            return ResponseEntity.ok("비밀번호가 재설정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("토큰이 만료되었거나 유효하지 않습니다.");
        }
    }
}
