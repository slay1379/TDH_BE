package ToDo.example.service;

import ToDo.example.DTO.TaskRequest;
import ToDo.example.domain.Category;
import ToDo.example.domain.Task;
import ToDo.example.domain.User;
import ToDo.example.repository.CategoryRepository;
import ToDo.example.repository.TaskRepository;
import ToDo.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new IllegalStateException("유효하지 않은 ID입니다.");
        }

        Category category = categoryRepository.findByName(categoryName);
        if (category == null) {
            throw new IllegalStateException("유효하지 않은 카테고리ID 입니다.");
        }

        Task task = new Task(taskName, category, frequency, notes, user);
        taskRepository.save(task);
        return task;
    }




}
