package ToDo.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long task_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    private String taskName;
    private int frequency;
    private LocalDateTime lastDate;
    private LocalDateTime nextDate;
    private boolean isCompleted;
    private String notes;

    public Task(String taskName, Category category, int frequency, String notes, User user) {
        this.taskName = taskName;
        this.category = category;
        this.frequency = frequency;
        this.isCompleted = false;
        this.lastDate = LocalDateTime.now();
        this.nextDate = this.lastDate.plusDays(frequency);
        this.notes = notes;
        this.user = user;
    }

}
