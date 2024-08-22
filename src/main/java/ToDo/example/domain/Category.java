package ToDo.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String categoryName;
    private String categoryColor;
}
