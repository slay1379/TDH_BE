package ToDo.example.service;

import ToDo.example.domain.Category;
import ToDo.example.domain.Task;
import ToDo.example.domain.User;
import ToDo.example.repository.CategoryRepository;
import ToDo.example.repository.TaskRepository;
import ToDo.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    //할 일 생성
    @Transactional
    public Task createTask(String taskName, Long userId, String categoryName, int frequency, String notes) {
        Optional<User> optionalUser = userRepository.findByUserId(userId);

        User user = optionalUser.orElseThrow(() -> new IllegalStateException("유효하지 않은 사용자입니다."));

        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);

        Category category = optionalCategory.orElseThrow(() -> new IllegalStateException("유효하지 않는 카테고리입니다."));

        Task task = new Task(taskName, category, frequency, notes, user);
        taskRepository.save(task);
        return task;
    }

    @Transactional
    public Task updateTask(Long taskId, String taskName, String categoryName, int frequency, String notes, LocalDate lastDate) {

        Optional<Task> optionalTask = taskRepository.findByTaskId(taskId);

        Task task = optionalTask.orElseThrow(() -> new IllegalStateException("유효하지 않은 할 일입니다."));

        if (taskName != null && !taskName.isEmpty() && !task.getTaskName().equals(taskName)){
            task.updateTaskName(taskName);
        }

        if (categoryName != null && !categoryName.isEmpty() && !task.getCategory().getCategoryName().equals(categoryName)) {
            Optional<Category> optionalCategory = categoryRepository.findByCategoryName(categoryName);
            Category category = optionalCategory.orElseThrow(() -> new IllegalStateException("유효하지 않은 카테고리입니다."));
            task.updateCategory(category);
        }

        if (frequency > 0 && task.getFrequency() != frequency) {
            task.updateFrequency(frequency);
        }

        if (notes != null && !task.getNotes().equals(notes)) {
            task.updateNotes(notes);
        }

        if (!task.getLastDate().equals(lastDate)) {
            task.updateLastDate(lastDate);
        }

        return task;
    }

    @Transactional
    public void delayCycle(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findByTaskId(taskId);

        Task task = optionalTask.orElseThrow(() -> new IllegalStateException("유효하지 않은 할 일입니다."));

        LocalDate newLastDate = task.getLastDate().plusDays(task.getFrequency());
        task.updateLastDate(newLastDate);
    }

    @Transactional
    public void delayDay(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findByTaskId(taskId);

        Task task = optionalTask.orElseThrow(() -> new IllegalStateException("유효하지 않은 할 일입니다."));

        LocalDate newLastDate = task.getLastDate().plusDays(1);
        task.updateLastDate(newLastDate);
    }

    @Transactional
    public void changeCompleted(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findByTaskId(taskId);

        Task task = optionalTask.orElseThrow(() -> new IllegalStateException("유효하지 않은 할 일입니다."));

        task.changeCompleted();
    }

    @Transactional
    public List<Task> findTaskByLastDate(LocalDate lastDate) {
        return taskRepository.findByLastDate(lastDate);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }


}
