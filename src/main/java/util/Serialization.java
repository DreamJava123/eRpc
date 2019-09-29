package util;

/**
 * Created by TOM
 * On 2019/9/29 23:38
 */
public interface Serialization {

  /**
   * 序列化算法
   *
   * @return 序列化code可以用于 请求响应编解码时使用
   */
  byte getSerializerAlogrithm();

  /**
   * java 对象转换成二进制
   */
  <T> byte[] serialize(T t);

  /**
   * 二进制转换成 java 对象
   */
  <T> T deserialize(byte[] serialize, Class<T> t);

  Integer getMagicNum();

}
