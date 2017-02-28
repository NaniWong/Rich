import java.util.TimerTask;
/**
 *   开始画板定时器。
  * @author SoftStar,嘟嘟熊
  * @version 1.0
  */

public class OpenCanvasTimerTask
    extends TimerTask {
  /**
 * 开始画板
 */

  OpenCanvas openCanvas;
  /**
   * richman实例
   */

  KMRichMan richMan;
  /**
   *	构造一个对象
   */

  public OpenCanvasTimerTask(OpenCanvas openCanvas, KMRichMan richMan) {
    this.openCanvas = openCanvas;
    this.richMan = richMan;
  }
  /**
   * 定时器运行
   */

  public void run() {
    richMan.createPlayCanvas();
    openCanvas.paintOpenCanvas();
  }
}