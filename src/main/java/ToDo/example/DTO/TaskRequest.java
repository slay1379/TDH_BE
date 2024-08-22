package ToDo.example.DTO;

import ToDo.example.domain.Category;
import lombok.Getter;

@Getter
public class TaskRequest {
    private String taskName;
    private Category category;
    private int frequency;
    private String notes;
}
