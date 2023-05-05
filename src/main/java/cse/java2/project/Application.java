package cse.java2.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is the main class of the Spring Boot application.
 * mvn spring-boot:run
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        StackOverflowApi stackOverflowApi = new StackOverflowApi();
        stackOverflowApi.fetchData("questions", null);
        SpringApplication.run(Application.class, args);
    }

}
