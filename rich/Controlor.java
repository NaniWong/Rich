import java.util.TimerTask;

 /**
  * �����ࡣ���ڿ��Ƹ������ڵ��ӳ���ʾ��ִ��
  * @author SoftStar,����
  * @version 1.0
  */

public class Controlor extends TimerTask
{
   /**
     *playCanvasʵ��
     */

  PlayCanvas playCanvas;
  /**
   * cardCanvas ʵ��
   */

  CardCanvas cardCanvas;
  /**
   * playMessageFormʵ��
   */

  PlayMessageForm playMessageForm;
  /**
   * ��ֵ�����ǵ��Ի������
   */

  int num_ComputerActor_or_ManActor;
  /**
  * ����ʵ��
  */

  public Controlor(PlayCanvas playCanvas)
  {
    num_ComputerActor_or_ManActor = 0;
    cardCanvas = null;
    playMessageForm = null;
    this.playCanvas = playCanvas;
  }
  /**
   * ����ʵ��
   */

  public Controlor(PlayCanvas playCanvas, int i)
  {
    cardCanvas = null;
    playMessageForm = null;
    this.playCanvas = playCanvas;
    num_ComputerActor_or_ManActor = i;
  }
  /**
  * ����ʵ��
  */

  public Controlor(CardCanvas cardCanvas)
  {
    num_ComputerActor_or_ManActor = 0;
    playCanvas = null;
    playMessageForm = null;
    this.cardCanvas = cardCanvas;
  }
  /**
  * ����ʵ��
  */

  public Controlor(PlayMessageForm playMessageFormObject)
  {
    num_ComputerActor_or_ManActor = 0;
    playCanvas = null;
    cardCanvas = null;
    playMessageForm = playMessageFormObject;
  }
  /**
   * �߳�ִ��
   */

  public void run()
  {
    if(playCanvas != null)
    {
      if(num_ComputerActor_or_ManActor != 0) // 1- ���� 0 - ���
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
