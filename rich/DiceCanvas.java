import java.io.PrintStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
/**
  * ɫ�ӻ��塣
  * @author SoftStar,����
  * @version 1.0
  */

public class DiceCanvas
    extends Canvas
    implements Runnable {
  /**
   * richManʵ��
   */

  KMRichMan richMan;
  /**
   * ɫ�ӵ�ͼ��
   */

  private Image[] diceImages;
  /**
   * ������ͼ��
   */

  private Image barImage;
  /**
   * �߳�
   */

  private volatile Thread thread;
  /**
   * ɫ����ֵ1
   */

  private int dictNum1;
  /**
   * ɫ����ֵ2
   */

  private int dictNum2;
  /**
   * ��־���ƶ���Ͷ��ɫ�ӻ��Ǽ�ǮͶ��ɫ�ӣ��ֽ𿨣�
   */

  private boolean flagIsGoOrAddMoney;
  /**
   * �����
   */

  private Random random;
  /**
   * ����һ������
   * @param kmrichman richMan����
   * @param flag  ��־���ƶ���Ͷ��ɫ�ӻ��Ǽ�ǮͶ��ɫ�ӣ��ֽ𿨣�
   */


  public DiceCanvas(KMRichMan kmrichman, boolean flag) {

    thread = null;
    dictNum1 = 0;
    dictNum2 = 0;
    richMan = kmrichman;
    flagIsGoOrAddMoney = flag;
    random = new Random();
    diceImages = new Image[6];

    try {
      barImage = Image.createImage("/res/image/barbw.png");
    }
    catch (Exception exception) {}

    try {
      Image diceImage = com.siemens.mp.ui.Image.createImageWithoutScaling(
          "/res/image/dice.png");
      for (int i = 0; i < 6; i++) {
        Image image = Image.createImage(20, 20);
        Graphics g = image.getGraphics();
        g.drawImage(diceImage, 0, -20 * i, 20);
        diceImages[i] = image;
      }
      diceImage = null;
    }
    catch (Exception exception) {}

    startThread();
  }
  /**
   * ��ʼ�߳�
   */

  public void startThread() {
    if (thread == null) {
      thread = new Thread(this);
      thread.start();
    }
  }
  /**
   * Ͷɫ�ӽ���
   */

  public void diceOver() {
    thread = null;
    if (flagIsGoOrAddMoney)
      richMan.diceAndSetDisplayToPlayForm(dictNum1 + dictNum2 + 2);
    else
      richMan.addDiceMoney(dictNum1 + dictNum2 + 2, flagIsGoOrAddMoney);
  }
  /**
   * �߳�
   */

  public void run() {
    Thread thread = Thread.currentThread();
    int i = 0;
    do {
      if (thread != this.thread)
        break;
      try {
        Thread.sleep(50L);
        if (++i == 20) //�任��20��OVER
          diceOver();
        else
        if (i < 7) {
          dictNum1 = Math.abs(random.nextInt() % 6);
          dictNum2 = Math.abs(random.nextInt() % 6);
          repaint();
        }
      }
      catch (Exception exception) {}
    }
    while (true);
  }
  /**
   * ���ƻ���
   */

  protected void paint(Graphics g) {
    g.setColor(255, 255, 255);
    g.fillRect(0, 0, 101, 80);
    g.drawImage(diceImages[dictNum1], 28, 40, 6);
    g.drawImage(diceImages[dictNum2], 52, 40, 6);
    try {
      g.drawImage(barImage, 0, 0, 20);
    }
    catch (Exception exception) {
    }
  }
}