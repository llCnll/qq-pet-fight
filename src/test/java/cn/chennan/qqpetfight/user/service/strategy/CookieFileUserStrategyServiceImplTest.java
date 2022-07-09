package cn.chennan.qqpetfight.user.service.strategy;

import cn.chennan.qqpetfight.common.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author cn
 * @date 2022-07-10 00:09
 */
@SpringBootTest
class CookieFileUserStrategyServiceImplTest {
    @Autowired
    private CookieFileUserStrategyServiceImpl service;

    @Test
    void getUserList() throws Exception {
        System.out.println(JsonUtil.objectToJson(service.getUserList()));
    }
}