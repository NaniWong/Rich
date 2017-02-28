import javax.microedition.lcdui.*;
/**
  * ѡ��˵���
  * @author SoftStar,����
  * @version 1.0
  */


public class OptionList
    extends List
    implements CommandListener {
  /**
   *	richManʵ��
   */

  private KMRichMan richMan;
  /**
   * playCanvas ʵ��
   */

  private PlayCanvas playCanvas;
  /**
   * ����һ������
   * @param kmrichman richmanʵ��
   * @param playCanvas playCnavasʵ��
   */

  public OptionList(KMRichMan kmrichman,PlayCanvas playCanvas) {
    super("ѡ��˵�", 3);
    this.richMan = kmrichman;
    this.playCanvas = playCanvas;
    append("�ʲ�״��", null);
    append("��������", null);
    append("������Ϸ", null);
    setCommandListener(this);
  }
  /**
   * ������
   * @param command ��������
   * @param displayalbe ��������
   */

  public void commandAction(Command command, Displayable displayable) {
    switch (getSelectedIndex()) {
      case 0:
        int[] stock = new int[3];
        // �����Ʊ�۸�
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
