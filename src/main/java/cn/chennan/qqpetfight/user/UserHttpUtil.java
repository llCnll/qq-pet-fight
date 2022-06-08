package cn.chennan.qqpetfight.user;

import cn.chennan.qqpetfight.user.entity.UserInfo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cn
 * @date 2022-06-09 00:11
 */
public class UserHttpUtil {
    private UserHttpUtil() {
    }

    public static HttpEntity<String> getHttpEntity(UserInfo userInfo) {
        return new HttpEntity<>(getHttpHeaders(userInfo));
    }

    public static HttpHeaders getHttpHeaders(UserInfo userInfo) {
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add("skey=" + userInfo.getSkey());
        cookies.add("uin=" + userInfo.getUin());
        headers.put(HttpHeaders.COOKIE, cookies);
        return headers;
    }
}
