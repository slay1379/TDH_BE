package ToDo.example.repository;

import ToDo.example.domain.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByTaskName(String taskName);

    List<Task> findByUserId(Long userId);

    List<Task> findByLastDate(LocalDate lastDate);


}
