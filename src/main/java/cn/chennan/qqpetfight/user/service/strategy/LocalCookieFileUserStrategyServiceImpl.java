package cn.chennan.qqpetfight.user.service.strategy;

import cn.chennan.qqpetfight.common.result.Result;
import cn.chennan.qqpetfight.user.constant.UserStrategy;
import cn.chennan.qqpetfight.user.entity.UserInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author cn
 * @date 2022-07-09 23:58
 */
@Service
public class LocalCookieFileUserStrategyServiceImpl implements IUserStrategyService {
    private static final Logger logger = LoggerFactory.getLogger(LocalCookieFileUserStrategyServiceImpl.class);

    @Value("${local.cookie.file.url}")
    private String cookieFileUrl;

    @Override
    public Result<Collection<UserInfo>> getUserList() {
        List<String> cookieLines;
        try {
            cookieLines = Files.readAllLines(Paths.get(cookieFileUrl));
        } catch (Exception ex) {
            logger.error("读取本地cookie file文件返回值发生异常", ex);
            return Result.fail(-1, "返回值为空");
        }
        Set<UserInfo> userInfos = Sets.newHashSet();
        cookieLines.stream()
                .filter(cookieLine -> !StringUtils.isEmpty(cookieLine))
                .forEach(cookieLine -> {
                    try {
                        Map<String, String> cookieMap = Splitter.on(";")
                                .trimResults()
                                .withKeyValueSeparator("=")
                                .split(cookieLine);
                        userInfos.add(new UserInfo(cookieMap.get("uin").substring(2), cookieMap.get("skey"), cookieMap.get("uin")));
                    } catch (Exception e) {
                        logger.error("解析cookie发生异常: [{}]", cookieLine, e);
                    }
                });
        return Result.success(userInfos);
    }

    @Override
    public UserStrategy getUserStrategy() {
        return UserStrategy.LOCAL_COOKIE_FILE;
    }
}
