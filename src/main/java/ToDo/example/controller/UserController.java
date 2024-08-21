package ToDo.example.controller;

import ToDo.example.authentication.JwtUtil;
import ToDo.example.domain.User;
import ToDo.example.repository.UserRepository;
import ToDo.example.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PutMapping("/{id}")
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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        userService.sendPasswordResetLink(email);
        return ResponseEntity.ok("비밀번호 재설정 링크를 이메일로 보냈습니다.")
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        String email = jwtUtil.extractEmailFromToken(token);

        if (email != null && !jwtUtil.isTokenExpired(token)) {
            userService.updatePassword(email, newPassword);
            return ResponseEntity.ok("비밀번호가 재설정되었습니다.")
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("토큰이 만료되었거나 유효하지 않습니다.");
        }
    }
}
