import java.util.Timer;
import javax.microedition.lcdui.*;

/**
 * ��Ƭ���塣
 * @author SoftStar,����
 * @version 1.0
 */

public class CardCanvas extends Canvas
    implements CommandListener
{

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

    KMRichMan richMan;
    /**
     * ��Ƭ
     */

    int cardIDs[];
    /**
     * ��Ƭ���
     */

    int cardIndex;
    /**
     * ��Ƭ����
     */

    int kardCount;
    /**
     * ��Ƭ����
     */

    String kardNames[] = {
        "͵����", "������", "ǿռ��", "˯�߿�", "���￨", "���޿�", "��ʹ��", "�ֽ�", "����", "˥��"
    };
    /**
     * ��ƬͼƬ
     */

    Image cardImages[];
    /**
     * ������ƬͼƬ
     */

    Image cardImage;
    /**
     * ������ͼƬ
     */

    Image barImage;
    /**
     * ����һ������
     * @param kmrichMan richman����
     * @param cardIDs ��ƬID
     * @param cardIndex ��Ƭ���
     * @param isComputerActor �Ƿ��ǵ��Խ�ɫ
     */

    public CardCanvas(KMRichMan kmrichman, int cardIDs[], int cardIndex, boolean isComputerActor)
    {
        kardCount = 0;
        cardImages = null;
        cardImage = null;
        barImage = null;
        richMan = kmrichman;
        this.cardIDs = cardIDs;
        this.cardIndex = cardIndex;
        canvasWidth = getWidth();
        canvasHeight = getHeight();
        if(cardIDs != null)
        {
          for(int j = 0; j < 5; j++)
            if(cardIDs[j] != 16)
              kardCount++;
        }
        setCommandListener(this);
        try
        {
          barImage = Image.createImage("/res/image/barbw.png");
        }
        catch(Exception exception) { }

        if(cardIDs != null) //��ʾ�鿴��Ƭ
        {
          if (!isComputerActor) {
            addCommand(new Command("ʹ��", 8, 1));
            addCommand(new Command("����", 8, 2));
            addCommand(new Command("����", 8, 0));
          }
          cardImages = new Image[cardIDs.length];
          try
          {
            for(int i = 0; i < kardCount + 1; i++)
              cardImages[i] = Image.createImage(String.valueOf((new StringBuffer("/res/image/card")).append(cardIDs[i]).append(".png")));
          }
          catch(Exception exception1) { }
        }
        else //��ÿ�Ƭ�ȵ�
        {
          if (!isComputerActor) {
            addCommand(new Command("����", 8, 0));
          }
          try
          {
            cardImage = Image.createImage(String.valueOf((new StringBuffer("/res/image/card")).append(cardIndex).append(".png")));
          }
          catch(Exception exception2)
          {
          }
        }
        // ����ǵ��Խ�ɫ�����Զ�
        if(isComputerActor)
          (new Timer()).schedule(new Controlor(this), 2000L);
      }
      /**
       * ʹ�û���ʾ��Ƭ
       */

      void useCard_ComputerActor()
      {
        if(cardIDs != null)
        {
          richMan.useCard(cardIDs[cardIndex]);
          setNull();
        } else
        {
          setNull();
          richMan.confirmMessage(); // ���һ�ſ�Ƭ��͵���ߵ����Ŵ�������
        }
      }
      /**
       * ������
       * @param key ����ֵ
       */

    public void keyPressed(int key)
    {
      if(cardIDs == null)
        return;
      switch(getGameAction(key))
      {
        case 6:
        case 5: //��
          cardIndex++;
          if(cardIndex == kardCount)
            cardIndex = 0;
          repaint();
          break;
        case 1:
        case 2: //��
          cardIndex--;
          if(cardIndex == -1)
            cardIndex = kardCount - 1;
          repaint();
          break;
      }
    }
    /**
     * ����COOMMAND
     * @param command ����
     * @param displayable displayable
     */

    public void commandAction(Command command, Displayable displayable)
    {
      if(command.getLabel() == "ʹ��")
        richMan.useCard(cardIDs[cardIndex]);
      if(command.getLabel() == "����")
        richMan.showCardDetail(cardIDs, cardIndex);
      if(command.getLabel() == "����" && cardIDs == null)
        richMan.confirmMessage(); //�������ڵĳ������ߵ��������
      if(command.getLabel() == "����")
        richMan.setDisplayToPlayCanvas1();
    }
    /**
     * ��ձ���
     */

    private void setNull()
    {
      cardIDs = null;
      cardImages = null;
      cardImage = null;
      barImage = null;
    }
    /**
     * ���ƻ���
     * @param g ���
     */

    protected void paint(Graphics g)
    {
      //��һ��ϣ��Źֵģ������ǻ��ƿ�Ƭ���ܺ���⡣
      //�����������ɡ�
      char c = 'e';
      byte byte0 = 80;
      g.setColor(0);
      g.fillRect(0, 0, canvasWidth, canvasHeight);
      g.setClip((canvasWidth - c) / 2, (canvasHeight - byte0) / 2, c, byte0);
      g.translate((canvasWidth - c) / 2, (canvasHeight - byte0) / 2);
      g.setColor(255, 255, 255);
      g.fillRect(0, 0, c, byte0);
      g.setColor(0);
      if(cardIDs == null)
      {
        g.drawImage(cardImage, c / 2, byte0 / 2, 3);
        g.drawString(kardNames[cardIndex], c / 2, byte0 / 2 + 25, 0x10 | 0x1);
      } else
      if(cardImages[cardIndex] != null)
      {
        g.drawImage(cardImages[cardIndex], c / 2, byte0 / 2, 3);
        g.drawString(kardNames[cardIDs[cardIndex]], c / 2, byte0 / 2 + 25, 0x10 | 0x1);
      }
      g.drawImage(barImage, 0, 0, 20);
    }
}
