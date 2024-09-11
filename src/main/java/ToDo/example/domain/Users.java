package ToDo.example.domain;

import ToDo.example.DTO.UserDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Users(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(UserDto userDto, BCryptPasswordEncoder passwordEncoder) {
        if (userDto.getUsername() != null) {
            this.username = userDto.getUsername();
        }
        if (userDto.getEmail() != null) {
            this.email = userDto.getEmail();
        }
        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new IllegalStateException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        if (userDto.getPassword() != null) {
            this.password = passwordEncoder.encode(userDto.getPassword());
        }
        this.updatedAt = LocalDateTime.now();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

}
