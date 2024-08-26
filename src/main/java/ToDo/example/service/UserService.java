package ToDo.example.service;

import ToDo.example.DTO.UserDto;
import ToDo.example.authentication.JwtUtil;
import ToDo.example.domain.User;
import ToDo.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    //회원정보수정
    public User updateUser(Long userId, String newName, String newEmail, String newPassword, String token) {
        if (jwtUtil.isTokenExpired(token)) {
            throw new IllegalStateException("토큰이 만료되었습니다. 다시 로그인 해주세요.");
        }

        String tokenName = jwtUtil.extractUsername(token);

        Optional<User> optionalUser = userRepository.findByEmail(newEmail);

        User user = optionalUser.orElseThrow(() -> new IllegalStateException("유효하지 않는 사용자입니다."));


        if (!user.getUsername().equals(tokenName)) {
            throw new IllegalStateException("다른 사용자의 정보를 수정할 수 없습니다.");
        }

        if (newName != null && !newName.isEmpty() && !newName.equals(user.getUsername())) {
            user.updateUserName(newName);
        }

        if (newEmail != null && !newEmail.isEmpty() && !newEmail.equals(user.getEmail())) {
            user.updateEmail(newEmail);
        }

        if (newPassword != null && !newPassword.isEmpty()) {
            String encodePassword = passwordEncoder.encode(newPassword);
            user.updatePassword(encodePassword);
        }

        return user;
    }

    //비밀번호 재설정 링크 이메일로 보내기
    public void sendPasswordResetLink(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.orElseThrow(() -> new IllegalStateException("유효하지 않는 사용자입니다."));

        if (user != null) {
            String token = jwtUtil.generatePasswordResetToken(user.getEmail());
            String resetLink = "http:나의도메인.com/reset-password?token=" + token;

        }
    }

    //비밀번호 재설정
    public void updatePassword(String email, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.orElseThrow(() -> new IllegalStateException("유효하지 않는 사용자입니다."));

        if (user != null) {
            user.updatePassword(new BCryptPasswordEncoder().encode(newPassword));
            userRepository.save(user);
        }
    }

}
