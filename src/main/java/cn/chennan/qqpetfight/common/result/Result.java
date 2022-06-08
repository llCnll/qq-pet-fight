package cn.chennan.qqpetfight.common.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author cn
 * @date 2022-06-09 00:03
 */
public class Result<T> {
    private static final int SUCCESS_CODE = 0;
    private final int code;
    private final String message;
    private final T data;
    public static final Result<Void> SYSTEM_ERROR = fail(500, "system error");

    public static <T> Result<T> success(T data) {
        return new Result<>(SUCCESS_CODE, data, "ok");
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, null, message);
    }

    @JsonCreator
    public Result(@JsonProperty("code") int code, @JsonProperty("data") T data, @JsonProperty("message") String message) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public boolean success() {
        return this.getCode() == SUCCESS_CODE;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public T getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
