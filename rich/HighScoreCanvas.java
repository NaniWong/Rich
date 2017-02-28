import javax.microedition.lcdui.*;
/**
  * ��߷ִ��ڡ�
  * @author SoftStar,����
  * @version 1.0
  */

public class HighScoreCanvas
    extends Canvas
    implements CommandListener {

  public int canvasWidth;
  public int canvasHeight;
  private KMRichMan richManObject;
  private String playersData[][];
  private Image BackgroudImage;
  /**
    * ����һ������
    * @param kmrichman richmanʵ��
    * @param playData[][] ������ֺ���߷֣���С������
    */

  public HighScoreCanvas(KMRichMan kmrichman, String playerData[][]) {
    richManObject = null;
    playersData = null;
    BackgroudImage = null;
    playersData = null;
    richManObject = kmrichman;
    playersData = playerData;
    canvasWidth = getWidth();
    canvasHeight = getHeight();
    try {
      BackgroudImage = Image.createImage("/res/image/sbback.png");
    }
    catch (Exception exception) {}
    addCommand(new Command("����", 8, 0));
    setCommandListener(this);
  }
  /**
   *	���ƻ���
   */

  protected void paint(Graphics g) {
    for (int i = 0; i <= canvasWidth >> 4; i++) {
      for (int j = 0; j <= canvasHeight >> 4; j++)
        g.drawImage(BackgroudImage, i * 32, j * 32, 20);
    }

    g.setColor(0, 0, 0);
    g.drawString("No.", 10, 16, 20);
    g.drawString("1", 10, 32, 20);
    g.drawString("2", 10, 48, 20);
    g.drawString("3", 10, 64, 20);
    g.drawString("����", 30, 16, 20);
    g.drawString("����", 70, 16, 20);
    g.drawString(playersData[0][0], 30, 32, 20);
    g.drawString(playersData[1][0], 30, 48, 20);
    g.drawString(playersData[2][0], 30, 64, 20);
    g.drawString(playersData[0][1], 70, 32, 20);
    g.drawString(playersData[1][1], 70, 48, 20);
    g.drawString(playersData[2][1], 70, 64, 20);
    try {
      g.drawImage(Image.createImage("/res/image/barbw.png"), 0, 0, 20);
    }
    catch (Exception exception) {}
  }
  /**
   *	������
   */

  public void commandAction(Command command, Displayable displayable) {
    if (command.getLabel() == "����")
      richManObject.setDisplayToGameMenu();
  }
}