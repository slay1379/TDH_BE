package ToDo.example.DTO;

import ToDo.example.domain.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class TaskDto {

    @NotBlank(message = "할 일 입력은 필수입니다.")
    private String taskName;

    @NotBlank(message = "카테고리를 선택해주세요.")
    private Category category;

    @Min(value = 1, message = "주기 입력은 필수입니다.")
    private int frequency;

    private String notes;
}
