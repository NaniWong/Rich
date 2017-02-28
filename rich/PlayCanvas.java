
import java.io.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
/**
 * 游戏画板。这是这个游戏最关键的一个类
 * @author SoftStar,嘟嘟熊
 * @version 1.0
 */

public class PlayCanvas extends Canvas
    implements Runnable
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
  private KMRichMan richMan;
  /**
   * 地图第1部分
   */
  private Image mapImage1;
  /**
   * 地图第2部分
   */
  private Image mapImage2;
  /**
   * 游戏控制面板
   */
  private Image gamePanelImage;
  /**
   * 线程
   */
  private volatile Thread thread;
  /**
   * 控制面板的选择序号（1-色子，2-卡片，3-其他（玩家信息，股票信息）4-系统
   */

  private static int choiceIndex = 0;
  /**
   * 游戏角色要走的步数
   */

  private static int steps = 0;
  /**
   * 游戏进行的轮数
   */

  private static int turnCount = 0;
  /**
   * 游戏状态（0-进行，2-胜利，3-失败）
   */

  private static int gameStatusGoOrWinOrFail = 0;
  /**
   * 角色特殊状态（10--在公园处，20-在监狱里，30-在睡眠中（睡眠卡）
   */

  private static int status_Park10_Prinson20_Sleep30 = 0;
  /**
   * 现在正在进行的角色
   */

  public static int nowPlayerID = 0;
  /**
   * 现在玩家是否能控制进行游戏（如果是电脑角色在玩，玩家就不能控制）
   */

  private boolean ManControlStatus_CanNotPlay;
  /**
   * 随机函数
   */

  private Random random;
  /**
   * 定时器
   */

  private static Timer timer = null;
  /**
   * 地皮
   */

  private static int groundForBuilding[] = null;


  // 游戏角色变量

  /**
   * 角色在地图上位置（用地皮ID表示）
   */

  private static int player_location[] = null;
  /**
   * 角色卡片
   */

  private static int player_cards[][] = null;
  /**
   * 角色因为进入监狱而不能移动的次数
   */

  private static int player_prinson_canNotMoveNum[] = null;
  /**
   * 角色因为睡眠而不能移动的次数
   */

  private static int player_sleep_canNotMoveNum[] = null;
  /**
   * 角色轮转次序
   */

  public  static int player_sequence[] = null;
  /**
   * 角色的金钱
   */

  public  static int player_money[] = null;
  /**
   * 角色的地皮和房屋的图象
   */

  private static Image player_houseImage[][] = null;
  /**
   * 角色的头像
   */

  private static Image player_faceImage[] = null;
  /**
   * 玩家的股票 [角色ID][股票ID][0]---- 购入数目，[角色ID][股票ID][1]---- 购入价格
   *
   */

  public static int player_stock[][][] = null;
  /**
   * 屏幕图象缓冲
   */

  Image bufferImage;
  /**
   * 空白地皮图象
   */

  Image emptyGround;
  /**
   * 屏幕图象缓冲句柄
   */

  Graphics bufferImageG;
  /**
   * 是否背景灯打开
   */

  private boolean isLightOn = true;
  /**
   * 是否背景音乐打开
   */

  private boolean isMusicOn = true;
  /**
   * 游戏速度
   */

  private int gameSpeed = 3;
  /**
   * 音乐序号
   */

  private int music = 1;
  /**
   * 西门子手机的音乐合成器
   */

  com.siemens.mp.game.MelodyComposer composer;
  /**
   * 地皮的缓冲
   */

  private int[] bufferGroundForBuilding;

