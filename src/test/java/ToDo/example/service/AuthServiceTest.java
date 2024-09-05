package ToDo.example.service;

import ToDo.example.DTO.UserDto;
import ToDo.example.ToDoHousework.ToDoHouseworkApplication;
import ToDo.example.domain.User;
import ToDo.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ToDoHouseworkApplication.class)
@Transactional
@Commit
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegister_Success() {
        UserDto userDto = UserDto.builder()
                .username("slay1379")
                .password("password")
                .confirmPassword("password")
                .email("palgalow@gmail.com")
                .build();

        when(userRepository.existsByUsername("slay1379")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        authService.register(userDto);

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    public void testRegister_Failure_UsernameAlreadyExists() {
        UserDto userDto = UserDto.builder()
                .username("existingUser")
                .password("password")
                .confirmPassword("password")
                .email("palgalow@gmail.com")
                .build();

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            authService.register(userDto);
        });

        assertEquals("이미 존재하는 아이디입니다.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testRegister_Failure_PasswordsDoNotMatch() {
        UserDto userDto = UserDto.builder()
                .username("slay1379")
                .password("password")
                .confirmPassword("differentPassword")
                .email("palgalow@gmail.com")
                .build();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            authService.register(userDto);
        });

        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}