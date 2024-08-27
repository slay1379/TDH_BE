package ToDo.example.service;

import ToDo.example.DTO.TaskDto;
import ToDo.example.authentication.JwtUtil;
import ToDo.example.domain.Category;
import ToDo.example.domain.Task;
import ToDo.example.domain.User;
import ToDo.example.repository.CategoryRepository;
import ToDo.example.repository.TaskRepository;
import ToDo.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final JwtUtil jwtUtil;

    //할 일 생성
    public Task createTask(TaskDto taskDto, String token) {
        if (jwtUtil.isTokenExpired(token)) {
            throw new IllegalStateException("토큰이 만료되었습니다 다시 로그인 해주세요.");
        }
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않은 사용자입니다."));

        Task task = Task.builder()
                .taskName(taskDto.getTaskName())
                .user(user)
                .frequency(taskDto.getFrequency())
                .category(taskDto.getCategory())
                .notes(taskDto.getNotes())
                .build();

        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, TaskDto taskDto) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 할 일 입니다."));

        if (taskDto.getTaskName() != null && !task.getTaskName().equals(taskDto.getTaskName())){
            task.updateTaskName(taskDto.getTaskName());
        }

        if (taskDto.getCategory().getCategoryName() != null && !taskDto.getCategory().getCategoryName().isEmpty() && !task.getCategory().getCategoryName().equals(taskDto.getCategory().getCategoryName())) {
            Category category = categoryRepository.findByCategoryName(taskDto.getCategory().getCategoryName())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 카테고리입니다."));
            task.updateCategory(category);
        }

        if (taskDto.getFrequency() > 0 && task.getFrequency() != taskDto.getFrequency()) {
            task.updateFrequency(taskDto.getFrequency());
        }

        if (!task.getNotes().equals(taskDto.getNotes())) {
            task.updateNotes(taskDto.getNotes());
        }

        if (!task.getLastDate().equals(taskDto.getLastDate())) {
            task.updateLastDate(taskDto.getLastDate());
        }

        return task;
    }

    public void delayCycle(Long taskId) {
        Task task = getTaskById(taskId);

        LocalDate newLastDate = task.getLastDate().plusDays(task.getFrequency());
        task.updateLastDate(newLastDate);
    }

    public void delayDay(Long taskId) {
        Task task = getTaskById(taskId);

        LocalDate newLastDate = task.getLastDate().plusDays(1);
        task.updateLastDate(newLastDate);
    }

    @Transactional
    public void changeCompleted(Long taskId) {
        Task task = getTaskById(taskId);

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

    private Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("유효하지 않은 할 일입니다."));
    }


}
