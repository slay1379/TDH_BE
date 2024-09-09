package ToDo.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @NotBlank(message = "할 일을 입력해주세요.")
    private String taskName;

    private int frequency;
    private LocalDate lastDate;
    private LocalDate nextDate;
    private boolean isCompleted;
    private String notes;

    @Builder
    public Task(String taskName, Category category, int frequency, String notes, User user) {
        this.taskName = taskName;
        this.category = category;
        this.frequency = frequency;
        this.isCompleted = false;
        this.lastDate = LocalDate.now();
        this.nextDate = this.lastDate.plusDays(frequency);
        this.notes = notes;
        this.user = user;
    }

    public void updateTaskName(String newTaskName) {
        this.taskName = newTaskName;
    }

    public void updateCategory(Category newCategory) {
        this.category = newCategory;
    }

    public void updateFrequency(int newFrequency) {
        this.frequency = newFrequency;
        this.nextDate = this.lastDate.plusDays(newFrequency);
    }

    public void updateLastDate(LocalDate newLastDate) {
        this.lastDate = newLastDate;
    }

    public void updateNotes(String newNotes) {
        this.notes = newNotes;
    }

    public void changeCompleted() {
        if (this.isCompleted == false) {
            this.isCompleted = true;
        } else {
            this.isCompleted = false;
        }
    }


}
