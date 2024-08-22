package ToDo.example.controller;

import ToDo.example.domain.Task;
import ToDo.example.domain.User;
import ToDo.example.repository.UserRepository;
import ToDo.example.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    //할 일 생성
    @PostMapping("/todosetting")
    public ResponseEntity<Task> createTask(@RequestParam String taskName,
                                           @RequestParam String categoryName,
                                           @RequestParam int frequency,
                                           @RequestParam String notes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByName(username);

        Task task = taskService.createTask(taskName, user.getUserId(), categoryName, frequency, notes);
        return ResponseEntity.ok(task);
    }

    //할 일 수정
    @PostMapping("/todosetting/{taskid}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId,
                                           @RequestParam String taskName,
                                           @RequestParam String categoryName,
                                           @RequestParam int frequency,
                                           @RequestParam String notes,
                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate lastDate) {
        Task updatedTask = taskService.updateTask(taskId, taskName, categoryName, frequency, notes, lastDate);
        return ResponseEntity.ok(updatedTask);
    }

    //할 일 미루기
    @PostMapping("/todosetting/{taskId}/delayCycle")
    public ResponseEntity<Void> delayCycle(@PathVariable Long taskId) {
        taskService.delayCycle(taskId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/todosetting/{taskId}/delayDay")
    public ResponseEntity<Void> delayDay(@PathVariable Long taskId) {
        taskService.delayDay(taskId);
        return ResponseEntity.ok().build();
    }
    
}
