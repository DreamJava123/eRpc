package enums;

import lombok.Getter;
import util.JsonSerializationUtil;
import util.ProtoSerializationUtil;
import util.Serialization;

/**
 * 序列化算法枚举类
 * Created by TOM
 * On 2019/9/29 23:42
 */
@Getter
public enum SerializerAlogrithm {
  PROTOSTUFF((byte) 0, 0x001, ProtoSerializationUtil.getInstance()),
  JSON((byte) 1, 0x002, JsonSerializationUtil.getInstance());

  private Byte code;

  private Integer magicNum;

  private Serialization serialization;

  SerializerAlogrithm(Byte code, Integer magicNum, Serialization serialization) {
    this.code = code;
    this.magicNum = magicNum;
    this.serialization = serialization;
  }

  public static SerializerAlogrithm coverMagicNum(Byte b) {
    for (SerializerAlogrithm value : SerializerAlogrithm.values()) {
      if (b.equals(value.code)) {
        return value;
      }
    }
    return null;
  }
}
