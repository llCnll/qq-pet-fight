package cn.chennan.qqpetfight.user.service.strategy;

import cn.chennan.qqpetfight.common.result.Result;
import cn.chennan.qqpetfight.user.constant.UserStrategy;
import cn.chennan.qqpetfight.user.entity.UserInfo;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author cn
 * @date 2022-07-09 23:58
 */

@Service
public class CustomUserStrategyServiceImpl implements IUserStrategyService{

    @Override
    public Result<Collection<UserInfo>> getUserList() {
        List<UserInfo> userCookies = Lists.newArrayListWithCapacity(3);
        userCookies.add(new UserInfo("416773613", "@", "o0416773613"));
        userCookies.add(new UserInfo("972581592", "@", "o0972581592"));
        userCookies.add(new UserInfo("409987865", "@", "o0409987865"));
        return Result.success(userCookies);
    }

    @Override
    public UserStrategy getUserStrategy() {
        return UserStrategy.CUSTOM;
    }
}
