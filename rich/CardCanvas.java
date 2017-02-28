import java.util.Timer;
import javax.microedition.lcdui.*;

/**
 * 卡片画板。
 * @author SoftStar,嘟嘟熊
 * @version 1.0
 */

public class CardCanvas extends Canvas
    implements CommandListener
{

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

    KMRichMan richMan;
    /**
     * 卡片
     */

    int cardIDs[];
    /**
     * 卡片序号
     */

    int cardIndex;
    /**
     * 卡片记数
     */

    int kardCount;
    /**
     * 卡片名称
     */

    String kardNames[] = {
        "偷盗卡", "均富卡", "强占卡", "睡眠卡", "免罪卡", "怪兽卡", "天使卡", "现金卡", "财神卡", "衰神卡"
    };
    /**
     * 卡片图片
     */

    Image cardImages[];
    /**
     * 单个卡片图片
     */

    Image cardImage;
    /**
     * 标题条图片
     */

    Image barImage;
    /**
     * 构造一个对象
     * @param kmrichMan richman对象
     * @param cardIDs 卡片ID
     * @param cardIndex 卡片序号
     * @param isComputerActor 是否是电脑角色
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

        if(cardIDs != null) //表示查看卡片
        {
          if (!isComputerActor) {
            addCommand(new Command("使用", 8, 1));
            addCommand(new Command("详情", 8, 2));
            addCommand(new Command("返回", 8, 0));
          }
          cardImages = new Image[cardIDs.length];
          try
          {
            for(int i = 0; i < kardCount + 1; i++)
              cardImages[i] = Image.createImage(String.valueOf((new StringBuffer("/res/image/card")).append(cardIDs[i]).append(".png")));
          }
          catch(Exception exception1) { }
        }
        else //获得卡片等等
        {
          if (!isComputerActor) {
            addCommand(new Command("返回", 8, 0));
          }
          try
          {
            cardImage = Image.createImage(String.valueOf((new StringBuffer("/res/image/card")).append(cardIndex).append(".png")));
          }
          catch(Exception exception2)
          {
          }
        }
        // 如果是电脑角色，则自动
        if(isComputerActor)
          (new Timer()).schedule(new Controlor(this), 2000L);
      }
      /**
       * 使用或显示卡片
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
          richMan.confirmMessage(); // 获得一张卡片（偷或走到？号处）返回
        }
      }
      /**
       * 处理按键
       * @param key 按键值
       */

    public void keyPressed(int key)
    {
      if(cardIDs == null)
        return;
      switch(getGameAction(key))
      {
        case 6:
        case 5: //下
          cardIndex++;
          if(cardIndex == kardCount)
            cardIndex = 0;
          repaint();
          break;
        case 1:
        case 2: //上
          cardIndex--;
          if(cardIndex == -1)
            cardIndex = kardCount - 1;
          repaint();
          break;
      }
    }
    /**
     * 处理COOMMAND
     * @param command 按键
     * @param displayable displayable
     */

    public void commandAction(Command command, Displayable displayable)
    {
      if(command.getLabel() == "使用")
        richMan.useCard(cardIDs[cardIndex]);
      if(command.getLabel() == "详情")
        richMan.showCardDetail(cardIDs, cardIndex);
      if(command.getLabel() == "返回" && cardIDs == null)
        richMan.confirmMessage(); //好象现在的程序不能走到这条语句
      if(command.getLabel() == "返回")
        richMan.setDisplayToPlayCanvas1();
    }
    /**
     * 清空变量
     */

    private void setNull()
    {
      cardIDs = null;
      cardImages = null;
      cardImage = null;
      barImage = null;
    }
    /**
     * 绘制画板
     * @param g 句柄
     */

    protected void paint(Graphics g)
    {
      //这一段希奇古怪的，反正是绘制卡片，很好理解。
      //就留着这样吧。
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
