import java.util.TimerTask;

 /**
  * 控制类。用于控制各个窗口的延迟显示，执行
  * @author SoftStar,嘟嘟熊
  * @version 1.0
  */

public class Controlor extends TimerTask
{
   /**
     *playCanvas实例
     */

  PlayCanvas playCanvas;
  /**
   * cardCanvas 实例
   */

  CardCanvas cardCanvas;
  /**
   * playMessageForm实例
   */

  PlayMessageForm playMessageForm;
  /**
   * 数值关于是电脑还是玩家
   */

  int num_ComputerActor_or_ManActor;
  /**
  * 创建实例
  */

  public Controlor(PlayCanvas playCanvas)
  {
    num_ComputerActor_or_ManActor = 0;
    cardCanvas = null;
    playMessageForm = null;
    this.playCanvas = playCanvas;
  }
  /**
   * 创建实例
   */

  public Controlor(PlayCanvas playCanvas, int i)
  {
    cardCanvas = null;
    playMessageForm = null;
    this.playCanvas = playCanvas;
    num_ComputerActor_or_ManActor = i;
  }
  /**
  * 创建实例
  */

  public Controlor(CardCanvas cardCanvas)
  {
    num_ComputerActor_or_ManActor = 0;
    playCanvas = null;
    playMessageForm = null;
    this.cardCanvas = cardCanvas;
  }
  /**
  * 创建实例
  */

  public Controlor(PlayMessageForm playMessageFormObject)
  {
    num_ComputerActor_or_ManActor = 0;
    playCanvas = null;
    cardCanvas = null;
    playMessageForm = playMessageFormObject;
  }
  /**
   * 线程执行
   */

  public void run()
  {
    if(playCanvas != null)
    {
      if(num_ComputerActor_or_ManActor != 0) // 1- 电脑 0 - 玩家
        playCanvas.dealWithComputerActorPlay();
      else
        playCanvas.play(false);
    } else
    if(cardCanvas != null)
      cardCanvas.useCard_ComputerActor();
    else
    if(playMessageForm != null)
      playMessageForm.autoSelect();
  }
}
