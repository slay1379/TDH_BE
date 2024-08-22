package ToDo.example.repository;

import ToDo.example.domain.Task;
import ToDo.example.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TaskRepository {

    @PersistenceContext
    private EntityManager em;

    public Task save(Task task) {
        em.persist(task);
        return task;
    }

    public Task findOne(Long id) {
        return em.find(Task.class, id);
    }

    public List<Task> findAll(User user) {
        return em.createQuery("select t from Task t where t.user = :user", Task.class)
                .setParameter("user",user)
                .getResultList();
    }



}
