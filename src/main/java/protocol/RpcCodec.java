package protocol;


import enums.SerializerAlogrithm;
import io.netty.buffer.ByteBuf;
import java.util.Objects;
import util.Serialization;

/**
 * 数据包装类
 * 校验位/4,请求命令/1,序列化算法/1,包长度/4,包数据,
 * Created by TOM
 * On 2019/9/29 23:12
 */
public class RpcCodec {


  private static final class RpcCodecHolder {

    private static final RpcCodec RPC_CODEC = new RpcCodec();
  }

  static RpcCodec getInstance() {
    return RpcCodecHolder.RPC_CODEC;
  }

  protected RpcCodec() {
  }

  public void encode(ByteBuf buffer, BasePacket basePacket, Serialization serialization) {
    byte[] serialize = serialization.serialize(basePacket);
    buffer.writeInt(serialization.getMagicNum());
    buffer.writeByte(basePacket.getCommand());
    buffer.writeByte(serialization.getSerializerAlogrithm());
    buffer.writeInt(serialize.length);
    buffer.writeBytes(serialize);
  }

  public Object decode(ByteBuf byteBuf, Class<? extends BasePacket> clazz) {
    int magicNum = byteBuf.readInt();
    byteBuf.skipBytes(1);
    byte serializerAlogrithm = byteBuf.readByte();
    if (SerializerAlogrithm.coverMagicNum(serializerAlogrithm) == null || !Objects
        .requireNonNull(SerializerAlogrithm.coverMagicNum(serializerAlogrithm)).getMagicNum().equals(magicNum)) {
      throw new IllegalArgumentException("不是此协议!!!");
    }
    SerializerAlogrithm serialization = SerializerAlogrithm.coverMagicNum(serializerAlogrithm);
    int length = byteBuf.readInt();
    ByteBuf byteBuf1 = byteBuf.readBytes(length);
    assert serialization != null;
    return serialization.getSerialization().deserialize(byteBuf1.array(), clazz);
  }
}
