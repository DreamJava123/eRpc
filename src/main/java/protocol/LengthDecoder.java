package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 根据请求包长度拆包
 * 现在的协议,前四位 校验位, 这个整合到init里面 这个类后续废掉
 * Created by TOM
 * On 2019/9/30 0:10
 */
@Deprecated
public class LengthDecoder extends LengthFieldBasedFrameDecoder {

  public LengthDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
    super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
  }

  @Override
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    return super.decode(ctx, in);
  }
}
