package ToDo.example.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Getter
public class CategoryDto {
    private Long id;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    private Long userId;
}
