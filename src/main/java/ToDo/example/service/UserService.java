package ToDo.example.service;

import ToDo.example.DTO.UserDto;
import ToDo.example.authentication.JwtUtil;
import ToDo.example.domain.Users;
import ToDo.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;


    //회원정보수정
    public void updateUser(UserDto userDto, String token) {
        validateToken(token);

        String username = jwtUtil.extractUsername(token);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않은 사용자입니다."));

        users.update(userDto, passwordEncoder);
    }

    //비밀번호 재설정 링크 이메일로 보내기
    public void sendPasswordResetLink(String email) {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않은 사용자입니다."));

        String token = jwtUtil.generatePasswordResetToken(users.getEmail());
        String resetLink = "http://나의도메인.com/reset-password?token=" + token;

        emailService.sendPasswordResetEmail(users.getEmail(), resetLink);
    }

    //비밀번호 재설정
    public void resetPassword(String token, String newPassword) {
        validateToken(token);

        String email = jwtUtil.extractEmail(token);
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않은 사용자입니다."));

        users.updatePassword(passwordEncoder.encode(newPassword));
        userRepository.save(users);
    }

    private void validateToken(String token) {
        if (jwtUtil.isTokenExpired(token)) {
            throw new IllegalStateException("토큰이 만료되었습니다. 다시 로그인 해주세요.");
        }
    }

}
