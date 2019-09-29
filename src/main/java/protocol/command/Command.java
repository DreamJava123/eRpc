package protocol.command;

/**
 * Created by TOM
 * On 2019/9/30 0:42
 */
public enum Command {
  REQUEST((byte) 0),
  RESPONSE((byte) 1);

  private Byte command;

  Command(Byte command) {
    this.command = command;
  }

  public Byte getCommand() {
    return command;
  }

  public void setCommand(Byte command) {
    this.command = command;
  }
}
