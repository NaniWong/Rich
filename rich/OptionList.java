import javax.microedition.lcdui.*;
/**
  * 选项菜单。
  * @author SoftStar,嘟嘟熊
  * @version 1.0
  */


public class OptionList
    extends List
    implements CommandListener {
  /**
   *	richMan实例
   */

  private KMRichMan richMan;
  /**
   * playCanvas 实例
   */

  private PlayCanvas playCanvas;
  /**
   * 构造一个对象
   * @param kmrichman richman实例
   * @param playCanvas playCnavas实例
   */

  public OptionList(KMRichMan kmrichman,PlayCanvas playCanvas) {
    super("选项菜单", 3);
    this.richMan = kmrichman;
    this.playCanvas = playCanvas;
    append("资产状况", null);
    append("股市行情", null);
    append("返回游戏", null);
    setCommandListener(this);
  }
  /**
   * 处理按键
   * @param command 不解释了
   * @param displayalbe 不解释了
   */

  public void commandAction(Command command, Displayable displayable) {
    switch (getSelectedIndex()) {
      case 0:
        int[] stock = new int[3];
        // 计算股票价格
        for (int i = 0; i < 3; i++) {
          stock[i] = 0;
          for (int j = 0; j < 4; j++) {
            stock[i] += playCanvas.stock_price[j] * playCanvas.player_stock[i][j][0];
          }
        }
        richMan.showPlayerStatus(playCanvas.player_money[0],
                                 playCanvas.player_money[1],
                                 playCanvas.player_money[2],
                                 stock[0],
                                 stock[1],
                                 stock[2],
                                 playCanvas.countPlayerGround(1),
                                 playCanvas.countPlayerGround(10),
                                 playCanvas.countPlayerGround(100),
                                 playCanvas.countPlayerGround(2) + playCanvas.countPlayerGround(3),
                                 playCanvas.countPlayerGround(20) + playCanvas.countPlayerGround(30),
                                 playCanvas.countPlayerGround(200) + playCanvas.countPlayerGround(300),
                                 playCanvas.countPlayerGround(4),
                                 playCanvas.countPlayerGround(40),
                                 playCanvas.countPlayerGround(400),
                                 playCanvas.player_sequence);

        break;
      case 1:
        richMan.setDisplayToStockList();
        break;
      case 2:
        richMan.setDisplayToPlayCanvas1();
        break;
    }
  }
}
