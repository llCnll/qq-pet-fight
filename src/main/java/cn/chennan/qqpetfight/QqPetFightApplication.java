package cn.chennan.qqpetfight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class QqPetFightApplication {

    public static void main(String[] args) {
        SpringApplication.run(QqPetFightApplication.class, args);
    }

}
