package ToDo.example.repository;

import ToDo.example.domain.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;


@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Category category) {
        em.persist(category);
    }

    public Category findOne(Long id) {
        return em.createQuery(
                "select c from Category c where c.categoryId = :id", Category.class)
                .getSingleResult();
    }

    public Category findByName(String name) {
        return em.createQuery(
                        "select c from Category c where c.categoryId = :name", Category.class)
                .getSingleResult();
    }


}
