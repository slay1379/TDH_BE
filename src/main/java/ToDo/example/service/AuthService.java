package ToDo.example.service;

import ToDo.example.DTO.UserDto;
import ToDo.example.authentication.JwtUtil;
import ToDo.example.domain.User;
import ToDo.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //회원가입
    @Transactional
    public void register(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        if (userDto.getPassword().equals(userDto.getComfirmPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .build();

        userRepository.save(user);

    }

    //로그인
    public String login(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());

        User user = optionalUser.orElseThrow(() -> new IllegalStateException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new IllegalStateException("비밀번호가 틀립니다.");
        }

        return jwtUtil.generateAccessToken(user.getUsername());
    }

}
