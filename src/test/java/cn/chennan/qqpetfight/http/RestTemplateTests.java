package cn.chennan.qqpetfight.http;

import cn.chennan.qqpetfight.common.json.JsonUtil;
import cn.chennan.qqpetfight.common.result.BasicResult;
import cn.chennan.qqpetfight.config.OnceActivityConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cn
 * @date 2022-06-07 23:01
 */
@SpringBootTest
class RestTemplateTests {

    @Autowired
    private RestTemplate restTemplate;

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
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add("skey=@");
        cookies.add("uin=o0");
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        dailyUrls.forEach(url -> {
            sendHttp(requestEntity, url);
        });
    }

    @Test
    void testOnceActivity() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add("skey=@");
        cookies.add("uin=o0");
        headers.put(HttpHeaders.COOKIE, cookies);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        for (OnceActivityConfig config : OnceActivityConfig.values()) {
            if (config.isArg()) {
                for (int i = config.getStart(); i <= config.getEnd(); ++i) {
                    int retry = 0;
                    for (int j = 0; j < config.getCount(); ++j) {
                        BasicResult ans = sendHttp(requestEntity, String.format(config.getUrl(), i));
                        while (ans.isSystemError() && retry < 500) {
                            ans = sendHttp(requestEntity, String.format(config.getUrl(), i));
                            retry++;
                        }
                        System.out.println(config + " --> " + i + " --> " + j + "次 --> " + ans);
                    }
                }
                System.out.println("----------------->" + config + " finish");
            }
        }
    }

    private BasicResult sendHttp(HttpEntity<String> requestEntity, String url) {
        String body = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class).getBody();
        BasicResult ans = null;
        try {
            ans = JsonUtil.jsonToObject(body, new TypeReference<BasicResult>() {
            });
        } catch (Exception e) {
            System.out.println("发生了异常: " + e);
            BasicResult basicResult = new BasicResult();
            basicResult.setResult(-2);
            return basicResult;
        }
        return ans;
    }
}
