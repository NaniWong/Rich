import javax.microedition.lcdui.*;
/**
  * 角色资产状态窗口。
  * @author SoftStar,嘟嘟熊
  * @version 1.0
  */

public class PlayerStatusForm
    extends Form
    implements CommandListener {

  KMRichMan richManObject;
  /**
   * 构造一个对象
   * @param kmrichman richman实例
   * @param s 角色状态字符串
  */

  public PlayerStatusForm(KMRichMan kmrichman, String s) {
    super("游戏状态");
    richManObject = null;
    richManObject = kmrichman;
    append(s);
    setCommandListener(this);
    addCommand(new Command("返回", 8, 0));
  }
  /**
   * 处理按键
  */

  public void commandAction(Command command, Displayable displayable) {
    if (command.getLabel() == "返回")
      richManObject.setDisplayToPlayCanvas1();
  }
}