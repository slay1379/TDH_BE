package ToDo.example.repository;

import ToDo.example.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long categoryId);

    Optional<Category> findByCategoryName(String categoryName);

    List<Category> findAll();

    List<Category> findAllByCategoryName(String categoryName);

    void deleteById(Long categoryId);

    boolean existsByCategoryId(Long categoryId);
}
