import java.io.PrintStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
/**
  * 色子画板。
  * @author SoftStar,嘟嘟熊
  * @version 1.0
  */

public class DiceCanvas
    extends Canvas
    implements Runnable {
  /**
   * richMan实例
   */

  KMRichMan richMan;
  /**
   * 色子的图象
   */

  private Image[] diceImages;
  /**
   * 标题条图象
   */

  private Image barImage;
  /**
   * 线程
   */

  private volatile Thread thread;
  /**
   * 色子数值1
   */

  private int dictNum1;
  /**
   * 色子数值2
   */

  private int dictNum2;
  /**
   * 标志是移动的投的色子还是加钱投的色子（现金卡）
   */

  private boolean flagIsGoOrAddMoney;
  /**
   * 随机数
   */

  private Random random;
  /**
   * 构造一个对象
   * @param kmrichman richMan对象
   * @param flag  标志是移动的投的色子还是加钱投的色子（现金卡）
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
   * 开始线程
   */

  public void startThread() {
    if (thread == null) {
      thread = new Thread(this);
      thread.start();
    }
  }
  /**
   * 投色子结束
   */

  public void diceOver() {
    thread = null;
    if (flagIsGoOrAddMoney)
      richMan.diceAndSetDisplayToPlayForm(dictNum1 + dictNum2 + 2);
    else
      richMan.addDiceMoney(dictNum1 + dictNum2 + 2, flagIsGoOrAddMoney);
  }
  /**
   * 线程
   */

  public void run() {
    Thread thread = Thread.currentThread();
    int i = 0;
    do {
      if (thread != this.thread)
        break;
      try {
        Thread.sleep(50L);
        if (++i == 20) //变换了20次OVER
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
   * 绘制画板
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