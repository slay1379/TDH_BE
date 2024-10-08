package ToDo.example.controller;

import ToDo.example.authentication.TokenExtractor;
import ToDo.example.domain.Category;
import ToDo.example.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    //새 카테고리 생성하기
    @PostMapping("/todosetting/addCategory")
    public ResponseEntity<Category> addCategory(@RequestHeader("Authorization") String token, @RequestParam String categoryName) {

        String jwt = TokenExtractor.extract(token);
        Category category = categoryService.createCategory(jwt, categoryName);
        return ResponseEntity.ok(category);
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
