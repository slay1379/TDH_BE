package ToDo.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @NotBlank(message = "아이디는 필수입력 입니다.")
    @Size(min = 8, max = 50, message = "아이디는 8자 이상 50자 이하로 설정해주세요.")
    private String username;

    @Email(message = "이메일은 필수입력 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입력 입니다.")
    @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void updateUserName(String newUsername) {
        this.username = newUsername;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateTime(LocalDateTime newUpdatedAt) {
        this.updatedAt = newUpdatedAt;
    }
}
