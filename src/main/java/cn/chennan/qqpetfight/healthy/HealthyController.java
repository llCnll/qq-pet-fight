package cn.chennan.qqpetfight.healthy;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cn
 * @date 2022-06-07 22:49
 */
@RestController
public class HealthyController {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
