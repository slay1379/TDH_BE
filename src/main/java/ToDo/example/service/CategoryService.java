package ToDo.example.service;

import ToDo.example.authentication.JwtUtil;
import ToDo.example.domain.Category;
import ToDo.example.domain.User;
import ToDo.example.repository.CategoryRepository;
import ToDo.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    //카테고리 생성
    public Category createCategory(String token, String categoryname) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByName(username);

        if (user == null) {
            throw new IllegalStateException("유효하지 않은 사용자입니다.");
        }

        Category category = new Category(user,categoryname);
        categoryRepository.save(category);

        return category;
    }
}
