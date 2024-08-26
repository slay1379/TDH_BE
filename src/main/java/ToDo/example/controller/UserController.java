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
    @PutMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);

        User updateUser = userService.updateUser(id, name, email, password, token);
        return ResponseEntity.ok(updateUser);
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
        String email = jwtUtil.extractEmailFromToken(token);

        if (email != null && !jwtUtil.isTokenExpired(token)) {
            userService.updatePassword(email, newPassword);
            return ResponseEntity.ok("비밀번호가 재설정되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("토큰이 만료되었거나 유효하지 않습니다.");
        }
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtUtil.extractJwtFromRequest(request);
        if (token != null && jwtUtil.validateToken(token)) {
            long expiration = jwtUtil.getExpiration(token).getTime();
            tokenBlacklistService.addToBlacklist(token, expiration);
            return ResponseEntity.ok("로그아웃되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
    }
}
