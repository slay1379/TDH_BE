package ToDo.example.service;

import ToDo.example.authentication.JwtUtil;
import ToDo.example.domain.Category;
import ToDo.example.domain.User;
import ToDo.example.exception.GlobalExceptionHandler;
import ToDo.example.repository.CategoryRepository;
import ToDo.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final GlobalExceptionHandler globalExceptionHandler;

    //카테고리 생성
    public Category createCategory(String token, String categoryname) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않는 사용자입니다."));

        Category category = new Category(user, categoryname);

        return categoryRepository.save(category);
    }


    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 카테고리입니다."));
        categoryRepository.deleteById(categoryId);
    }
}
