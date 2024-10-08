package ToDo.example.service;

import ToDo.example.authentication.JwtUtil;
import ToDo.example.domain.Category;
import ToDo.example.domain.Users;
import ToDo.example.repository.CategoryRepository;
import ToDo.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    //카테고리 생성
    public Category createCategory(String token, String categoryname) {
        String username = getValidateUsername(token);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("유효하지 않는 사용자입니다."));

        Category category = new Category(users, categoryname);

        return categoryRepository.save(category);
    }


    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 카테고리입니다."));
        categoryRepository.deleteById(categoryId);
    }

    private String getValidateUsername(String token) {
        if (jwtUtil.isTokenExpired(token)) {
            throw new IllegalStateException("토큰이 만료되었습니다. 다시 로그인 해주세요.");
        }
        return jwtUtil.extractUsername(token);
    }
}
