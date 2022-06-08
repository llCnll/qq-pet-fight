package cn.chennan.qqpetfight.common.result;

/**
 * @author cn
 * @date 2022-06-07 23:30
 */
public class BasicPetResult {
    /**
     * 0: 正常
     * -1: 今天已经领取过了，明天再来哦！* 免费次数不足
     * -2: 很抱歉，系统繁忙，请稍后再试!
     * -5 登录校验失败
     */
    private int result;
    private String msg;

    public boolean isSystemError() {
        return result == -2;
    }

    public boolean isSuccess() {
        return result == 0;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "{" +
                "result=" + result +
                ", msg='" + msg + '\'' +
                '}';
    }
}
