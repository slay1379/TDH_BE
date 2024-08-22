package ToDo.example.controller;

import ToDo.example.domain.Task;
import ToDo.example.domain.User;
import ToDo.example.repository.UserRepository;
import ToDo.example.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
