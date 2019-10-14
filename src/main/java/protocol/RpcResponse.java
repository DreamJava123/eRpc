package protocol;

import lombok.Getter;
import lombok.Setter;
import protocol.command.Command;

/**
 * Created by TOM
 * On 2019/9/29 22:12
 */
@Getter
@Setter
public class RpcResponse extends BasePacket {

  private Long requestId;
  private Object result;
  private Integer code;
  private Integer serializerCode;

  @Override
  public Byte getCommand() {
    return Command.RESPONSE.getCommand();
  }
}
