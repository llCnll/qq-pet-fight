package cn.chennan.qqpetfight.http;

import cn.chennan.qqpetfight.common.result.PetJsonUtil;
import cn.chennan.qqpetfight.common.result.Result;
import cn.chennan.qqpetfight.config.OnceActivityConfig;
import cn.chennan.qqpetfight.user.UserHttpUtil;
import cn.chennan.qqpetfight.user.entity.UserInfo;
import cn.chennan.qqpetfight.user.service.UserService;
import org.assertj.core.util.Lists;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
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

//    @Test
    void testRestTemplate() throws Exception {
        //{"result":"-2","msg":"很抱歉，系统繁忙，请稍后再试!"}
        // 登录态需要 skey和uin的值
        Result<Collection<UserInfo>> userListResult = userService.userList();
        if (!userListResult.success()) {
            logger.warn("获得用户列表失败");
        }
        userListResult.getData().forEach(user -> dailyUrls.forEach(url -> sendHttp(UserHttpUtil.getHttpEntity(user), url, 0, url, user.getName())));
    }

//    @Test
    void testOnceActivity() throws Exception {
        Result<Collection<UserInfo>> userListResult = userService.userList();
        if (!userListResult.success()) {
            logger.warn("获得用户列表失败");
        }

        userListResult.getData().forEach(user -> {
            HttpEntity<String> requestEntity = UserHttpUtil.getHttpEntity(user);
            for (OnceActivityConfig config : OnceActivityConfig.values()) {
                if (config.isArg()) {
                    for (int i = config.getStart(); i <= config.getEnd(); ++i) {
                        for (int j = 0; j < config.getCount(); ++j) {
                            sendHttp(requestEntity, String.format(config.getUrl(), i), 0, config.toString(), user.getName());
                        }
                    }
                    logger.info("---------" + user.getName() + "-------->" + config + " finish");
                }
            }
        });
    }

    @Test
    void testRunList() {
        try {
            List<String> urls = Files.readAllLines(Paths.get("src", "main", "resources", "runlist.txt"));
            Collection<UserInfo> userList = userService.userList().getData();
            String title = "no title";
            for (String url : urls) {
                if (url.startsWith("#")) {
                    title = url;
                    continue;
                }
                if (!StringUtils.hasLength(url)) {
                    continue;
                }
                for (UserInfo user : userList) {
                    sendHttp(UserHttpUtil.getHttpEntity(user), url, 0, title, user.getName());
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Test
    void testSelfRunList() {
        Collection<UserInfo> userList = userService.userList().getData();
        for (UserInfo user : userList) {
            try {
                List<String> urls = Files.readAllLines(Paths.get("src", "main", "resources", "runlist." + user.getName() + ".txt"));
                String title = "no title";
                for (String url : urls) {
                    if (url.startsWith("#")) {
                        title = url;
                        continue;
                    }
                    if (!StringUtils.hasLength(url)) {
                        continue;
                    }
                    sendHttp(UserHttpUtil.getHttpEntity(user), url, 0, title, user.getName());
                }
            } catch (IOException e) {
                logger.warn("[{}]没有自定义运行文件", user.getName());
            }
        }
    }

    private void sendHttp(HttpEntity<String> requestEntity, String url, int retry, String title, String account) {
        try {
            if (retry > 10) {
                logger.warn("{} - {} - {} 重试达到达上限", account, title, url);
                return;
            }
            String body = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
            if (!StringUtils.hasLength(body)) {
                logger.info("{} - {} - retry:{} - {}", account, title, retry, "响应结果为空");
                sendHttp(requestEntity, url, retry + 1, title, account);
                return;
            }
            JSONObject ans = null;
            try {
                ans = new JSONObject(body);
                logger.info("{} - {} - retry:{} - {}", account, title, retry, ans);
            } catch (Exception e) {
                logger.error("发生了异常, {} - {} - retry:{}", account, title, retry, e);
                sendHttp(requestEntity, url, retry + 1, title, account);
                return;
            }
            if (PetJsonUtil.isNotLogin(ans) || PetJsonUtil.isSystemBusy(ans)) {
                sendHttp(requestEntity, url, retry + 1, title, account);
            }
        } catch (Throwable e) {
            logger.error("发生了异常, {} - {} - retry:{}", account, title, retry, e);
            sendHttp(requestEntity, url, retry + 1, title, account);
        }
    }
}
