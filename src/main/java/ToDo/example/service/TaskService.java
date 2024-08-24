package ToDo.example.service;

import ToDo.example.domain.Category;
import ToDo.example.domain.Task;
import ToDo.example.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    //할 일 생성
    @Transactional
    public Task createTask(String taskName, Long userId, String categoryName, int frequency, String notes) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new IllegalStateException("유효하지 않은 사용자 입니다.");
        }

        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new IllegalStateException("유효하지 않은 카테고리 입니다.");
        }

        Task task = new Task(taskName, category, frequency, notes, user);
        taskRepository.save(task);
        return task;
    }

    @Transactional
    public Task updateTask(Long taskId, String taskName, String categoryName, int frequency, String notes, LocalDate lastDate) {

        Task task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new IllegalStateException("유효하지 않은 일입니다.");
        }

        if (taskName != null && !taskName.isEmpty() && !task.getTaskName().equals(taskName)){
            task.updateTaskName(taskName);
        }

        if (categoryName != null && !categoryName.isEmpty() && !task.getCategory().getCategoryName().equals(categoryName)) {
            Category category = categoryRepository.findByName(categoryName);
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
        Task task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new IllegalStateException("유효하지 않는 할 일 입니다.");
        }
        LocalDate newLastDate = task.getLastDate().plusDays(task.getFrequency());
        task.updateLastDate(newLastDate);
    }

    @Transactional
    public void delayDay(Long taskId) {
        Task task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new IllegalStateException("유효하지 않는 할 일 입니다.");
        }
        LocalDate newLastDate = task.getLastDate().plusDays(1);
        task.updateLastDate(newLastDate);
    }

    @Transactional
    public void changeCompleted(Long taskId) {
        Task task = taskRepository.findOne(taskId);
        if (task == null) {
            throw new IllegalStateException("유효하지 않는 할 일 입니다.");
        }
        task.changeCompleted();
    }

    @Transactional
    public List<Task> findTaskByLastDate(LocalDate lastDate) {
        return taskRepository.findByLastDate(lastDate);
    }


}
