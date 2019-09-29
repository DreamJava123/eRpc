package enums;

import lombok.Getter;

/**
 * 序列化算法枚举类
 * Created by TOM
 * On 2019/9/29 23:42
 */
@Getter
public enum SerializerAlogrithm {
  PROTOSTUFF((byte) 0, 0x001),
  JSON((byte) 1, 0x002);

  private Byte code;

  private Integer magicNum;

  SerializerAlogrithm(Byte code, Integer magicNum) {
    this.code = code;
    this.magicNum = magicNum;
  }
}
