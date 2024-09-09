package ToDo.example.service;

import ToDo.example.DTO.UserDto;
import ToDo.example.authentication.JwtUtil;
import ToDo.example.domain.User;
import ToDo.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //회원가입
    public void register(UserDto userDto) {
        validateUserExists(userDto.getUsername());
        validatePasswordMatch(userDto.getPassword(), userDto.getConfirmPassword());

        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

        userRepository.save(user);
    }

    //로그인
    public String login(UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

        validatePassword(userDto.getPassword(), user.getPassword());

        return jwtUtil.generateAccessToken(user.getUsername());
    }

    //사용자 유효성 검사
    private void validateUserExists(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    //비밀번호 일치 검사
    private void validatePasswordMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }
    }

    //비밀번호 유효성 검사
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }
    }

}
