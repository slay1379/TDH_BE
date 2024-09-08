package ToDo.example.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank(message = "이름을 입력해주세요.")
    private String categoryName;

    @Builder
    public Category(User user, String categoryName) {
        this.user = user;
        this.categoryName = categoryName;
    }

    public void updateCategoryName(String newCategoryName) {
        this.categoryName = newCategoryName;
    }

}
