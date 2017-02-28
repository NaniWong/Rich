import javax.microedition.lcdui.*;
/**
  * 游戏菜单。
  * @author SoftStar,嘟嘟熊
  * @version 1.0
  */

public class GameMenu
    extends List
    implements CommandListener {
  /**
   * richman实例
   */

  private KMRichMan richManObject;
  /**
   * 角色1的FACE
   */

  private Image playerFaceImage1;
  /**
   * 角色2的FACE
   */

  private Image playerFaceImage2;
  /**
   * 角色3的FACE
   */

  private Image playerFaceImage3;
  /**
   * 构造一个对象
   */

  public GameMenu(KMRichMan kmrichman) {
    super("菜单[大富翁1.2]", 3);
    richManObject = null;
    playerFaceImage1 = null;
    playerFaceImage2 = null;
    playerFaceImage3 = null;
    richManObject = kmrichman;
    try {
      playerFaceImage1 = Image.createImage("/res/image/ph0.png");
      playerFaceImage2 = Image.createImage("/res/image/ph1.png");
      playerFaceImage3 = Image.createImage("/res/image/ph2.png");
    }
    catch (Exception exception) {}
    append("继续上次游戏", null);
    append("新游戏", null);
    append("高分榜", null);
    append("帮助", null);
    append("退出", null);
    setCommandListener(this);
  }
  /**
   * 处理按键
   */

  public void commandAction(Command command, Displayable displayable) {
    if (size() == 5)
      switch (getSelectedIndex()) {
        case 0:
          richManObject.continuePlay(3); // 接着上次游戏玩
          break;

        case 1:
          selectPlayer();
          break;

        case 2:
          richManObject.showHighScore();
          break;

        case 3:
          richManObject.setDisplayToHelpForm();
          break;

        case 4:
          KMRichMan.exitGame();
          break;
      }
    else // 选择角色开始新游戏
      richManObject.continuePlay(getSelectedIndex());
  }
  /**
   * 构造选择角色菜单
   */

  private void selectPlayer() {
    for (int i = 0; i < 5; i++)
      delete(0);

    setTitle("选择一个角色");
    append("孙小美", playerFaceImage1);
    append("阿土仔", playerFaceImage2);
    append("钱夫人", playerFaceImage3);
  }
}