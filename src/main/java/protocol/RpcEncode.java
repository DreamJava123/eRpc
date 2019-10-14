package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import util.ProtoSerializationUtil;

/**
 * Created by TOM
 * On 2019/9/29 22:13
 */
public class RpcEncode extends MessageToByteEncoder<RpcResponse> {

  @Override
  protected void encode(ChannelHandlerContext ctx, RpcResponse msg, ByteBuf out) {
    //后续可以改成灵活的
    RpcCodec.getInstance().encode(out, msg, ProtoSerializationUtil.getInstance());
  }
}
