package util;

import com.alibaba.fastjson.JSON;
import enums.SerializerAlogrithm;

/**
 * Json方式的序列话工具类
 * Created by TOM
 * On 2019/9/29 23:54
 */
public class JsonSerializationUtil implements Serialization {

  private static class JsonSerializationHolder {

    private final static JsonSerializationUtil JSON_SERIALIZATION_UTIL = new JsonSerializationUtil();
  }

  public static JsonSerializationUtil getInstance() {
    return JsonSerializationHolder.JSON_SERIALIZATION_UTIL;
  }

  @Override
  public byte getSerializerAlogrithm() {
    return SerializerAlogrithm.JSON.getCode();
  }

  @Override
  public <T> byte[] serialize(T t) {
    return JSON.toJSONBytes(t);
  }

  @Override
  public <T> T deserialize(byte[] serialize, Class<T> t) {
    return JSON.parseObject(serialize, t);
  }

  @Override
  public Integer getMagicNum() {
    return SerializerAlogrithm.JSON.getMagicNum();
  }
}
