package ToDo.example.service;

import ToDo.example.DTO.UserDto;
import ToDo.example.ToDoHousework.ToDoHouseworkApplication;
import ToDo.example.domain.User;
import ToDo.example.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ToDoHouseworkApplication.class)
@ActiveProfiles("test")
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    private UserDto testUserDto;


    @BeforeEach
    void setup() {
        testUserDto = UserDto.builder()
                .username("testuser")
                .password("testpassword")
                .confirmPassword("testpassword")
                .email("test@example.com")
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    public void testRegister_Success() {

        authService.register(testUserDto);

        User savedUser = userRepository.findByUsername(testUserDto.getUsername())
                .orElseThrow(() -> new AssertionError("사용자를 찾을 수 없습니다."));

        assertNotNull(savedUser);
        assertEquals(testUserDto.getUsername(), savedUser.getUsername());
        assertEquals(testUserDto.getEmail(), savedUser.getEmail());
        assertNotEquals(testUserDto.getPassword(), savedUser.getPassword());
    }
}
