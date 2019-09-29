package util;

import enums.SerializerAlogrithm;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * 对象序列化反序列化工具类
 * Created by TOM
 * On 2019/9/29 22:23
 */
public enum ProtoSerializationUtil implements Serialization {
  INSTANCE;
  private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

  private static final Objenesis OBJENESIS = new ObjenesisStd(true);

  @SuppressWarnings("unchecked")
  private static <T> Schema<T> getSchema(Class<T> cls) {
    return (Schema<T>) cachedSchema.computeIfAbsent(cls, RuntimeSchema::createFrom);
  }

  @Override
  public byte getSerializerAlogrithm() {
    return SerializerAlogrithm.PROTOSTUFF.getCode();
  }

  /**
   * 序列化方法
   *
   * @param t
   * @param <T>
   * @return
   */
  @SuppressWarnings("unchecked")
  public <T> byte[] serialize(T t) {
    Class<T> aClass = (Class<T>) t.getClass();
    //不能放为全局变量 多线程访问不安全
    LinkedBuffer allocate = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    Schema<T> schema = getSchema(aClass);
    try {
      return ProtostuffIOUtil.toByteArray(t, schema, allocate);
    } finally {
      allocate.clear();
    }
  }

  /**
   * 反序列化
   *
   * @param t 对应的类
   */
  public <T> T deserialize(byte[] serialize, Class<T> t) {
    //实例化
    T t1 = OBJENESIS.newInstance(t);
    Schema<T> schema = getSchema(t);
    ProtostuffIOUtil.mergeFrom(serialize, t1, schema);
    return t1;
  }

  @Override
  public Integer getMagicNum() {
    return SerializerAlogrithm.PROTOSTUFF.getMagicNum();
  }

}
