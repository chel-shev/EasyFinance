package ru.ixec.easyfinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class EasyFinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EasyFinanceApplication.class, args);
    }
}
