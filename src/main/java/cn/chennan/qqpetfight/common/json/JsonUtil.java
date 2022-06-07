package cn.chennan.qqpetfight.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;

/**
 * @author cn
 * @date 2022-05-07 21:08
 */
public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS)
            // 允许有未知字段
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build()
            // 序列化的时候，不输出null值
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    public static String objectToJson(Object obj) throws Exception {
        return OBJECT_MAPPER.writeValueAsString(obj);
    }

    public static <T> T jsonToObject(String json, Class<T> clazz) throws Exception {
        return OBJECT_MAPPER.readValue(json, clazz);
    }

    public static <T> T jsonToObject(File file, TypeReference<T> typeReference) throws Exception {
        return OBJECT_MAPPER.readValue(file, typeReference);
    }

    public static <T> T jsonToObject(String json, TypeReference<T> typeReference) throws Exception {
        return OBJECT_MAPPER.readValue(json, typeReference);
    }
}
