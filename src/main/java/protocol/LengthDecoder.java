package protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 根据请求包长度拆包
 * 现在的协议,前四位 校验位
 * Created by TOM
 * On 2019/9/30 0:10
 */
public class LengthDecoder extends LengthFieldBasedFrameDecoder {

  private Class clazz;

  public LengthDecoder(Class<? extends BasePacket> clazz) {
    super(Integer.MAX_VALUE, 6, 4);
    this.clazz = clazz;
  }

  @Override
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) {
    ByteBuf decode = null;
    Object rpcRequest = null;
    try {
      decode = (ByteBuf) super.decode(ctx, in);
      rpcRequest = RpcCodec.getInstance().decode(decode, clazz);
    } catch (Exception e) {
      System.out.println(ExceptionUtils.getStackTrace(e));
    } finally {
      if (decode != null) {
        //释放
        decode.release();
      }
    }
    return rpcRequest;
  }
}
