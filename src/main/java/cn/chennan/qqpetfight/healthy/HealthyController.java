package cn.chennan.qqpetfight.healthy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cn
 * @date 2022-06-07 22:49
 */
@RestController
public class HealthyController {
    private static final Logger logger = LoggerFactory.getLogger(HealthyController.class);

    @GetMapping("/ping")
    public String ping() {
        logger.debug("debug日志");
        logger.info("info日志");
        logger.warn("warn日志");
        logger.error("error日志");
        return "pong";
    }
}
