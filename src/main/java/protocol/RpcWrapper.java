package protocol;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import util.Serialization;

/**
 * 数据包装类
 * 校验位/4,链路id/8,包长度/4,数据/?,方法名,以及参数数量?
 * Created by TOM
 * On 2019/9/29 23:12
 */
public class RpcWrapper<T> {

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

  public BasePacket decode(ByteBuf byteBuf) {
    //魔数校验在拆包器那层做 //有点乱了
    byteBuf.skipBytes(4);

    return null;
  }
}
