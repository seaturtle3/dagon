package kroryi.dagon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DagonApplication {
    public static void main(String[] args) {
        SpringApplication.run(DagonApplication.class, args);
    }
}
