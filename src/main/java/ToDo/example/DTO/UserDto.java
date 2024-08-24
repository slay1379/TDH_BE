package ToDo.example.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDto {

    @NotBlank(message = "아이디는 필수입력 입니다.")
    @Size(min = 8, max = 50, message = "아이디는 8자 이상 50자 이하로 설정해주세요.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입력 입니다.")
    @Size(min = 6, message = "비밀번호는 6자 이상이어야 합니다.")
    private String password;

    @Email(message = "이메일은 필수입력 입니다.")
    @NotBlank(message = "이메일은 필수입력 입니다.")
    private String email;
}
