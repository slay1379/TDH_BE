package ToDo.example.repository;

import ToDo.example.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findById(Long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}
