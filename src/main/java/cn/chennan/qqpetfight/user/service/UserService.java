package cn.chennan.qqpetfight.user.service;

import cn.chennan.qqpetfight.common.result.Result;
import cn.chennan.qqpetfight.user.constant.UserStrategy;
import cn.chennan.qqpetfight.user.entity.UserInfo;
import cn.chennan.qqpetfight.user.service.strategy.IUserStrategyService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

/**
 * @author cn
 * @date 2022-06-08 23:03
 */
@Service
public class UserService implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final Map<UserStrategy, IUserStrategyService> iUserStrategyServiceMap = Maps.newHashMap();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IUserStrategyService> tempMap = applicationContext.getBeansOfType(IUserStrategyService.class);
        for (IUserStrategyService value : tempMap.values()) {
            iUserStrategyServiceMap.put(value.getUserStrategy(), value);
        }
    }

    public Result<Collection<UserInfo>> userList() {
        return iUserStrategyServiceMap.get(UserStrategy.COOKIE_FILE).getUserList();
    }
}