// 股票变量

  /**
   * 股票名字
   */

  public String[] stock_name;
  /**
   * 股票的价格
   */

  public int[] stock_price;
  /**
   * 股票的涨幅
   */

  public int[] stock_amplitude;
  /**
   * 股票的分数部分（便于计算）如 2元 涨幅 1%，总要个分数部分吧，不然老是2元
   */

  public int[] stock_price_fraction;
  /**
   * 构造游戏画板
   */

    public PlayCanvas(KMRichMan kmrichman)
    {

      player_faceImage = null;
      richMan = null;
      mapImage1 = null;
      thread = null;
      random = null;
      thread = null;

      richMan = kmrichman;
      ManControlStatus_CanNotPlay = false;
      random = new Random();
      timer = new Timer();
      canvasWidth = getWidth();
      canvasHeight = getHeight();
      bufferImage = Image.createImage(101,80);
      bufferImageG = bufferImage.getGraphics();

      groundForBuilding = new int[56];
      bufferGroundForBuilding = new int[56];

      player_location = new int[3];
      player_money = new int[3];
      player_sequence = new int[3];
      player_cards = new int[3][5];
      player_prinson_canNotMoveNum = new int[3];
      player_sleep_canNotMoveNum = new int[3];
      player_faceImage = new Image[3];

      player_stock = new int[3][4][2];

     // 加载图像
      loadMap();
      loadImage();
      //初始化音乐
      this.initialMusic();
      //初始化游戏
      initialGame();

    }
    /**
     * 加载地图
     */

    void loadMap()
    {
      try
      {
        mapImage1 = Image.createImage(128,160);
        mapImage2 = Image.createImage(128,160);
        Graphics g = mapImage1.getGraphics();
        g.drawImage(com.siemens.mp.ui.Image.createImageWithoutScaling("/res/image/map0bw.png"),0,0,20);
        g = mapImage2.getGraphics();
        g.drawImage(com.siemens.mp.ui.Image.createImageWithoutScaling("/res/image/map1bw.png"),0,0,20);
      }
      catch(Exception exception) { }
    }
    /**
     * 加载图象
     */

    void loadImage()
    {
      Image houseImage = null;
      Graphics g = null;
      try
      {
        houseImage = Image.createImage("/res/image/housebw.png");
        emptyGround = Image.createImage("/res/image/ground.png");
        gamePanelImage = com.siemens.mp.ui.Image.createImageWithoutScaling("/res/image/gamepanelbw.png");
        for(int i = 0; i < 3; i++)
          player_faceImage[i] = Image.createImage(String.valueOf(new StringBuffer("/res/image/h").append(i).append(".png")));
      }
      catch(Exception exception) {
        System.out.println("Err on loading Images");
      }
      // 把地皮和房子的图象分割成一个一个小块
      player_houseImage = new Image[3][4];
      for(int i = 0; i < 3; i++)
      {
        for(int j = 0; j < 4; j++)
        {
          Image image = Image.createImage(16, 16);
          g = image.getGraphics();
          g.drawImage(houseImage, -16 * j, -16 * i, 20);
          player_houseImage[i][j] = image;
        }
      }
      houseImage = null;
      g = null;
    }
    /**
     * 排列角色的次序
     * @param selectedPlayerID 玩家选择的角色ID
     */

    void arragePlayerSecquence(int selectedPlayerID)
    {
        player_sequence[0] = selectedPlayerID;

        int player2 = fetchRandom(2);
        int player3 = fetchRandom(2);
        for(; player2 == selectedPlayerID; player2 = fetchRandom(2));
        player_sequence[1] = player2;
        for(; player3 == selectedPlayerID || player3 == player2; player3 = fetchRandom(2));
        player_sequence[2] = player3;

    }
    /**
     * 转到下一个玩家
     * @param delay 延迟时间
     */

    public void nextPlay(int deplay)
    {
        timer.schedule(new Controlor(this), deplay);
    }
    /**
     * 电脑玩家开始游戏
     * @param delay 延迟时间
     * @param Num_ComputerActor_or_HunmanActor 是电脑还是玩家 （1-电脑 0-玩家）
     */

    public void play_computerActor(int delay, int Num_ComputerActor_or_HumanActor)
    {
        timer.schedule(new Controlor(this, Num_ComputerActor_or_HumanActor), delay);
    }
    /**
     * 开始游戏
     * @param isLoadGameRestart 是否是进行的存储的游戏（继续上次游戏）
     */

    public void play(boolean isLoadGameRestart)
    {
      // siemens special call
      if (this.isIsLightOn())
        com.siemens.mp.game.Light.setLightOn(); //开灯
      else
        com.siemens.mp.game.Light.setLightOff();//关灯
        //siemens special call

      choiceIndex = 0;
      status_Park10_Prinson20_Sleep30 = 0;

      if(isLoadGameRestart)
      {
        nowPlayerID = 2; // 让玩家先开始
      } else {
        repaint();
        serviceRepaints();
        try
        {
          Thread.sleep(600L);
        }
        catch(Exception exception) { }
      }
      // 换游戏角色
      nowPlayerID++;
      nowPlayerID %= 3;

      // 如果是玩家操作，那么保存游戏
      if(nowPlayerID == 0)
      {
        autoSaveGame();
        turnCount++;
        this.changeStock();
      }
      // 如果有游戏角色 钱 < 0
      if(player_money[nowPlayerID] < 0)
      {
        nowPlayerID++;
        nowPlayerID %= 3;
      }
      // 如果 玩家没有钱
      if(player_money[0] < 0)
      {
        gameOver(0);
        return;
      }
      // 如果电脑角色没有钱
      if(player_money[1] < 0 && player_money[2] < 0)
      {
        gameOver(1);
        return;
      }
      repaint();
      serviceRepaints();

      if(player_prinson_canNotMoveNum[nowPlayerID] > 0)
      {
        if(deleteCard(nowPlayerID, 5)) // 使用免罪卡
        {
          player_prinson_canNotMoveNum[nowPlayerID] = 0;
          nextPlay(1000);
        } else
        {
          status_Park10_Prinson20_Sleep30 = 20;
          repaint();
          serviceRepaints();
          nextPlay(1000);
          player_prinson_canNotMoveNum[nowPlayerID]--;
        }
      } else
      if(player_sleep_canNotMoveNum[nowPlayerID] > 0)
      {
        status_Park10_Prinson20_Sleep30 = 30;
        repaint();
        serviceRepaints();
        nextPlay(1000);
        player_sleep_canNotMoveNum[nowPlayerID]--;
      } else
      if(nowPlayerID != 0)
        play_computerActor(1000, 1); // 电脑玩家开始玩
      else
        ManControlStatus_CanNotPlay = false; // 玩家开始玩
    }
    /**
     * 启动移动角色线程
     * @param steps 步数
     */

    public void moveActor(int steps)
    {
        PlayCanvas.steps = steps;
        if(thread == null)
        {
          thread = new Thread(this);
          thread.start();
        }
      }
      /**
       * 移动角色的线程
       */

    public void run()
    {
        Thread thread = Thread.currentThread();
        int stepCount = 0;
        while(thread == this.thread)
          try
          {
            Thread.sleep((long)(gameSpeed*100)); //移动一步休息的时间
            if(stepCount == steps)
            {
              this.thread = null;
              stepOver(); //如果移动完毕
            } else
            {
              stepCount++;
              moveSteps(stepCount); //移动
              repaint();
              serviceRepaints();
            }
          }
          catch(Exception exception) { }
      }
      /**
       * 移动角色
       * @param stepCount 移动步数
       */

    private void moveSteps(int stepCount)
    {
        //System.out.println("Location:" + playerLocation[nowPlayerID]);
        player_location[nowPlayerID]++;
        specialLocationPlusSteps();
        if(player_location[nowPlayerID] == 50)
        { //如果到第50号地皮，就是一圈完毕
            if(stepCount != 1)
            {
                status_Park10_Prinson20_Sleep30 = 1;
                player_money[nowPlayerID] += 200; //加200的钱
            }
            player_location[nowPlayerID] = 0;
        }
    }
    /**
     * 到了特别的地方要加走的步数（因为？或飞机场占了几步的位置，但也只能算一步）
     */

    private void specialLocationPlusSteps()
    {
        switch(player_location[nowPlayerID])
        {
        case 5: // '\005'
        case 10: // '\n'
        case 14: // '\016'
        case 15: // '\017'
        case 27: // '\033'
        case 36: // '$'
        case 41: // ')'
        case 48: // '0'
            steps++;
            break;
        }
    }
    /**
     * 移动完毕要处理的事情
     */

    private void stepOver()
    {
        //System.out.println("StepOverLocation:" + player_location[nowPlayerID]);
        //playerLocation[nowPlayerID] = 49 ;
        switch(player_location[nowPlayerID])
        {
        case 2: //
        case 20: //
        case 42: // 随机事件
            randomEvent();
            return;

        case 6: //
        case 37: // 飞机场
            showFlyToNextStation();
            return;

        case 11: //
        case 25: //
        case 32: // 卡片
            addCard(10);
            return;

        case 16: // 监狱
            prise();
            return;

        case 28: //
        case 45: // 公园
            status_Park10_Prinson20_Sleep30 = 10;
            repaint();
            serviceRepaints();
            nextPlay(1000);
            return;

        case 49: // '1'
            nextPlay(1000);
            return;
        case 0:
        case 1:
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
        case 7: // '\007'
        case 8: // '\b'
        case 9: // '\t'
        case 10: // '\n'
        case 12: // '\f'
        case 13: // '\r'
        case 14: // '\016'
        case 15: // '\017'
        case 17: // '\021'
        case 18: // '\022'
        case 19: // '\023'
        case 21: // '\025'
        case 22: // '\026'
        case 23: // '\027'
        case 24: // '\030'
        case 26: // '\032'
        case 27: // '\033'
        case 29: // '\035'
        case 30: // '\036'
        case 31: // '\037'
        case 33: // '!'
        case 34: // '"'
        case 35: // '#'
        case 36: // '$'
        case 38: // '&'
        case 39: // '\''
        case 40: // '('
        case 41: // ')'
        case 43: // '+'
        case 44: // ','
        case 46: // '.'
        case 47: // '/'
        case 48: // '0'
        default:
            onCommonGround();
            return;
        }
    }
    /**
     * 移动完毕到了一般的地皮上要处理的事情
     */

    private void onCommonGround()
    {
       // 如果是0-空地，-1 -- 财神作用过的土地，-2 --衰神作用过的土地
        if(groundForBuilding[player_location[nowPlayerID]] <= 0)
        {
            if(player_money[nowPlayerID] >= fetchGroundPrice(player_location[nowPlayerID]) && player_money[nowPlayerID] != 0)
                buildorBuyGround();
            else
                nextPlay(1000);
        } else
        if(isNowPlayerIDGround()) //如果是自己的地皮
        {   //如果有足够的钱修建
            if(player_money[nowPlayerID] >= fetchGroundPrice(player_location[nowPlayerID]))
            {
                int actorLevel = 1;
                for(int i = 0; i < nowPlayerID; i++)
                    actorLevel *= 10;
                    //如果不是最高级建筑就可以修建
                if(groundForBuilding[player_location[nowPlayerID]] == 4 * actorLevel)
                    nextPlay(1000);
                else
                    buildorBuyGround();
            } else
            {
                nextPlay(1000);
            }
        } else //如果土地的所有者处于不能移动状态
        if(player_prinson_canNotMoveNum[fetchNowPlayerLocationGroundBelongtoWho()] > 0 || player_sleep_canNotMoveNum[fetchNowPlayerLocationGroundBelongtoWho()] > 0)
            nextPlay(1000);
        else //减钱，过路费啦：）
            decreaseMoney(fetchNowPlayerLocationGroundBelongtoWho(), fetchConnectedLocationPrice(player_location[nowPlayerID]));
    }
    /**
     * 绘制画板
     */

    protected void paint(Graphics graphics)
    {
      // 如果游戏仍在进行
      if(gameStatusGoOrWinOrFail == 0)
      {
        //清屏
        bufferImageG.setColor(255,255,255);
        bufferImageG.fillRect(0, 0, canvasWidth, canvasHeight);

        int mapX = (40 - computePlayerX(player_location[nowPlayerID])) + -6;
        int mapY = (56 - computePlayerY(player_location[nowPlayerID])) + -16;

        // 画出地皮
        for(int i = 0; i < 50; i++) {
          //如果地皮有所变化
          if (this.groundForBuilding[i] != this.bufferGroundForBuilding[i]) {
            this.bufferGroundForBuilding[i] = this.groundForBuilding[i];
            Graphics g1 = mapImage1.getGraphics();
            Graphics g2 = mapImage2.getGraphics();

            if (groundForBuilding[i] == 0) {
              g1.drawImage(this.emptyGround, computeGroundX(i),
                           computeGroundY(i), 20);
              g2.drawImage(this.emptyGround, -128 + computeGroundX(i),
                           computeGroundY(i), 20);
              continue;
            }

            //财神使用过的土地
            if (groundForBuilding[i] == -1)
              try {
                g1.drawImage(Image.createImage("/res/image/h3.png"),
                             computeGroundX(i), computeGroundY(i),
                             20);
                g2.drawImage(Image.createImage("/res/image/h3.png"),
                             -128 + computeGroundX(i), computeGroundY(i),
                             20);
                continue;
              }
              catch (Exception exception3) {}

            // 衰神卡使用过的土地
            if (groundForBuilding[i] == -2)
              try {
                g1.drawImage(Image.createImage("/res/image/h4.png"),
                             computeGroundX(i), computeGroundY(i),
                             20);
                g2.drawImage(Image.createImage("/res/image/h4.png"),
                             -128 + computeGroundX(i), computeGroundY(i),
                             20);
                continue;
              }
              catch (Exception exception4) {}

            // 玩家1
            if (groundForBuilding[i] < 10) {
              g1.drawImage(player_houseImage[player_sequence[0]][groundForBuilding[
                           i] - 1], computeGroundX(i),
                           computeGroundY(i), 20);
              g2.drawImage(player_houseImage[player_sequence[0]][groundForBuilding[
                           i] - 1], -128 + computeGroundX(i),
                           computeGroundY(i), 20);
              continue;
            }
            //玩家2
            if (groundForBuilding[i] < 100) {
              g1.drawImage(player_houseImage[player_sequence[1]][groundForBuilding[
                           i] / 10 - 1], computeGroundX(i),
                           computeGroundY(i), 20);
              g2.drawImage(player_houseImage[player_sequence[1]][groundForBuilding[
                           i] / 10 - 1], -128 + computeGroundX(i),
                           computeGroundY(i), 20);
              continue;
            }
            //玩家3
            g1.drawImage(player_houseImage[player_sequence[2]][groundForBuilding[
                         i] / 100 - 1], computeGroundX(i), computeGroundY(i), 20);
            g2.drawImage(player_houseImage[player_sequence[2]][groundForBuilding[
                         i] / 100 - 1], -128 + computeGroundX(i),computeGroundY(i), 20);
          }

        }
        //绘制地图
        try
        {
          bufferImageG.drawImage(mapImage1, (34 - computePlayerX(player_location[nowPlayerID])), (40 - computePlayerY(player_location[nowPlayerID])), 20);
          bufferImageG.drawImage(mapImage2, (162 - computePlayerX(player_location[nowPlayerID])), (40 - computePlayerY(player_location[nowPlayerID])), 20);
        }
        catch(Exception exception) { }


        // 画出非当前游戏者的头像
        try
        {
          for(int i = 2; i >= 0; i--)
            if(i != nowPlayerID && player_money[i] >= 0)
              bufferImageG.drawImage(player_faceImage[player_sequence[i]], mapX + computePlayerX(player_location[i]), mapY + computePlayerY(player_location[i]), 20);
        }
        catch(Exception exception5) { }

        // 画出控制条
        try
        {
          Image image = Image.createImage(28,80);
          Graphics g = image.getGraphics();
          g.drawImage(gamePanelImage, 0, -16, 20);
          bufferImageG.drawImage(image, 73, 0, 20);
          g = null;
          image = null;
        }
        catch(Exception exception6) { }

         try
         {
           // 画出控制条上的头像
           bufferImageG.drawImage(player_faceImage[player_sequence[nowPlayerID]], 79, 4, 20);
           // 画出游戏者头像
           if(player_money[nowPlayerID] >= 0)
             bufferImageG.drawImage(player_faceImage[player_sequence[nowPlayerID]], 34, 40, 20);
         }
         catch(Exception exception8) { }
         //画出选择框
         bufferImageG.setColor(255,255,255);
         bufferImageG.drawRect(78, 23 + choiceIndex * 14, 17, 13);

       }
       //如果玩家获胜
       if(gameStatusGoOrWinOrFail == 2)
       {
         try
         {
           bufferImageG.drawImage(Image.createImage("/res/image/start00bw.png"), 0, 0, 20);
           bufferImageG.drawImage(Image.createImage("/res/image/win.png"), 20, 22, 20);
         }
         catch(Exception exception1) { }
       }
       //如果玩家失败
       if(gameStatusGoOrWinOrFail == 3)
       {
         try
         {
           bufferImageG.drawImage(Image.createImage("/res/image/start00bw.png"), 0, 0, 20);
           bufferImageG.drawImage(Image.createImage("/res/image/lost.png"), 17, 22, 20);
         }
         catch(Exception exception2) { }
       }
       //绘制特殊角色状态
       if(status_Park10_Prinson20_Sleep30 == 30)
         drawMessage(bufferImageG, "睡眠");
       else
       if(status_Park10_Prinson20_Sleep30 == 20)
         drawMessage(bufferImageG, "监狱");
       else
       if(status_Park10_Prinson20_Sleep30 == 10)
         drawMessage(bufferImageG, "公园");
       else
       if(status_Park10_Prinson20_Sleep30 > 0)
       {
         drawMessage(bufferImageG, "+ $200");
         status_Park10_Prinson20_Sleep30 = status_Park10_Prinson20_Sleep30 != 4 ? ++status_Park10_Prinson20_Sleep30 : 0;
       }
       //画出缓冲的图
       graphics.drawImage(bufferImage,0,0,20);
    }
    /**
     * 绘制出特殊角色状态
     * @param g 句柄
     * @param s 状态信息
     */

    private void drawMessage(Graphics g, String s)
    {
        g.setColor(0, 0, 0);
        g.fillRect(8, 29, 80, 30);
        g.setColor(255, 255, 255);
        g.fillRect(10, 31, 76, 26);
        g.setColor(255, 0, 0);
        g.setFont(Font.getFont(0, 1, 0));
        g.drawString(s, 48, 48, 65);
    }
    /**
     * 按键处理
     * @param keyInt 键值
     */

    public void keyPressed(int keyInt)
    {
      // 如果玩家失败或是成功
      if (gameStatusGoOrWinOrFail > 1)
        KMRichMan.exitGame();
      // 如果不是玩家进行游戏
      if (nowPlayerID != 0 || ManControlStatus_CanNotPlay)
        return;
      int pressKey = getGameAction(keyInt);

      switch (pressKey) {
        default:
          break;

        case 1: // 上
        case 2:
          choiceIndex--;
          if (choiceIndex == -1)
            choiceIndex = 3;
          repaint();
          serviceRepaints();
          break;

        case 5: // 下
        case 6:
          choiceIndex++;
          if (choiceIndex == 4)
            choiceIndex = 0;
          repaint();
          serviceRepaints();
          break;

        case 8: // 确定
          switch (choiceIndex) {
            default:
              break;

            case 0: // 投色子
              richMan.setDisplayToDiceCanvas();
              ManControlStatus_CanNotPlay = true;
              break;

            case 1: // 卡片
              if (player_cards[nowPlayerID][0] == 16)
                richMan.showNoCard();
              else
                richMan.showCards(player_cards[nowPlayerID]);
              break;

            case 2: // 显示
              this.richMan.setDisplayToFucntionList();
              break;

            case 3: // 选项
              this.richMan.setDisplayToOptionList();
              break;
          }
          break;
      }
    }
    /**
     * 随机事件发生（角色走到？处）
     */

    private void randomEvent()
    {
        int random = fetchRandom(9);
        String s = "";
        switch(random)
        {
        case 0: // '\0'
            player_prinson_canNotMoveNum[nowPlayerID] += 2;
            player_location[nowPlayerID] = 16;
            s = "监狱(连续停留)2天";
            break;

        case 1: // '\001'
            player_money[nowPlayerID] += 300;
            s = "抽奖赢得 + $300";
            break;

        case 2: // '\002'
            s = "获得财富的 10%\n" + " + $".concat(String.valueOf(player_money[nowPlayerID] / 10));
            player_money[nowPlayerID] = (player_money[nowPlayerID] * 110) / 100;
            break;

        case 3: // '\003'
            s = "海外投资失利!\n损失财产10%\n" + " + $".concat(String.valueOf(String.valueOf((player_money[nowPlayerID] * 90) / 100)));
            player_money[nowPlayerID] = (player_money[nowPlayerID] * 90) / 100;
            break;

        case 4: // '\004'
            s = "收入税 5%\n" + " - $".concat(String.valueOf(String.valueOf(player_money[nowPlayerID] / 20)));
            player_money[nowPlayerID] = (player_money[nowPlayerID] * 95) / 100;
            break;

        case 5: // '\005'
            s = "财产税 5%\n" + " - $".concat(String.valueOf(String.valueOf(fetchBuildingTax() / 20)));
            player_money[nowPlayerID] -= fetchBuildingTax() / 20;
            break;

        case 6: // '\006'
            tricleBroadCast(15);
            s = "地震";
            break;

        case 7: // '\007'
            tricleBroadCast(25);
            s = "龙卷风";
            break;

        case 8: // '\b'
            fireGround();
            s = "火警";
            break;

        case 9: // '\t'
            player_money[nowPlayerID] += 200;
            s = "地产投资获利\n+ $200";
            break;
        }
        if(random != 6 && random != 7 && random != 8)
            richMan.showChance(s);
    }
    /**
     * 进入监狱
     */

    private void prise()
    {
        player_prinson_canNotMoveNum[nowPlayerID] += 3;
        richMan.showInPrisonMessage();
    }
    /**
     * 显示飞到下一个飞机场信息
     */

    private void showFlyToNextStation()
    {
        richMan.showBroadCast("飞到下一个机场.", null, true, "机场");
    }
    /**
     * 飞到下飞机场
     */

    public void fly()
    {
        try
        {
            Thread.sleep(1000L);
        }
        catch(Exception exception) { }
        switch(player_location[nowPlayerID])
        {
        case 6: // '\006'
            player_location[nowPlayerID] = 37;
            break;

        case 37: // '%'
            player_location[nowPlayerID] = 6;
            break;
        }
        nextPlay(1000);
    }
    /**
     * 使用卡片
     * @param cardID 卡片的ID
     */

    void useCard(int cardID)
    {
      switch(cardID)
      {
        case 4: // 免罪卡
        default:
          break;

        case 0: // 偷盗卡
          int cardNum = stealCard();
          if(cardNum == -1)
            richMan.showCardUsedMessage("没有卡片可偷.");
          else {
            deleteCard(nowPlayerID, cardID);
            addCard(cardNum);
          }
          break;

        case 1: // 均富卡
          deleteCard(nowPlayerID, cardID);
          int money = 0;
          for(int i = 0; i < 3; i++)
            if(i != nowPlayerID)
            {
              player_money[i] /= 2;
              money += player_money[i];
            }

          player_money[nowPlayerID] += money;
          richMan.showCardUsedMessage("获得 + $".concat(String.valueOf(money)));
          break;

        case 2: // 强占卡
          if(!isNowPlayerIDGround()) {
            occupyGround();
            deleteCard(nowPlayerID, cardID);
          }
          else {
            richMan.showCardUsedMessage("你不能在这用这张卡片.");
          }
          break;

        case 3: // 睡眠卡
          deleteCard(nowPlayerID, cardID);
          int playerID;
          for(playerID = fetchRandom(2); playerID == nowPlayerID; playerID = fetchRandom(2));
          player_sleep_canNotMoveNum[playerID] += 3;
          player_prinson_canNotMoveNum[playerID] = 0;
          richMan.showCardUsedMessage("玩家不能移动三轮.");
          break;

        case 5: // 怪兽卡
          deleteCard(nowPlayerID, cardID);
          tricleBroadCast(5);
          break;

        case 6: // 天使卡
          deleteCard(nowPlayerID, cardID);
          tricleBroadCast(11);
          break;

        case 7: // 现金卡
          deleteCard(nowPlayerID, cardID);
          richMan.setDisplayToDiceForm(player_money, nowPlayerID);
          break;

        case 8: // 财神卡
          if(isEmptyGround(player_location[nowPlayerID]))
          {
            groundForBuilding[player_location[nowPlayerID]] = -1;
            deleteCard(nowPlayerID, cardID);
            nextPlay(1000);
          } else
          {
            richMan.showCardUsedMessage("你不能在这使用.");
          }
          break;

        case 9: // 衰神卡
          if(isEmptyGround(player_location[nowPlayerID]))
          {
            groundForBuilding[player_location[nowPlayerID]] = -2;
            deleteCard(nowPlayerID, cardID);
            nextPlay(1000);
          } else
          {
            richMan.showCardUsedMessage("你不能在这使用.");
          }
          break;
      }
    }
    /**
     * 判断是否是空地
     * @param groundLocation 空地位置
     */

    private boolean isEmptyGround(int groundLocation)
    {
      return groundLocation != 2 && groundLocation != 6 && groundLocation != 11 && groundLocation != 16 && groundLocation != 20 && groundLocation != 25 && groundLocation != 28 && groundLocation != 32 && groundLocation != 37 && groundLocation != 42 && groundLocation != 45 && groundLocation != 49 && groundForBuilding[groundLocation] == 0;
    }
    /**
     * 强占土地
     */

    private void occupyGround()
    {
        for(; groundForBuilding[player_location[nowPlayerID]] > 9; groundForBuilding[player_location[nowPlayerID]] /= 10);
        switch(nowPlayerID)
        {
        case 2: // '\002'
            groundForBuilding[player_location[nowPlayerID]] *= 100;

        case 1: // '\001'
            groundForBuilding[player_location[nowPlayerID]] *= 10;

        default:
            richMan.showCardUsedMessage("这块地现在是你的了.");
            break;
        }
    }
    /**
     * 删除卡片
     * @param playID 角色ID
     * @param cardID 卡片ID
     */

    private boolean deleteCard(int playID, int cardID)
    {
      boolean flag = false;
      //判断有没有这张卡
      for(int i = 0; i < player_cards[playID].length; i++)
        if(player_cards[playID][i] == cardID)
          flag = true;

      if(!flag)
        return false;

      for(int i = 0; i < player_cards[playID].length; i++)
        if(player_cards[playID][i] == cardID)
        {
          player_cards[playID][i] = 16;
          i = 5; // 跳出循环，很奇怪的设置。。呵呵
        }
      // 重新排列卡片
      for(int i = 0; i < 4; i++)
        if(player_cards[playID][i] == 16)
        {
          player_cards[playID][i] = player_cards[playID][i + 1];
          player_cards[playID][i + 1] = 16;
        }
      return true;
    }
    /**
     * 偷卡片
     */

    private int stealCard()
    {
        if(nowPlayerID == 0 && player_cards[1][0] == 16 && player_cards[2][0] == 16 ||
           nowPlayerID == 1 && player_cards[0][0] == 16 && player_cards[2][0] == 16 ||
           nowPlayerID == 2 && player_cards[0][0] == 16 && player_cards[1][0] == 16)
            return -1;
        int cardNum = 16;
        int id = fetchRandomIn0ToMaxNumExceptExcludeNum(2, nowPlayerID);
        for(; cardNum == 16; cardNum = player_cards[id][fetchRandom(4)])
            id = fetchRandomIn0ToMaxNumExceptExcludeNum(2, nowPlayerID);
        deleteCard(id, cardNum);
        return cardNum;
    }
    /**
     * 增加卡片
     * @param 卡片ID
     */

    private void addCard(int cardID)
    {
      if(cardID == 10) // 随机增加卡片
        cardID = fetchRandom(9);
      for(int i = 0; i < 5; i++)
        if(player_cards[nowPlayerID][i] == 16)
        {
          player_cards[nowPlayerID][i] = cardID;
          richMan.showCardMessage(cardID);
          return;
        }
      forceAddCard(cardID);
    }
    /**
     * 强行增加卡片（5张卡片都满的时候）
     * @param 卡片ID
     */

    private void forceAddCard(int cardNum)
    {
      for(int i = 0; i < 4; i++)
        player_cards[nowPlayerID][i] = player_cards[nowPlayerID][i + 1];
      player_cards[nowPlayerID][4] = cardNum;
      richMan.showCardMessage(cardNum);
    }
    /**
     * 获的一个随机数（>=0 and <= number)
     * @param number 最大数（包括）
     */

    private int fetchRandom(int number)
    {
        return (random.nextInt() % (number + 1) + (number + 1)) % (number + 1);
    }
    /**
     * 获得一个随机数，除去一个数
     * @param maxNum 最大数
     * @param excludeNum 排除数
     */

    private int fetchRandomIn0ToMaxNumExceptExcludeNum(int maxNum, int excludeNum)
    {
        int id;
        for(id = fetchRandom(maxNum); id == excludeNum; id = fetchRandom(maxNum));
        return id;
    }
    /**
     * 火灾
     */

    private void fireGround()
    {
        int groundID = autoFetchNotSpecialGround();
        int groundID2;
        for(groundID2 = autoFetchNotSpecialGround(); groundNotConnected(groundID) != groundNotConnected(groundID2) || groundID2 == groundID; groundID2 = autoFetchNotSpecialGround());
        firedetroyGround(groundID, groundID + 1, 1);
        firedetroyGround(groundID2, groundID2 + 1, 1);
        repaint();
        serviceRepaints();
        int groundIDs[] = {
            groundID, groundID2
        };
        richMan.showBroadCast("这些区域被火灾摧毁了:", groundIDs, nowPlayerID != 0, "火警");
    }
    /**
     * 破坏地皮（因为火灾）
     * @param beginIndex 地皮开始序号
     * @param endIndex 地皮结束序号
     * @param j1 我都不知道干什么用的。。呵呵。（j1在这里没有用）
     */

    private void firedetroyGround(int beginIndex, int endIndex, int j1)
    {
        for(int k1 = beginIndex; k1 < endIndex + 1; k1++)
            if(groundForBuilding[k1] != 0)
                groundForBuilding[k1] = groundForBuilding[k1] >= 10 ? groundForBuilding[k1] >= 100 ? 100 : 10 : 1;

    }
    /**
     * 自动获取非特殊地皮。（除去？，飞机场等）
     */

    private int autoFetchNotSpecialGround()
    {
        int l = fetchRandom(47);
        if(l == 2 || l == 5 || l == 6 || l == 10 || l == 11 || l == 14 || l == 15 || l == 16 || l == 20 || l == 25 || l == 27 || l == 28 || l == 32 || l == 36 || l == 37 || l == 41 || l == 42 || l == 45)
            l = autoFetchNotSpecialGround();
        return l;
    }
    /**
     * 获取不连接的土地
     * @param groundLocation 地皮位置
     */

    private int groundNotConnected(int groundLocation)
    {
        return groundLocation >= 2 ? groundLocation >= 5 ? groundLocation >= 10 ? ((int) (groundLocation >= 14 ? ((int) (groundLocation >= 20 ? ((int) (groundLocation >= 25 ? ((int) (groundLocation >= 32 ? ((int) (groundLocation >= 36 ? ((int) (groundLocation >= 41 ? 9 : 8)) : 7)) : 6)) : 5)) : 4)) : 3)) : 2 : 1 : 0;
    }
    /**
     * 激发广播（地震等）
     * @param broadCastType 广播类型
     */

    private void tricleBroadCast(int broadCastType)
    {
        int groundIDs[] = null;
        switch(fetchRandom(8))
        {
        case 0: // '\0'
            broadCastChangeGround(0, 1, broadCastType);
            groundIDs = (new int[] {
                0, 1
            });
            break;

        case 1: // '\001'
            broadCastChangeGround(3, 4, broadCastType);
            groundIDs = (new int[] {
                3, 4
            });
            break;

        case 2: // '\002'
            broadCastChangeGround(7, 9, broadCastType);
            groundIDs = (new int[] {
                7, 8, 9
            });
            break;

        case 3: // '\003'
            broadCastChangeGround(12, 13, broadCastType);
            groundIDs = (new int[] {
                12, 13
            });
            break;

        case 4: // '\004'
            broadCastChangeGround(17, 19, broadCastType);
            groundIDs = (new int[] {
                17, 18, 19
            });
            break;

        case 5: // '\005'
            broadCastChangeGround(21, 24, broadCastType);
            groundIDs = (new int[] {
                21, 22, 23, 24
            });
            break;

        case 6: // '\006'
            broadCastChangeGround(26, 31, broadCastType);
            groundForBuilding[27] = 0; //很特别哦。
            groundForBuilding[28] = 0;//很特别哦，其他CASE都没有
            groundIDs = (new int[] {
                26, 29, 30, 31
            });
            break;

        case 7: // '\007'
            broadCastChangeGround(33, 35, broadCastType);
            groundIDs = (new int[] {
                33, 34, 35
            });
            break;

        case 8: // '\b'
            broadCastChangeGround(38, 47, broadCastType);
            groundIDs = (new int[] {
                38, 39, 40, 43, 44, 46, 47
            });
            break;
        }
        if(broadCastType == 11)
            richMan.showBroadCast("天使祝福:\n", groundIDs, nowPlayerID != 0, "新闻");
        else
        if(broadCastType == 5)
            richMan.showBroadCast("怪兽袭击\n:", groundIDs, nowPlayerID != 0, "新闻");
        else
        if(broadCastType == 15)
            richMan.showBroadCast("地震摧毁:\n", groundIDs, nowPlayerID != 0, "新闻");
        else
        if(broadCastType == 25)
            richMan.showBroadCast("龙卷风毁坏:\n", groundIDs, nowPlayerID != 0, "新闻");
    }
    /**
     * 广播改变（地震等）
     * @param beginID 开始地皮序号
     * @param endID 结束地皮序号
     * @param broadCastType 广播类型
     */

    private void broadCastChangeGround(int beginID, int endID, int broadCastType)
    {
        switch(broadCastType)
        {
        case 11: // 天使祝福
            for(int location = beginID; location < endID + 1; location++)
            {
                if(groundForBuilding[location] >= 100)
                    groundForBuilding[location] += 100;
                else
                if(groundForBuilding[location] >= 10)
                    groundForBuilding[location] += 10;
                else
                if(groundForBuilding[location] > 0)
                    groundForBuilding[location]++;
                adjustGroundOverBuilding(location);
            }

            break;

        case 15: // 地震摧毁
            for(int l1 = beginID; l1 < endID + 1; l1++)
                groundForBuilding[l1] = 0;

            break;

        default: // 龙卷风毁坏,怪兽袭击
            destroyGround(beginID, endID);
            break;
        }
    }
    /**
      * 龙卷风破坏土地
      * @param beginID 开始地皮序号
      * @param endID 结束地皮序号
      */


    private void destroyGround(int beginID, int endID)
    {
        for(int j1 = beginID; j1 < endID + 1; j1++)
            if(groundForBuilding[j1] != 0)
                groundForBuilding[j1] = groundForBuilding[j1] >= 10 ? groundForBuilding[j1] >= 100 ? 100 : 10 : 1;

    }
    /**
     * 调整土地，避免过度建设
     * @param groundLocation 土地位置
     */

    private void adjustGroundOverBuilding(int groundLocation)
    {
        groundForBuilding[groundLocation] = groundForBuilding[groundLocation] != 5 ? groundForBuilding[groundLocation] != 50 ? groundForBuilding[groundLocation] != 500 ? groundForBuilding[groundLocation] : 400 : 40 : 4;
    }
    /**
     * 获得财产税
     */

    private int fetchBuildingTax()
    {
        int tax = 0;
        int playerBuiding = 1;
        for(int j1 = 0; j1 < nowPlayerID; j1++)
            playerBuiding *= 10;

        for(int i = 0; i < groundForBuilding.length; i++)
            if(groundForBuilding[i] == 1 * playerBuiding)
                tax += fetchGroundPrice(i);
            else
            if(groundForBuilding[i] == 2 * playerBuiding)
                tax += (fetchGroundPrice(i) * 8 * 5) / 100;
            else
            if(groundForBuilding[i] == 3 * playerBuiding)
                tax += (fetchGroundPrice(i) * 8 * 15) / 100;
            else
            if(groundForBuilding[i] == 4 * playerBuiding)
                tax += (fetchGroundPrice(i) * 8 * 125) / 100;

        return tax;
    }
    /**
     * 判断是否是当前角色的地皮
     */

    private boolean isNowPlayerIDGround()
    {
        if(groundForBuilding[player_location[nowPlayerID]] == 0)
            return true;
        else
            return nowPlayerID == (groundForBuilding[player_location[nowPlayerID]] >= 10 ? groundForBuilding[player_location[nowPlayerID]] >= 100 ? 2 : 1 : 0);
    }
    /**
     * 买地或是加盖房子
     */

    private void buildorBuyGround()
    {
        int buildingLevel = groundForBuilding[player_location[nowPlayerID]];
        for(int i1 = 0; i1 < 2; i1++)
            if(buildingLevel > 10)
                buildingLevel /= 10;

        richMan.buildorBuyGround(player_location[nowPlayerID], fetchGroundPrice(player_location[nowPlayerID]), nowPlayerID != 0, buildingLevel);
    }
    /**
     * 减钱（走到别人的地皮上）
     * @param groundBelongtoWho 谁的土地
     * @param price  过路费
     */

    private void decreaseMoney(int groundBelongtoWho, int price)
    {
        player_money[nowPlayerID] -= price;
        player_money[groundBelongtoWho] += price;
        // 如果角色钱 < 0 把他所有的土地清除
        if(player_money[nowPlayerID] < 0)
            switch(nowPlayerID)
            {
            default:
                break;

            case 0: // '\0'
                for(int j1 = 0; j1 < groundForBuilding.length; j1++)
                    if(groundForBuilding[j1] < 10 && groundForBuilding[j1] > 0)
                        groundForBuilding[j1] = 0;
                break;

            case 1: // '\001'
              for(int j1 = 0; j1 < groundForBuilding.length; j1++)
                  if(groundForBuilding[j1] < 100 && groundForBuilding[j1] > 10)
                      groundForBuilding[j1] = 0;
              break;


            case 2: // '\002'
                for(int l1 = 0; l1 < groundForBuilding.length; l1++)
                    if(groundForBuilding[l1] >= 100)
                        groundForBuilding[l1] = 0;
                break;
            }
        richMan.showPaymentExpense(price, nowPlayerID != 0);
    }
    /**
     * 判断当前的当前角色所处地皮是谁的。
     */

    private int fetchNowPlayerLocationGroundBelongtoWho()
    {
        if(groundForBuilding[player_location[nowPlayerID]] < 10)
            return 0;
        else
            return groundForBuilding[player_location[nowPlayerID]] >= 100 ? 2 : 1;
    }
    /**
     * 获得地皮的价格
     * @param groundID 地皮ID
     */

    private int fetchGroundPrice(int groundID)
    {
        switch(groundID)
        {
        case 0: // '\0'
            return 82;

        case 1: // '\001'
        case 13: // '\r'
            return 80;

        case 3: // '\003'
        case 8: // '\b'
            return 100;

        case 4: // '\004'
        case 7: // '\007'
        case 9: // '\t'
        case 31: // '\037'
        case 38: // '&'
        case 44: // ','
            return 120;

        case 12: // '\f'
        case 29: // '\035'
        case 34: // '"'
            return 90;

        case 17: // '\021'
        case 23: // '\027'
        case 24: // '\030'
        case 30: // '\036'
        case 46: // '.'
        case 47: // '/'
            return 110;

        case 18: // '\022'
            return 130;

        case 19: // '\023'
            return 95;

        case 21: // '\025'
        case 43: // '+'
            return 150;

        case 22: // '\026'
        case 40: // '('
            return 140;

        case 26: // '\032'
            return 105;

        case 33: // '!'
        case 35: // '#'
            return 85;

        case 39: // '\''
            return 125;

        case 2: // '\002'
        case 5: // '\005'
        case 6: // '\006'
        case 10: // '\n'
        case 11: // '\013'
        case 14: // '\016'
        case 15: // '\017'
        case 16: // '\020'
        case 20: // '\024'
        case 25: // '\031'
        case 27: // '\033'
        case 28: // '\034'
        case 32: // ' '
        case 36: // '$'
        case 37: // '%'
        case 41: // ')'
        case 42: // '*'
        case 45: // '-'
        default:
            return 0;
        }
    }
    /**
     * 获得相连的地皮价格
     * @param id 地皮ID
     */

    private int fetchConnectedLocationPrice(int id)
    {
        int price = 0;
        int groundID = id;
        byte groundActorID = groundForBuilding[groundID] >= 10 ? (byte)(groundForBuilding[groundID] >= 100 ? 100 : 10) : 1;
        if(groundID != 0)
            do
            {
                if(groundForBuilding[groundID - 1] == 0 || groundForBuilding[groundID - 1] < groundActorID || groundForBuilding[groundID - 1] > groundActorID * 4)
                    break;
                price += fetchTheGroundPrice(groundForBuilding[groundID - 1], groundID - 1);
            } while(--groundID != 0);
        groundID = groundID;
        do
        {
            if(groundForBuilding[groundID + 1] == 0 || groundForBuilding[groundID + 1] < groundActorID || groundForBuilding[groundID + 1] > groundActorID * 4)
                break;
            price += fetchTheGroundPrice(groundForBuilding[groundID + 1], groundID + 1);
        } while(++groundID != 49);
        if(price == 0)
            price = fetchAddPrice(groundID);
        else
            price += fetchTheGroundPrice(groundForBuilding[groundID], groundID);
        return price;
    }
    /**
     * 获得地皮价格
     * @param groundBuilding 地皮的级别
     * @param groudID 地皮ID
     */

    private int fetchTheGroundPrice(int groundBuilding, int groundID)
    {
        switch(groundBuilding)
        {
        case 1: // '\001'
        case 10: // '\n'
        case 100: // 'd'
            return fetchGroundPrice(groundID);
        }
        return 500;
    }
    /**
     * 获得地皮价格
     * @param 地皮ID
     */

    private int fetchAddPrice(int groundID)
    {
        switch(groundForBuilding[groundID])
        {
        case 1: // '\001'
        case 10: // '\n'
        case 100: // 'd'
            return (fetchGroundPrice(groundID) * 4) / 5;

        case 2: // '\002'
        case 20: // '\024'
        case 200:
            return fetchGroundPrice(groundID) * 2;

        case 3: // '\003'
        case 30: // '\036'
        case 300:
            return (fetchGroundPrice(groundID) * 14) / 5;

        case 4: // '\004'
        case 40: // '('
        case 400:
            return fetchGroundPrice(groundID) * 4;
        }
        return 0;
    }
    /**
     * 计算地皮的数量
     * @param groundValue 地皮值
     */

    public int countPlayerGround(int goundValue)
    {
        int count = 0;
        for(int i = 0; i < groundForBuilding.length; i++)
            if(groundForBuilding[i] == goundValue)
                count++;

        return count;
    }
    /**
     * 计算角色在地图上的坐标X
     * @param id 地皮ID
     */

    private int computePlayerX(int id)
    {
        return id <= 44 && id >= 3 ? id >= 7 ? id >= 12 ? id >= 16 ? id >= 21 ? id >= 25 ? id >= 33 ? id >= 45 ? 0 : 208 - 16 * (id - 33) : 224 : 160 + 16 * (id - 21) : 144 : 96 + 16 * (id - 12) : 80 : 32 + 16 * (id - 3) : 16;
    }
    /**
     * 计算角色在地图上的坐标Y
     * @param id 地皮ID
     */

    private int computePlayerY(int id)
    {
        return id >= 3 ? id >= 7 ? id >= 11 ? id >= 16 ? id >= 20 ? id >= 26 ? id >= 32 ? id >= 46 ? id >= 50 ? 0 : 32 + 16 * (id - 46) : 16 : 112 - 16 * (id - 26) : 128 : 64 + 16 * (id - 16) : 48 : 112 - 16 * (id - 7) : 128 : 96 + 16 * id;
    }
    /**
     * 计算地皮在地图上的坐标X
     * @param id 地皮ID
     */

    private int computeGroundX(int id)
    {
        return id >= 2 ? id >= 5 ? id >= 10 ? id >= 14 ? id >= 20 ? id >= 25 ? id >= 32 ? id >= 45 ? ((int) (id >= 48 ? -200 : 0)) : 208 - 16 * (id - 33) : 240 : 160 + 16 * (id - 21) : 128 : 96 + 16 * (id - 12) : 64 : 32 + 16 * (id - 3) : 0;
    }
    /**
     * 计算角色在地图上的坐标Y
     * @param id 地皮ID
     */

    private int computeGroundY(int id)
    {
        return id >= 2 ? id >= 5 ? id >= 10 ? id >= 14 ? id >= 20 ? id >= 25 ? id >= 32 ? id >= 45 ? id >= 48 ? 0 : 32 + 16 * (id - 46) : 0 : 112 - 16 * (id - 26) : 144 : 80 + 16 * (id - 17) : 64 : 112 - 16 * (id - 7) : 144 : 96 + 16 * id;
    }
    /**
     * 初始化游戏
     */

    public void initialGame()
    {
      //角色
      for(int i = 0; i < 3; i++)
      {
        player_location[i] = 49;
        player_money[i] = 3500;
        player_prinson_canNotMoveNum[i] = 0;
        player_sleep_canNotMoveNum[i] = 0;
        for(int j = 0; j < 5; j++)
          player_cards[i][j] = 16;
        for(int j = 0; j < 4; j++)
          for (int k = 0; k < 2; k++)
            player_stock[i][j][k] = 0;
      }
      //股票
      this.stock_name = new String[4];
      stock_name[0] = "嘟嘟控股";
      stock_name[1] = "华西医大";
      stock_name[2] = "Microsoft";
      stock_name[3] = "Linux";
      this.stock_price = new int[4];
      stock_price[0] = 10;
      stock_price[1] = 4;
      stock_price[2] = 12;
      stock_price[3] = 6;
      this.stock_amplitude = new int[4];
      stock_amplitude[0] = 5 - this.fetchRandom(10);
      stock_amplitude[1] = 5 - this.fetchRandom(10);
      stock_amplitude[2] = 5 - this.fetchRandom(10);
      stock_amplitude[3] = 5 - this.fetchRandom(10);
      this.stock_price_fraction = new int[4];
      stock_price_fraction[0] = 0;
      stock_price_fraction[1] = 0;
      stock_price_fraction[2] = 0;
      stock_price_fraction[3] = 0;
      //其他
      choiceIndex = 0;
      gameStatusGoOrWinOrFail = 0;
      turnCount = 0;
      nowPlayerID = 0;
      ManControlStatus_CanNotPlay = false;
    }
    /**
     * 控制股票升降
     */

    private void changeStock() {
      for (int i = 0; i < 4; i++ ){
        stock_amplitude[i] += (7 - this.fetchRandom(14));
        if (stock_amplitude[i] > 10)
          stock_amplitude[i] = 10; //涨幅不能大于10%
        if (stock_amplitude[i] < -10)
          stock_amplitude[i] = -10; //涨幅不能小于-10%
        int price = stock_price[i] * 10000 + stock_price_fraction[i];
        price = price * (100 + stock_amplitude[i]) / 100;
        stock_price[i] = price / 10000;
        stock_price_fraction[i] = price - stock_price[i] * 10000;
        if (stock_price[i] < 2) { //价格不能小于 2块钱
          stock_price[i] = 2;
        }
      }
    }
    /**
     * 游戏结束
     * @param status 结束标记
     */

    private void gameOver(int status)
    {
        if(status == 1)
        {
            gameStatusGoOrWinOrFail = 2;
            repaint();
            serviceRepaints();
            richMan.freshHighScore(player_sequence[0], turnCount);
        } else
        {
            gameStatusGoOrWinOrFail = 3;
            repaint();
            serviceRepaints();
        }
    }
    /**
     * 自动保存文件
     */

    public void autoSaveGame()
    {
      try {
        RecordStore recordstore = RecordStore.openRecordStore("RichMan", true);
        byte bytes[] = saveGame();
        if (recordstore.getNumRecords() == 0)
          recordstore.addRecord(bytes, 0, bytes.length);
        else {
          recordstore.setRecord(1, bytes, 0, bytes.length);
          System.out.print("AutoSetSaveGame Success!");
        }
        recordstore.closeRecordStore();
      }
      catch (RecordStoreException ex) {
        System.out.println("AutoSave failed!");
      }
    }
    /**
     * 获得游戏记录
     */

    public boolean fetchRecords()
    {
      try {
        RecordStore recordstore = RecordStore.openRecordStore("RichMan", false);
        if (recordstore == null)
          return false;
        rebuildGame(recordstore.getRecord(1));
        recordstore.closeRecordStore();
      }
      catch (Exception exception) {
        return false;
      }
      play(true);
      return true;
    }
    /**
     * 保存游戏
     */

    public byte[] saveGame()
    {
        byte bytes[] = null;
        try
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
            // 玩家数据
            for(int i = 0; i < 3; i++)
            {
                dataoutputstream.writeInt(player_location[i]);
                dataoutputstream.writeInt(player_money[i]);
                dataoutputstream.writeInt(player_sequence[i]);
                dataoutputstream.writeInt(player_prinson_canNotMoveNum[i]);
                for (int j = 0; j < 4; j++)
                  for (int k = 0; k < 2; k++)
                    dataoutputstream.writeInt(this.player_stock[i][j][k]);
                for(int j = 0; j < 5; j++)
                  dataoutputstream.writeInt(player_cards[i][j]);
            }
            // 地皮数据
            for(int i = 0; i < groundForBuilding.length; i++)
              dataoutputstream.writeInt(groundForBuilding[i]);
              // 股票数据
            for(int i = 0; i < 4; i++) {
              dataoutputstream.writeInt(this.stock_price[i]);
              dataoutputstream.writeInt(this.stock_amplitude[i]);
              dataoutputstream.writeInt(this.stock_price_fraction[i]);
            }
            // 其他数据
            dataoutputstream.writeInt(turnCount);
            dataoutputstream.writeInt(this.gameSpeed);
            dataoutputstream.writeBoolean(this.isLightOn);
            dataoutputstream.writeBoolean(this.isMusicOn);

            bytes = bytearrayoutputstream.toByteArray();
            bytearrayoutputstream.close();
            dataoutputstream.close();
        }
        catch(Exception exception) { }
        return bytes;
    }
    /**
     * 重建游戏（load game)
     */

    public void rebuildGame(byte recordData[])
    {
        try
        {
            ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(recordData);
            DataInputStream datainputstream = new DataInputStream(bytearrayinputstream);
            for(int i = 0; i < 3; i++)
            {
                player_location[i] = datainputstream.readInt();
                player_money[i] = datainputstream.readInt();
                player_sequence[i] = datainputstream.readInt();
                player_prinson_canNotMoveNum[i] = datainputstream.readInt();
                for (int j = 0; j < 4; j++)
                  for (int k = 0; k < 2; k++)
                    player_stock[i][j][k] = datainputstream.readInt();
                for(int j = 0; j < 5; j++)
                  player_cards[i][j] = datainputstream.readInt();

            }

            for(int i = 0; i < groundForBuilding.length; i++)
                groundForBuilding[i] = datainputstream.readInt();
              for(int i = 0; i < 4; i++) {
                this.stock_price[i] = datainputstream.readInt();
                this.stock_amplitude[i] = datainputstream.readInt();
                this.stock_price_fraction[i] = datainputstream.readInt();
              }

            turnCount = datainputstream.readInt();
            this.gameSpeed = datainputstream.readInt();
            this.isLightOn = datainputstream.readBoolean();
            this.isMusicOn = datainputstream.readBoolean();

            this.setIsLightOn(this.isLightOn);
            this.setIsMusicOn(this.isIsMusicOn());

            bytearrayinputstream.close();
            datainputstream.close();
        }
        catch(Exception exception) { }
    }
    /**
     * 购买土地
     */

    public void buyLand()
    {
        switch(groundForBuilding[player_location[nowPlayerID]])
        {
        case 0: // '\0'
            player_money[nowPlayerID] -= fetchGroundPrice(player_location[nowPlayerID]);
            break;

        case 1: //
        case 10: //
        case 100: //
            player_money[nowPlayerID] -= (fetchGroundPrice(player_location[nowPlayerID]) * 2) / 5;
            break;

        case 2: //
        case 20: //
        case 200:
            player_money[nowPlayerID] -= (fetchGroundPrice(player_location[nowPlayerID]) * 3) / 5;
            break;

        case 3: //
        case 30: //
        case 300:
            player_money[nowPlayerID] -= (fetchGroundPrice(player_location[nowPlayerID]) * 4) / 5;
            break;

        case -2: //衰神的土地
            player_money[nowPlayerID] -= fetchGroundPrice(player_location[nowPlayerID]);
            break;
        case -1: //财神的土地
          break;
        }
        if(groundForBuilding[player_location[nowPlayerID]] == -2)
            groundForBuilding[player_location[nowPlayerID]] = 0; // 恢复衰神的土地
        else
            groundForBuilding[player_location[nowPlayerID]] += nowPlayerID != 0 ? nowPlayerID != 1 ? 100 : 10 : 1;
        adjustGroundOverBuilding(player_location[nowPlayerID]);
    }
    /**
     * 处理电脑玩家玩游戏
     */

    void dealWithComputerActorPlay()
    {
      ManControlStatus_CanNotPlay = true;
      int cardIndex = -1;
      //自动获得卡片
      if(player_cards[nowPlayerID][0] != 16)
        cardIndex = autoFetchCardIndex();
      if(cardIndex != -1 &&
         this.fetchRandom(5) > 3) // 电脑有2/6机会使用卡片
      {
        choiceIndex = 1;
        repaint();
        serviceRepaints();
        try
        {
          Thread.sleep(1000L);
        }
        catch(Exception exception) { }

        richMan.useCard_ComputerActor(player_cards[nowPlayerID], cardIndex);

        /*
        choiceID = 0;
        repaint();
        serviceRepaints();
        try
        {
          Thread.sleep(1000L);
        }
        catch(Exception exception) { }
        */
      }else
        richMan.setDisplayToDiceCanvas();
    }
    /**
     * 自动获得卡片
     */

    private int autoFetchCardIndex()
    {
      int cardCount = 0;
      //计算卡片的数目
      for(int i1 = 0; i1 < 5; i1++)
        if(player_cards[nowPlayerID][i1] != 16)
          cardCount++;

      for(int index = 0; index < cardCount; index++)
        switch(player_cards[nowPlayerID][index])
        {
          case 4: // 免罪卡
          default:
            break;

          case 2: // 强占卡
            if(!isNowPlayerIDGround())
              return index;
            break;

          case 8: // 财神卡
          case 9: // 衰神卡
            if(groundForBuilding[player_location[nowPlayerID]] == 0 &&
               player_location[nowPlayerID] != 2 &&
               player_location[nowPlayerID] != 5 &&
               player_location[nowPlayerID] != 11 &&
               player_location[nowPlayerID] != 16 &&
               player_location[nowPlayerID] != 20 &&
               player_location[nowPlayerID] != 25 &&
               player_location[nowPlayerID] != 28 &&
               player_location[nowPlayerID] != 32 &&
               player_location[nowPlayerID] != 37 &&
               player_location[nowPlayerID] != 42 &&
               player_location[nowPlayerID] != 45 &&
               player_location[nowPlayerID] != 49)
              return index;
            break;

          case 0: //其他卡片
          case 1: //
          case 3: //
          case 5: //
          case 6: //
          case 7: //
            return index;
        }
      return -1;
    }
    /**
     * 设置背景灯
     * @param isLightOn 是否开灯
     */

  public void setIsLightOn(boolean isLightOn) {
    this.isLightOn = isLightOn;
    if (this.isLightOn)
      com.siemens.mp.game.Light.setLightOn();
    else
      com.siemens.mp.game.Light.setLightOff();
  }
  /**
   * 设置音乐
   * @param isMusicOn 是否打开音乐
   */

  public void setIsMusicOn(boolean isMusicOn) {
    this.isMusicOn = isMusicOn;
    if (this.isMusicOn)
      this.composer.getMelody().play();
    else
      this.composer.getMelody().stop();
  }
  /**
   * 获取背景灯状态
   */

  public boolean isIsLightOn() {
    return isLightOn;
  }
  /**
   * 获取背景音乐状态
   */

  public boolean isIsMusicOn() {
    return isMusicOn;
  }
  /**
   * 获取游戏速度
   */

  public int getGameSpeed() {
    return gameSpeed;
  }
  /**
   * 设置游戏速度
   */

  public void setGameSpeed(int gameSpeed) {
    this.gameSpeed = gameSpeed;
  }
  /**
   * 初始化音乐
   */

  public void initialMusic() {
    if (composer == null)
      composer = new com.siemens.mp.game.MelodyComposer();
    else
      composer.resetMelody();
    composer.setBPM(240);
    try {
      com.siemens.mp.io.File file = new com.siemens.mp.io.File();
      int musicFile = file.open("music" + this.music +".msc");
      int length = file.length(musicFile);
      byte[] bytes = new byte[length];
      file.read(musicFile,bytes,0,length);

      for (int i = 0; i < length / 2; i++) {
        composer.appendNote((int)bytes[i*2], (int)bytes[i*2+1]);
      }
      composer.appendNote(composer.TONE_REPEV, 1);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    /*
    try {
      composer.appendNote(composer.TONE_E3, composer.TONELENGTH_DOTTED_1_4);
      composer.appendNote(composer.TONE_D3, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_E3, composer.TONELENGTH_DOTTED_1_4);
      composer.appendNote(composer.TONE_D3, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_C3, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_H2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);

      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_1_8);
      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_DOTTED_1_4);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);

      composer.appendNote(composer.TONE_F2, composer.TONELENGTH_1_8);
      composer.appendNote(composer.TONE_F2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_E2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_F2, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_C3, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);

      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_4);
      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_H2, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_H2, composer.TONELENGTH_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_C3, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_D3, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);

      composer.appendNote(composer.TONE_E3, composer.TONELENGTH_DOTTED_1_4);
      composer.appendNote(composer.TONE_D3, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_E3, composer.TONELENGTH_DOTTED_1_4);
      composer.appendNote(composer.TONE_D3, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_C3, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_H2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);

      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_1_8);
      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_DOTTED_1_4);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);

      composer.appendNote(composer.TONE_F2, composer.TONELENGTH_1_8);
      composer.appendNote(composer.TONE_F2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_E2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_F2, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_C3, composer.TONELENGTH_DOTTED_1_16);
      composer.appendNote(composer.TONE_PAUSE, composer.TONELENGTH_DOTTED_1_16);

      composer.appendNote(composer.TONE_G2, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_A2, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_H2, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_C3, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_D3, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_E3, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_F3, composer.TONELENGTH_DOTTED_1_8);
      composer.appendNote(composer.TONE_G3, composer.TONELENGTH_DOTTED_1_8);

      composer.appendNote(composer.TONE_REPEV, 1);

    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
   */
  }
  /**
   * 获取音乐序号
   */

  public int getMusic() {
    return music;
  }
  /**
   * 设置音乐序号
   * @param music 音乐序号
   */

  public void setMusic(int music) {
    this.music = music;
    this.initialMusic();
  }
}