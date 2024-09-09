package ToDo.example.service;

import ToDo.example.DTO.TaskDto;
import ToDo.example.DTO.UpdateTaskDto;
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
        String username = getValidateUsername(token);
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

    public Task updateTask(Long taskId, UpdateTaskDto updateTaskDto) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 할 일 입니다."));

        if (updateTaskDto.getTaskName() != null && !task.getTaskName().equals(updateTaskDto.getTaskName())){
            task.updateTaskName(updateTaskDto.getTaskName());
        }

        if (updateTaskDto.getCategory().getCategoryName() != null && !updateTaskDto.getCategory().getCategoryName().isEmpty() && !task.getCategory().getCategoryName().equals(updateTaskDto.getCategory().getCategoryName())) {
            Category category = categoryRepository.findByCategoryName(updateTaskDto.getCategory().getCategoryName())
                    .orElseThrow(() -> new IllegalStateException("존재하지 않는 카테고리입니다."));
            task.updateCategory(category);
        }

        if (updateTaskDto.getFrequency() > 0 && task.getFrequency() != updateTaskDto.getFrequency()) {
            task.updateFrequency(updateTaskDto.getFrequency());
        }

        if (!task.getNotes().equals(updateTaskDto.getNotes())) {
            task.updateNotes(updateTaskDto.getNotes());
        }

        if (!task.getLastDate().equals(updateTaskDto.getLastDate())) {
            task.updateLastDate(updateTaskDto.getLastDate());
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

    private String getValidateUsername(String token) {
        if (jwtUtil.isTokenExpired(token)) {
            throw new IllegalStateException("토큰이 만료되었습니다. 다시 로그인 해주세요.");
        }
        return jwtUtil.extractUsername(token);
    }

}
