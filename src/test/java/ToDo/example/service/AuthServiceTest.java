package ToDo.example.service;

import ToDo.example.DTO.UserDto;
import ToDo.example.ToDoHousework.ToDoHouseworkApplication;
import ToDo.example.domain.Users;
import ToDo.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest(classes = ToDoHouseworkApplication.class)
@Transactional
public class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    private UserDto testUserDto;
    private Users testUser;

    @BeforeEach
    public void setUp() {
        testUserDto = UserDto.builder()
                .username("testuser")
                .password("testpass")
                .confirmPassword("testpass")
                .email("testuser@example.com")
                .build();

        testUser = Users.builder()
                .username("testuser")
                .password("hashedpassword")
                .email("testuser@example.com")
                .build();
    }

    @Test
    @Rollback(false)
    public void testRegister_Success() {
        //when
        authService.register(testUserDto);

        //then
        Users savedUser = userRepository.findByUsername(testUserDto.getUsername())
                .orElseThrow(() -> new AssertionError("사용자를 찾을 수 없습니다."));

        assertNotNull(savedUser);
        assertEquals(testUserDto.getUsername(), savedUser.getUsername());
        assertEquals(testUserDto.getEmail(), savedUser.getEmail());
        assertTrue(passwordEncoder.matches(testUserDto.getPassword(), savedUser.getPassword()));
    }


}
