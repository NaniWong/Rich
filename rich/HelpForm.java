import javax.microedition.lcdui.*;
/**
  * 帮助菜单。
  * @author SoftStar,嘟嘟熊
  * @version 1.0
  */

public class HelpForm
    extends Form
    implements CommandListener {

  KMRichMan richManObject;
  /**
   * 构造一个对象
   */

  public HelpForm(KMRichMan kmrichman) {
    super("帮助");
    richManObject = kmrichman;
    append("       嘟嘟熊\n" +
           "Email:david_nj@hotmail.com\n" +
           "QQ:122173984\n" +
           "说明:本游戏是SoftStar公司出品.Siemens版由zha移植.此版本是'嘟嘟熊'在zha的Simens版本上修改.\n" +
           "记得上初中的时候,就开始玩SoftStar大富翁." +
           "现在还记忆犹新!");
    setCommandListener(this);
    addCommand(new Command("返回", 8, 0));
  }
  /**
   * 处理按键
   */

  public void commandAction(Command command, Displayable displayable) {
    if (command.getLabel() == "返回") {
      richManObject.setDisplayToGameMenu();
    }
  }
}
