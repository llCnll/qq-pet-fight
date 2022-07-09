package cn.chennan.qqpetfight.user.service.strategy;

import cn.chennan.qqpetfight.common.result.Result;
import cn.chennan.qqpetfight.user.constant.UserStrategy;
import cn.chennan.qqpetfight.user.entity.UserInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author cn
 * @date 2022-07-09 23:58
 */
@Service
public class CookieFileUserStrategyServiceImpl implements IUserStrategyService {
    private static final Logger logger = LoggerFactory.getLogger(CookieFileUserStrategyServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${cookie.file.url}")
    private String cookieFileUrl;

    @Override
    public Result<Collection<UserInfo>> getUserList() {
        String cookieData = restTemplate.getForEntity(cookieFileUrl, String.class).getBody();
        if (StringUtils.isEmpty(cookieData)) {
            logger.warn("远程获取cookie file文件返回值为空");
            return Result.fail(-1, "返回值为空");
        }
        String[] cookieLines = cookieData.split("\r\n");
        Set<UserInfo> userInfos = Sets.newHashSet();
        Arrays.stream(cookieLines)
                .filter(cookieLine -> !StringUtils.isEmpty(cookieLine))
                .forEach(cookieLine -> {
                    try {
                        Map<String, String> cookieMap = Splitter.on(";")
                                .trimResults()
                                .withKeyValueSeparator("=")
                                .split(cookieLine);
                        userInfos.add(new UserInfo(cookieMap.get("uin"), cookieMap.get("skey"), cookieMap.get("uin")));
                    } catch (Exception e) {
                        logger.error("解析cookie发生异常: [{}]", cookieLine, e);
                    }
                });
        return Result.success(userInfos);
    }

    @Override
    public UserStrategy getUserStrategy() {
        return UserStrategy.COOKIE_FILE;
    }
}
