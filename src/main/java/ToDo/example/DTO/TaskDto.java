package ToDo.example.DTO;

import ToDo.example.domain.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class TaskDto {

    @NotBlank(message = "할 일 입력은 필수입니다.", groups = {Create.class, Update.class})
    private String taskName;

    @NotBlank(message = "카테고리를 선택해주세요.", groups = {Create.class, Update.class})
    private Category category;

    @Min(value = 1, message = "주기 입력은 필수입니다.", groups = {Create.class, Update.class})
    private int frequency;

    private String notes;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotBlank(message = "마지막으로 할 일을 한 날짜를 선택해주세요.", groups = {Update.class})
    private LocalDate lastDate;

    public interface Create {}
    public interface Update {}
}
