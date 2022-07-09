package cn.chennan.qqpetfight.user.service.strategy;

import cn.chennan.qqpetfight.common.result.Result;
import cn.chennan.qqpetfight.user.constant.UserStrategy;
import cn.chennan.qqpetfight.user.entity.UserInfo;

import java.util.Collection;

/**
 * @author cn
 * @date 2022-07-09 23:56
 */
public interface IUserStrategyService {

    Result<Collection<UserInfo>> getUserList();

    UserStrategy getUserStrategy();
}
