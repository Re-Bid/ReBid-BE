package server.rebid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class ReBidApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReBidApplication.class, args);
    }

}
