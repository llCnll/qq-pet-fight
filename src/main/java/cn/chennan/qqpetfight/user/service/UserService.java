package cn.chennan.qqpetfight.user.service;

import cn.chennan.qqpetfight.common.result.Result;
import cn.chennan.qqpetfight.user.entity.UserInfo;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cn
 * @date 2022-06-08 23:03
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Result<List<UserInfo>> userList() {
        List<UserInfo> userCookies = Lists.newArrayListWithCapacity(3);
        userCookies.add(new UserInfo("416773613", "@", "o0416773613"));
        userCookies.add(new UserInfo("972581592", "@", "o0972581592"));
        userCookies.add(new UserInfo("409987865", "@", "o0409987865"));
        return Result.success(userCookies);
    }
}
