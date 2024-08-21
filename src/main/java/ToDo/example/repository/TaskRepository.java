package ToDo.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepository {

    @PersistenceContext
    private EntityManager em;


}
