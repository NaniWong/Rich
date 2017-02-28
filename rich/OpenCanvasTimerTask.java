import java.util.TimerTask;
/**
 *   ��ʼ���嶨ʱ����
  * @author SoftStar,����
  * @version 1.0
  */

public class OpenCanvasTimerTask
    extends TimerTask {
  /**
 * ��ʼ����
 */

  OpenCanvas openCanvas;
  /**
   * richmanʵ��
   */

  KMRichMan richMan;
  /**
   *	����һ������
   */

  public OpenCanvasTimerTask(OpenCanvas openCanvas, KMRichMan richMan) {
    this.openCanvas = openCanvas;
    this.richMan = richMan;
  }
  /**
   * ��ʱ������
   */

  public void run() {
    richMan.createPlayCanvas();
    openCanvas.paintOpenCanvas();
  }
}