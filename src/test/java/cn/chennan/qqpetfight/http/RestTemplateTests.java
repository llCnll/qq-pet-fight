package cn.chennan.qqpetfight.http;

import cn.chennan.qqpetfight.common.json.JsonUtil;
import cn.chennan.qqpetfight.common.result.BasicPetResult;
import cn.chennan.qqpetfight.common.result.Result;
import cn.chennan.qqpetfight.config.OnceActivityConfig;
import cn.chennan.qqpetfight.user.UserHttpUtil;
import cn.chennan.qqpetfight.user.entity.UserInfo;
import cn.chennan.qqpetfight.user.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author cn
 * @date 2022-06-07 23:01
 */
@SpringBootTest
class RestTemplateTests {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateTests.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    List<String> dailyUrls = Lists.newArrayList(
            // 好礼步步升(7天)
            "https://fight.pet.qq.com/cgi-bin/petpk?cmd=newAct&subtype=37&op=get",
            // 娃娃机
            "https://fight.pet.qq.com/cgi-bin/petpk?cmd=newAct&subtype=114&op=1",
            // 乱斗-冠军助威
            "https://fight.pet.qq.com/cgi-bin/petpk?cmd=luandou&id=19&op=8",
            // 乱斗-试炼
            "https://fight.pet.qq.com/cgi-bin/petpk?cmd=luandou&id=18&op=8",
            // 乱斗-精英2-3
            "https://fight.pet.qq.com/cgi-bin/petpk?cmd=luandou&id=17&op=8",
            // 乱斗-护法3-3
            "https://fight.pet.qq.com/cgi-bin/petpk?cmd=luandou&id=16&op=8",
            // 乱斗-门童1-3
            "https://fight.pet.qq.com/cgi-bin/petpk?cmd=luandou&id=15&op=8"
    );

    @Test
    void testRestTemplate() throws Exception {
        //{"result":"-2","msg":"很抱歉，系统繁忙，请稍后再试!"}
        // 登录态需要 skey和uin的值
        Result<List<UserInfo>> userListResult = userService.userList();
        if (!userListResult.success()) {
            logger.warn("获得用户列表失败");
        }
        userListResult.getData().forEach(user -> dailyUrls.forEach(url -> logger.info("{} - {}", user.getName(), sendHttp(UserHttpUtil.getHttpEntity(user), url))));
    }

    @Test
    void testOnceActivity() throws Exception {
        Result<List<UserInfo>> userListResult = userService.userList();
        if (!userListResult.success()) {
            logger.warn("获得用户列表失败");
        }

        userListResult.getData().forEach(user -> {
            HttpEntity<String> requestEntity = UserHttpUtil.getHttpEntity(user);
            for (OnceActivityConfig config : OnceActivityConfig.values()) {
                if (config.isArg()) {
                    for (int i = config.getStart(); i <= config.getEnd(); ++i) {
                        int retry = 0;
                        for (int j = 0; j < config.getCount(); ++j) {
                            BasicPetResult ans = sendHttp(requestEntity, String.format(config.getUrl(), i));
                            while (ans.isSystemError() && retry < 500) {
                                ans = sendHttp(requestEntity, String.format(config.getUrl(), i));
                                retry++;
                            }
                            System.out.println(user.getName() + " --> " + "config + " + "-- > " + i + "-- > " + j + "次-- > " + ans);
                        }
                    }
                    System.out.println("---------" + user.getName() + "-------->" + config + " finish");
                }
            }
        });
    }

    private BasicPetResult sendHttp(HttpEntity<String> requestEntity, String url) {
        String body = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        BasicPetResult ans = null;
        try {
            ans = JsonUtil.jsonToObject(body, new TypeReference<BasicPetResult>() {
            });
        } catch (Exception e) {
            System.out.println("发生了异常: " + e);
            BasicPetResult basicResult = new BasicPetResult();
            basicResult.setResult(-2);
            return basicResult;
        }
        return ans;
    }
}