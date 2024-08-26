package ToDo.example.controller;

import ToDo.example.domain.Category;
import ToDo.example.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    //새 카테고리 생성하기
    @PostMapping("/todosetting/addCategory")
    public ResponseEntity<Category> addCategory(HttpServletRequest request, @RequestParam String categoryName) {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            Category category = categoryService.createCategory(token, categoryName);
            return ResponseEntity.ok(category);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    //카테고리 삭제
    @DeleteMapping("/todosetting/{catgoryId}/deleteCategory")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
