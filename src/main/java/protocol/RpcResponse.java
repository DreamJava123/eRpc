package protocol;

import protocol.command.Command;

/**
 * Created by TOM
 * On 2019/9/29 22:12
 */
public class RpcResponse extends BasePacket {

  @Override
  public Byte getCommand() {
    return Command.RESPONSE.getCommand();
  }
}
