package cn.printf.api;

import cn.printf.api.domain.todo.Todo;
import cn.printf.api.domain.todo.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class Application {
	private static final Logger LOG = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Profile("local")
	public CommandLineRunner setup(TodoRepository toDoRepository) {
		return (args) -> {
			toDoRepository.add(new Todo("Remove unused imports", true));
			toDoRepository.add(new Todo("Clean the code", true));
			toDoRepository.add(new Todo("Build the artifacts", false));
			toDoRepository.add(new Todo("Deploy the jar file", true));
			LOG.info("The sample data has been generated");
		};
	}
}
