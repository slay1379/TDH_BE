package ToDo.example.repository;

import ToDo.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(Long userId);

    void deleteByUserId(Long userId);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
