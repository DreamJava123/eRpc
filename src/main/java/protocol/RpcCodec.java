package protocol;


import enums.SerializerAlogrithm;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import util.Serialization;

/**
 * 数据包装类
 * 校验位/4,请求命令/1,序列化算法/1,包长度/4,包数据,
 * Created by TOM
 * On 2019/9/29 23:12
 */
@Getter
@Setter
public class RpcCodec {

  private static final Map cache = new ConcurrentHashMap(16);
  private Integer magicNum;

  private Class clazz;

  protected RpcCodec() {
  }

  public RpcCodec(Integer magicNum, Class clazz) {
    this.magicNum = magicNum;
    this.clazz = clazz;
  }

  public ByteBuf encode(ByteBufAllocator byteBufAllocator, BasePacket basePacket, Serialization serialization) {
    ByteBuf buffer = byteBufAllocator.buffer();
    byte[] serialize = serialization.serialize(basePacket);
    buffer.writeInt(serialization.getMagicNum());
    buffer.writeByte(basePacket.getCommand());
    buffer.writeByte(serialization.getSerializerAlogrithm());
    buffer.writeInt(serialize.length);
    buffer.writeBytes(serialize);
    return buffer;
  }

  public BasePacket decode(ByteBuf byteBuf, Serialization serialization) {
    int magicNum = byteBuf.readInt();
    byteBuf.skipBytes(1);
    byte serializerAlogrithm = byteBuf.readByte();
    if (SerializerAlogrithm.coverMagicNum(serializerAlogrithm) == null || !Objects
        .requireNonNull(SerializerAlogrithm.coverMagicNum(serializerAlogrithm)).getMagicNum().equals(magicNum)) {
      throw new IllegalArgumentException("不是此协议!!!");
    }
    int length = byteBuf.readInt();
    ByteBuf byteBuf1 = byteBuf.readBytes(length);
    return serialization.deserialize(byteBuf1.array(), BasePacket.class);
  }
}
