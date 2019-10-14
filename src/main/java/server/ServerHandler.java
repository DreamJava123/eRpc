package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import protocol.RpcRequest;
import protocol.RpcResponse;

/**
 * Created by TOM
 * On 2019/10/14 11:47
 */
public class ServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

  private ConcurrentHashMap<String, Object> handlerMap;

  ServerHandler(ConcurrentHashMap<String, Object> handlerMap) {
    this.handlerMap = handlerMap;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) {
    Server.subimt(() -> {
      RpcResponse rpcResponse = new RpcResponse();
      rpcResponse.setRequestId(msg.getRequestId());
      Object returnObject = this.handlerInvoke(msg);
      rpcResponse.setResult(returnObject);
      ctx.writeAndFlush(rpcResponse).addListener(future -> {
        if (future.isSuccess()) {
          System.out.println("响应成功" + msg.getRequestId());
        }
      });
    });
  }

  private Object handlerInvoke(RpcRequest msg) {
    System.out.println(msg.toString());
    Object bean = handlerMap.get(msg.getInterfaceName());
    Class<?> clazz = bean.getClass();
    Method method;
    try {
      method = clazz.getMethod(msg.getMethodName(), msg.getParameterTypes());
      method.setAccessible(true);
      return method.invoke(bean, msg.getParameters());
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      System.out.println(ExceptionUtils.getStackTrace(e));
      return ExceptionUtils.getStackTrace(e);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.out.println(ExceptionUtils.getStackTrace(cause));
    ctx.close();
  }
}
