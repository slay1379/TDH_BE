package ToDo.example.controller;

import ToDo.example.DTO.TaskDto;
import ToDo.example.DTO.UpdateTaskDto;
import ToDo.example.authentication.TokenExtractor;
import ToDo.example.domain.Task;
import ToDo.example.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    //할 일 생성
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskDto taskDto,
                                           @RequestHeader("Authorization") String token) {

        String jwt = TokenExtractor.extract(token);
        Task task = taskService.createTask(taskDto, jwt);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    //할 일 수정
    @PostMapping("/{taskid}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId,
                                           @Valid @RequestBody UpdateTaskDto updateTaskDto) {

        Task updatedTask = taskService.updateTask(taskId, updateTaskDto);
        return ResponseEntity.ok(updatedTask);
    }

    //할 일 삭제
    @DeleteMapping("/{taskId}")
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
    @PostMapping("/{taskId}/delay-cycle")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delayCycle(@PathVariable Long taskId) {
        taskService.delayCycle(taskId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{taskId}/delay-day")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> delayDay(@PathVariable Long taskId) {
        taskService.delayDay(taskId);
        return ResponseEntity.ok().build();
    }

    //할 일 완료여부 바꾸기
    @PostMapping("/{taskId}/change-completed")
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
