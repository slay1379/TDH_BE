package ToDo.example.DTO;

import jakarta.validation.constraints.NotBlank;

public class CategoryDto {
    private Long id;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    private Long userId;
}
