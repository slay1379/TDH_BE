package ToDo.example.DTO;

import ToDo.example.domain.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TaskDto {

    @NotBlank(message = "할 일을 입력해주세요.")
    private String taskName;

    @NotBlank(message = "카테고리를 선택해주세요.")
    private Category category;

    @NotBlank(message = "주기를 입력해주세요.")
    private int frequency;

    private String notes;
}
