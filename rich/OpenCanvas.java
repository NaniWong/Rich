import java.util.Timer;
import javax.microedition.lcdui.*;
import com.siemens.mp.io.*;
/**
  * 开始画板（游戏开始的显示的FLASH图片）。
  * @author SoftStar,嘟嘟熊
  * @version 1.0
  */


public class OpenCanvas
    extends Canvas {
  /**
   * 画板宽度
   */

  public int canvasWidth;
  /**
   * 画板高度
   */

  public int canvasHeight;
  /**
   * richMan实例
   */

  private final KMRichMan richManObject;
  /**
   * 定时器
   */

  private Timer timer;
  /**
   * 要绘制的FLASH图片ID
   */

  private int paintImageID;
  /**
   * FLASH图片
   */

  private Image image[];
  /**
   * 西门子的乐谱生成器
  */

  com.siemens.mp.game.MelodyComposer composer;
  /**
   * 构造一个对象
   */

  public OpenCanvas(KMRichMan kmrichman) {
    image = null;
    timer = null;
    paintImageID = 0;
    richManObject = kmrichman;
    image = new Image[2];
    canvasWidth = getWidth();
    canvasHeight = getHeight();
    try {
      image[0] = Image.createImage("/res/image/logobw2.png");
      image[1] = Image.createImage("/res/image/logobw.png");
    }
    catch (Exception exception) {}

    // 音乐
    composer = new com.siemens.mp.game.MelodyComposer();
    composer.setBPM(120);
    try {

      composer.appendNote(composer.TONE_G1, composer.TONELENGTH_1_8);
      composer.appendNote(composer.TONE_G1, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_H1, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_C2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_1_8);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);

      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_1_8);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_E2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_C2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G1, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G1, composer.TONELENGTH_1_8);
      composer.appendNote(composer.TONE_G1, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G1, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_8);

      composer.appendNote(composer.TONE_G1, composer.TONELENGTH_1_8);
      composer.appendNote(composer.TONE_G1, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_H1, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_C2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_8);

      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_1_8);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_E2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_C2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G1, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_A1, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_A1, composer.TONELENGTH_DOTTED_1_32);
      composer.appendNote(composer.TONE_H1, composer.TONELENGTH_DOTTED_1_32);
      composer.appendNote(composer.TONE_A1, composer.TONELENGTH_DOTTED_1_32);
      composer.appendNote(composer.TONE_H1, composer.TONELENGTH_DOTTED_1_32);
      composer.appendNote(composer.TONE_A1, composer.TONELENGTH_DOTTED_1_32);
      composer.appendNote(composer.TONE_H1, composer.TONELENGTH_DOTTED_1_32);
      composer.appendNote(composer.TONE_A1, composer.TONELENGTH_DOTTED_1_32);
      composer.appendNote(composer.TONE_H1, composer.TONELENGTH_DOTTED_1_32);

      composer.appendNote(composer.TONE_REPEAT, 2); // 奏乐2次

      composer.getMelody().play(); //开始奏乐

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    timer = new Timer();
    timer.schedule(new OpenCanvasTimerTask(this,richManObject), 10L);
  }
  /**
   * 切换FLASH图片
   */

  public void paintOpenCanvas() {
    paintImageID++;
    image[paintImageID - 1] = null;
    repaint();
  }
  /**
   * 绘制画板
   */

  public void paint(Graphics g) {
    if (image[paintImageID] != null) {
      g.drawImage(image[paintImageID], 0, 0, 20);
    }
    if (paintImageID == 1 && timer != null) {
      timer.cancel();
      timer = null;
    }
  }
  /**
   * 处理按键
   */

  public void keyPressed(int i) {
    if (paintImageID != 1) {
      return;
    }
    else {
      richManObject.setDisplayToGameMenu();
      return;
    }
  }
}
