package cn.chennan.qqpetfight.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * @author cn
 * @date 2022-06-07 23:00
 */
@Configuration
public class ApplicationConfig {
    @Bean
    public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
        builder.setReadTimeout(Duration.ofSeconds(2));
        builder.setConnectTimeout(Duration.ofSeconds(2));
        return builder.build();
    }
}
