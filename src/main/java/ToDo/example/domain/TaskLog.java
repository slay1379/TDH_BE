package ToDo.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class TaskLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int logId;

    @OneToOne
    @JoinColumn(name = "task_id")
    private Task taskId;
    private Timestamp completedAt;
}
