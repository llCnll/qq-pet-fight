package cn.chennan.qqpetfight.activity.service;

import cn.chennan.qqpetfight.common.result.PetJsonUtil;
import cn.chennan.qqpetfight.user.UserHttpUtil;
import cn.chennan.qqpetfight.user.entity.UserInfo;
import cn.chennan.qqpetfight.user.service.UserService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * @author cn
 * @date 2022-07-10 12:20
 */
@Service
public class ActivityService {
    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    /**
     * 每日23点执行
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void dailyRun() {
        selfRunList();
        runList();
    }

    public void runList() {
        Collection<UserInfo> userList = userService.userList().getData();
        for (UserInfo user : userList) {
            try {
                List<String> urls = Files.readAllLines(Paths.get(Objects.requireNonNull(ActivityService.class.getClassLoader().getResource("runlist.txt")).toURI()));
                String title = "no title";
                for (String url : urls) {
                    if (url.startsWith("#")) {
                        title = url;
                        continue;
                    }
                    if (!StringUtils.hasLength(url)) {
                        continue;
                    }
                    sendHttp(user, url, 0, title);
                }
            } catch (IOException | URISyntaxException | NullPointerException e) {
                logger.warn("[{}]没有通用运行文件", user.getName());
            }
        }
    }

    public void selfRunList() {
        Collection<UserInfo> userList = userService.userList().getData();
        for (UserInfo user : userList) {
            try {
                List<String> urls = Files.readAllLines(Paths.get(Objects.requireNonNull(ActivityService.class.getClassLoader().getResource(String.format("runlist.%s.txt", user.getName()))).toURI()));
                String title = "no title";
                for (String url : urls) {
                    if (url.startsWith("#")) {
                        title = url;
                        continue;
                    }
                    if (!StringUtils.hasLength(url)) {
                        continue;
                    }
                    sendHttp(user, url, 0, title);
                }
            } catch (IOException | URISyntaxException | NullPointerException e) {
                logger.warn("[{}]没有自定义运行文件", user.getName());
            }
        }
    }

    private void sendHttp(UserInfo user, String url, int retry, String title) {
        String account = user.getName();
        try {
            if (retry > 10) {
                logger.warn("{} - {} - {} 重试达到达上限", user.getName(), title, url);
                return;
            }
            HttpEntity<String> requestEntity = UserHttpUtil.getHttpEntity(user);
            String body = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
            if (!StringUtils.hasLength(body)) {
                logger.info("{} - {} - retry:{} - {}", account, title, retry, "响应结果为空");
                sendHttp(user, url, retry + 1, title);
                return;
            }
            JSONObject ans = null;
            try {
                ans = new JSONObject(body);
                logger.info("{} - {} - retry:{} - {}", account, title, retry, ans);
            } catch (Exception e) {
                logger.error("发生了异常, {} - {} - retry:{}", account, title, retry, e);
                sendHttp(user, url, retry + 1, title);
                return;
            }
            if (PetJsonUtil.isNotLogin(ans) || PetJsonUtil.isSystemBusy(ans)) {
                sendHttp(user, url, retry + 1, title);
            }
        } catch (Throwable e) {
            logger.error("发生了异常, {} - {} - retry:{}", account, title, retry, e);
            sendHttp(user, url, retry + 1, title);
        }
    }
}
