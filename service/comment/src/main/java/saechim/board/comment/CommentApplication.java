package saechim.board.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "saechim.board")
@EnableJpaRepositories(basePackages = "saechim.board")
public class CommentApplication {
	public static void main(String[] args) {
		SpringApplication.run(CommentApplication.class, args);
	}
}