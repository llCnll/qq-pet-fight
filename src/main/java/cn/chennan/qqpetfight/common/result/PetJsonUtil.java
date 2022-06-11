package cn.chennan.qqpetfight.common.result;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * @author cn
 * @date 2022-06-11 15:39
 */
public class PetJsonUtil {
    private static final String CODE = "result";
    private static final String MESSAGE = "msg";

    private static final int NOT_LOGIN = -5;
    private static final List<String> SYSTEM_BUSY = Arrays.asList("系统繁忙");

    public static boolean isNotLogin(JSONObject ans) {
        return ans.optInt(CODE) == NOT_LOGIN;
    }

    public static boolean isSystemBusy(JSONObject ans) {
        String msg = ans.optString(MESSAGE);
        return SYSTEM_BUSY.stream().anyMatch(msg::contains);
    }

    private PetJsonUtil() {

    }
}
