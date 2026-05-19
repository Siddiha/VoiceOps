package journal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This is the entry point — the main class that starts everything
@SpringBootApplication
public class JournalApp {

    public static void main(String[] args) {
        SpringApplication.run(JournalApp.class, args);
        // When you run this, Spring Boot starts a web server on port 8080
    }

}