package server;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import protocol.LengthDecoder;
import protocol.RpcEncode;
import protocol.RpcRequest;
import registry.ZookeeperRegistry;

/**
 * 服务端
 * Created by TOM
 * On 2019/9/26 1:40
 */
public class Server {

  private static ExecutorService executors;
  private EventLoopGroup masterLoopGroup;
  private EventLoopGroup slaveLoopGroup;
  private String serverHost;
  private Integer serverPort;
  private ZookeeperRegistry zookeeperRegistry;
  private ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
  private final static int threadNum = Runtime.getRuntime().availableProcessors();
  private final static String maoHao = ":";

  public Server(String serverIp) {
    this.checkIp(serverIp);
    start();
  }

  public Server(String serverIp, ZookeeperRegistry zookeeperRegistry) {
    this.checkIp(serverIp);
    this.zookeeperRegistry = zookeeperRegistry;
    start();
  }

  private void checkIp(String serverIp) {
    if (!serverIp.contains(maoHao)) {
      throw new IllegalArgumentException("服务端传入地址不对");
    }
    String[] split = serverIp.split(maoHao);
    if (split.length > 2 && split[0].length() < split[1].length()) {
      throw new IllegalArgumentException("服务端传入地址异常");
    }
    this.serverHost = split[0];
    this.serverPort = Integer.valueOf(split[1]);
  }

  /**
   * 处理业务
   */
  public static void subimt(Runnable runnable) {
    synchronized (Server.class) {
      if (executors == null) {
        executors = new ThreadPoolExecutor(threadNum, threadNum * 2, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
            r -> new Thread(r, "E-RPC"));
      }
    }
    executors.submit(runnable);
  }

  /**
   * 服务缓存
   */
  public void serverCache(String interfaceName, Object object) {
    Object o = cache.putIfAbsent(interfaceName, object);
    if (o != null) {
      System.out.println("重复添加的K,V为" + interfaceName + ":" + JSON.toJSONString(object));
    }
  }

  private void start() {
    if (masterLoopGroup == null && slaveLoopGroup == null) {
      masterLoopGroup = new NioEventLoopGroup();
      slaveLoopGroup = new NioEventLoopGroup();
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap.group(masterLoopGroup, slaveLoopGroup)
          .option(ChannelOption.SO_BACKLOG, 1024)
          .childOption(ChannelOption.SO_KEEPALIVE, true)
          //减少网络交互
          .childOption(ChannelOption.TCP_NODELAY, true)
          .childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
              ch.pipeline().addLast(new LengthDecoder(RpcRequest.class));
              ch.pipeline().addLast(new ServerHandler(cache));
              ch.pipeline().addLast(new RpcEncode());
            }
          });
      this.bind(serverBootstrap);
      if (this.zookeeperRegistry != null) {
        zookeeperRegistry.serverRegistry(this.serverHost, this.serverPort);
      }
    }
  }

  private void bind(ServerBootstrap serverBootstrap) {
    serverBootstrap.bind(this.serverHost, this.serverPort).addListener(future -> {
      if (future.isSuccess()) {
        System.out.println("start--");
      } else {
        System.out.println("绑定失败现在重新绑定,之前的端口号为" + this.serverPort);
        this.serverPort++;
        System.out.println("现在的为 " + this.serverPort);
        bind(serverBootstrap);
      }
    });
  }
}
