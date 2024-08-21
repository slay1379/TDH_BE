package ToDo.example.ToDoHousework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ToDo.example.authentication"}) // 이 경로는 JwtRequestFilter 클래스가 있는 패키지로 수정

public class ToDoHouseworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToDoHouseworkApplication.class, args);
	}

}
