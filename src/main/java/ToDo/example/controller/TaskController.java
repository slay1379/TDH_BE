package ToDo.example.controller;

import ToDo.example.DTO.TaskDto;
import ToDo.example.domain.Task;
import ToDo.example.domain.User;
import ToDo.example.repository.UserRepository;
import ToDo.example.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    //할 일 생성
    @PostMapping("/todosetting")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Task> createTask(@Validated(TaskDto.Create.class) @RequestBody TaskDto taskDto,
                                           @RequestHeader("Authorization") String token) {

        Task task = taskService.createTask(taskDto, token);
        return ResponseEntity.ok(task);
    }

    //할 일 수정
    @PostMapping("/todosetting/{taskid}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId,
                                           @Validated(TaskDto.Update.class) @RequestBody TaskDto taskDto) {

        Task updatedTask = taskService.updateTask(taskId, taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    //할 일 삭제
    @DeleteMapping("/todosetting/{taskId}/delete")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    //할 일 미루기
    @PostMapping("/todomain/{taskId}/delayCycle")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delayCycle(@PathVariable Long taskId) {
        taskService.delayCycle(taskId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/todomain/{taskId}/delayDay")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delayDay(@PathVariable Long taskId) {
        taskService.delayDay(taskId);
        return ResponseEntity.ok().build();
    }

    //할 일 완료여부 바꾸기
    @PostMapping("/todomain/{taskId}/changeCompleted")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changeCompleted(@PathVariable Long taskId) {
        taskService.changeCompleted(taskId);
        return ResponseEntity.ok().build();
    }

    //오늘 할 일 불러오기
    @GetMapping("/todomain/today")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Task>> getTodayTasks() {
        LocalDate today = LocalDate.now();
        List<Task> todayTasks = taskService.findTaskByLastDate(today);
        return ResponseEntity.ok(todayTasks);
    }


}
