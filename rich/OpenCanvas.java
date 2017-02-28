import java.util.Timer;
import javax.microedition.lcdui.*;
import com.siemens.mp.io.*;
/**
  * ��ʼ���壨��Ϸ��ʼ����ʾ��FLASHͼƬ����
  * @author SoftStar,����
  * @version 1.0
  */


public class OpenCanvas
    extends Canvas {
  /**
   * ������
   */

  public int canvasWidth;
  /**
   * ����߶�
   */

  public int canvasHeight;
  /**
   * richManʵ��
   */

  private final KMRichMan richManObject;
  /**
   * ��ʱ��
   */

  private Timer timer;
  /**
   * Ҫ���Ƶ�FLASHͼƬID
   */

  private int paintImageID;
  /**
   * FLASHͼƬ
   */

  private Image image[];
  /**
   * �����ӵ�����������
  */

  com.siemens.mp.game.MelodyComposer composer;
  /**
   * ����һ������
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

    // ����
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

      composer.appendNote(composer.TONE_REPEAT, 2); // ����2��

      composer.getMelody().play(); //��ʼ����

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    timer = new Timer();
    timer.schedule(new OpenCanvasTimerTask(this,richManObject), 10L);
  }
  /**
   * �л�FLASHͼƬ
   */

  public void paintOpenCanvas() {
    paintImageID++;
    image[paintImageID - 1] = null;
    repaint();
  }
  /**
   * ���ƻ���
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
   * ������
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
