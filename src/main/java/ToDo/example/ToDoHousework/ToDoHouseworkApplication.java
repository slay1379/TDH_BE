package ToDo.example.ToDoHousework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "ToDo.example.repository")
@EntityScan(basePackages = "ToDo.example.domain")  // 엔티티가 있는 패키지
public class ToDoHouseworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoHouseworkApplication.class, args);
	}

}
