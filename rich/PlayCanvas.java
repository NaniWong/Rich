
import java.io.*;
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.rms.*;
/**
 * ��Ϸ���塣���������Ϸ��ؼ���һ����
 * @author SoftStar,����
 * @version 1.0
 */

public class PlayCanvas extends Canvas
    implements Runnable
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
  private KMRichMan richMan;
  /**
   * ��ͼ��1����
   */
  private Image mapImage1;
  /**
   * ��ͼ��2����
   */
  private Image mapImage2;
  /**
   * ��Ϸ�������
   */
  private Image gamePanelImage;
  /**
   * �߳�
   */
  private volatile Thread thread;
  /**
   * ��������ѡ����ţ�1-ɫ�ӣ�2-��Ƭ��3-�����������Ϣ����Ʊ��Ϣ��4-ϵͳ
   */

  private static int choiceIndex = 0;
  /**
   * ��Ϸ��ɫҪ�ߵĲ���
   */

  private static int steps = 0;
  /**
   * ��Ϸ���е�����
   */

  private static int turnCount = 0;
  /**
   * ��Ϸ״̬��0-���У�2-ʤ����3-ʧ�ܣ�
   */

  private static int gameStatusGoOrWinOrFail = 0;
  /**
   * ��ɫ����״̬��10--�ڹ�԰����20-�ڼ����30-��˯���У�˯�߿���
   */

  private static int status_Park10_Prinson20_Sleep30 = 0;
  /**
   * �������ڽ��еĽ�ɫ
   */

  public static int nowPlayerID = 0;
  /**
   * ��������Ƿ��ܿ��ƽ�����Ϸ������ǵ��Խ�ɫ���棬��ҾͲ��ܿ��ƣ�
   */

  private boolean ManControlStatus_CanNotPlay;
  /**
   * �������
   */

  private Random random;
  /**
   * ��ʱ��
   */

  private static Timer timer = null;
  /**
   * ��Ƥ
   */

  private static int groundForBuilding[] = null;


  // ��Ϸ��ɫ����

  /**
   * ��ɫ�ڵ�ͼ��λ�ã��õ�ƤID��ʾ��
   */

  private static int player_location[] = null;
  /**
   * ��ɫ��Ƭ
   */

  private static int player_cards[][] = null;
  /**
   * ��ɫ��Ϊ��������������ƶ��Ĵ���
   */

  private static int player_prinson_canNotMoveNum[] = null;
  /**
   * ��ɫ��Ϊ˯�߶������ƶ��Ĵ���
   */

  private static int player_sleep_canNotMoveNum[] = null;
  /**
   * ��ɫ��ת����
   */

  public  static int player_sequence[] = null;
  /**
   * ��ɫ�Ľ�Ǯ
   */

  public  static int player_money[] = null;
  /**
   * ��ɫ�ĵ�Ƥ�ͷ��ݵ�ͼ��
   */

  private static Image player_houseImage[][] = null;
  /**
   * ��ɫ��ͷ��
   */

  private static Image player_faceImage[] = null;
  /**
   * ��ҵĹ�Ʊ [��ɫID][��ƱID][0]---- ������Ŀ��[��ɫID][��ƱID][1]---- ����۸�
   *
   */

  public static int player_stock[][][] = null;
  /**
   * ��Ļͼ�󻺳�
   */

  Image bufferImage;
  /**
   * �հ׵�Ƥͼ��
   */

  Image emptyGround;
  /**
   * ��Ļͼ�󻺳���
   */

  Graphics bufferImageG;
  /**
   * �Ƿ񱳾��ƴ�
   */

  private boolean isLightOn = true;
  /**
   * �Ƿ񱳾����ִ�
   */

  private boolean isMusicOn = true;
  /**
   * ��Ϸ�ٶ�
   */

  private int gameSpeed = 3;
  /**
   * �������
   */

  private int music = 1;
  /**
   * �������ֻ������ֺϳ���
   */

  com.siemens.mp.game.MelodyComposer composer;
  /**
   * ��Ƥ�Ļ���
   */

  private int[] bufferGroundForBuilding;

// ��Ʊ����

  /**
   * ��Ʊ����
   */

  public String[] stock_name;
  /**
   * ��Ʊ�ļ۸�
   */

  public int[] stock_price;
  /**
   * ��Ʊ���Ƿ�
   */

  public int[] stock_amplitude;
  /**
   * ��Ʊ�ķ������֣����ڼ��㣩�� 2Ԫ �Ƿ� 1%����Ҫ���������ְɣ���Ȼ����2Ԫ
   */

  public int[] stock_price_fraction;
  /**
   * ������Ϸ����
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

     // ����ͼ��
      loadMap();
      loadImage();
      //��ʼ������
      this.initialMusic();
      //��ʼ����Ϸ
      initialGame();

    }
    /**
     * ���ص�ͼ
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
     * ����ͼ��
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
      // �ѵ�Ƥ�ͷ��ӵ�ͼ��ָ��һ��һ��С��
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
     * ���н�ɫ�Ĵ���
     * @param selectedPlayerID ���ѡ��Ľ�ɫID
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
     * ת����һ�����
     * @param delay �ӳ�ʱ��
     */

    public void nextPlay(int deplay)
    {
        timer.schedule(new Controlor(this), deplay);
    }
    /**
     * ������ҿ�ʼ��Ϸ
     * @param delay �ӳ�ʱ��
     * @param Num_ComputerActor_or_HunmanActor �ǵ��Ի������ ��1-���� 0-��ң�
     */

    public void play_computerActor(int delay, int Num_ComputerActor_or_HumanActor)
    {
        timer.schedule(new Controlor(this, Num_ComputerActor_or_HumanActor), delay);
    }
    /**
     * ��ʼ��Ϸ
     * @param isLoadGameRestart �Ƿ��ǽ��еĴ洢����Ϸ�������ϴ���Ϸ��
     */

    public void play(boolean isLoadGameRestart)
    {
      // siemens special call
      if (this.isIsLightOn())
        com.siemens.mp.game.Light.setLightOn(); //����
      else
        com.siemens.mp.game.Light.setLightOff();//�ص�
        //siemens special call

      choiceIndex = 0;
      status_Park10_Prinson20_Sleep30 = 0;

      if(isLoadGameRestart)
      {
        nowPlayerID = 2; // ������ȿ�ʼ
      } else {
        repaint();
        serviceRepaints();
        try
        {
          Thread.sleep(600L);
        }
        catch(Exception exception) { }
      }
      // ����Ϸ��ɫ
      nowPlayerID++;
      nowPlayerID %= 3;

      // �������Ҳ�������ô������Ϸ
      if(nowPlayerID == 0)
      {
        autoSaveGame();
        turnCount++;
        this.changeStock();
      }
      // �������Ϸ��ɫ Ǯ < 0
      if(player_money[nowPlayerID] < 0)
      {
        nowPlayerID++;
        nowPlayerID %= 3;
      }
      // ��� ���û��Ǯ
      if(player_money[0] < 0)
      {
        gameOver(0);
        return;
      }
      // ������Խ�ɫû��Ǯ
      if(player_money[1] < 0 && player_money[2] < 0)
      {
        gameOver(1);
        return;
      }
      repaint();
      serviceRepaints();

      if(player_prinson_canNotMoveNum[nowPlayerID] > 0)
      {
        if(deleteCard(nowPlayerID, 5)) // ʹ�����￨
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
        play_computerActor(1000, 1); // ������ҿ�ʼ��
      else
        ManControlStatus_CanNotPlay = false; // ��ҿ�ʼ��
    }
    /**
     * �����ƶ���ɫ�߳�
     * @param steps ����
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
       * �ƶ���ɫ���߳�
       */

    public void run()
    {
        Thread thread = Thread.currentThread();
        int stepCount = 0;
        while(thread == this.thread)
          try
          {
            Thread.sleep((long)(gameSpeed*100)); //�ƶ�һ����Ϣ��ʱ��
            if(stepCount == steps)
            {
              this.thread = null;
              stepOver(); //����ƶ����
            } else
            {
              stepCount++;
              moveSteps(stepCount); //�ƶ�
              repaint();
              serviceRepaints();
            }
          }
          catch(Exception exception) { }
      }
      /**
       * �ƶ���ɫ
       * @param stepCount �ƶ�����
       */

    private void moveSteps(int stepCount)
    {
        //System.out.println("Location:" + playerLocation[nowPlayerID]);
        player_location[nowPlayerID]++;
        specialLocationPlusSteps();
        if(player_location[nowPlayerID] == 50)
        { //�������50�ŵ�Ƥ������һȦ���
            if(stepCount != 1)
            {
                status_Park10_Prinson20_Sleep30 = 1;
                player_money[nowPlayerID] += 200; //��200��Ǯ
            }
            player_location[nowPlayerID] = 0;
        }
    }
    /**
     * �����ر�ĵط�Ҫ���ߵĲ�������Ϊ����ɻ���ռ�˼�����λ�ã���Ҳֻ����һ����
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
     * �ƶ����Ҫ���������
     */

    private void stepOver()
    {
        //System.out.println("StepOverLocation:" + player_location[nowPlayerID]);
        //playerLocation[nowPlayerID] = 49 ;
        switch(player_location[nowPlayerID])
        {
        case 2: //
        case 20: //
        case 42: // ����¼�
            randomEvent();
            return;

        case 6: //
        case 37: // �ɻ���
            showFlyToNextStation();
            return;

        case 11: //
        case 25: //
        case 32: // ��Ƭ
            addCard(10);
            return;

        case 16: // ����
            prise();
            return;

        case 28: //
        case 45: // ��԰
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
     * �ƶ���ϵ���һ��ĵ�Ƥ��Ҫ���������
     */

    private void onCommonGround()
    {
       // �����0-�յأ�-1 -- �������ù������أ�-2 --˥�����ù�������
        if(groundForBuilding[player_location[nowPlayerID]] <= 0)
        {
            if(player_money[nowPlayerID] >= fetchGroundPrice(player_location[nowPlayerID]) && player_money[nowPlayerID] != 0)
                buildorBuyGround();
            else
                nextPlay(1000);
        } else
        if(isNowPlayerIDGround()) //������Լ��ĵ�Ƥ
        {   //������㹻��Ǯ�޽�
            if(player_money[nowPlayerID] >= fetchGroundPrice(player_location[nowPlayerID]))
            {
                int actorLevel = 1;
                for(int i = 0; i < nowPlayerID; i++)
                    actorLevel *= 10;
                    //���������߼������Ϳ����޽�
                if(groundForBuilding[player_location[nowPlayerID]] == 4 * actorLevel)
                    nextPlay(1000);
                else
                    buildorBuyGround();
            } else
            {
                nextPlay(1000);
            }
        } else //������ص������ߴ��ڲ����ƶ�״̬
        if(player_prinson_canNotMoveNum[fetchNowPlayerLocationGroundBelongtoWho()] > 0 || player_sleep_canNotMoveNum[fetchNowPlayerLocationGroundBelongtoWho()] > 0)
            nextPlay(1000);
        else //��Ǯ����·��������
            decreaseMoney(fetchNowPlayerLocationGroundBelongtoWho(), fetchConnectedLocationPrice(player_location[nowPlayerID]));
    }
    /**
     * ���ƻ���
     */

    protected void paint(Graphics graphics)
    {
      // �����Ϸ���ڽ���
      if(gameStatusGoOrWinOrFail == 0)
      {
        //����
        bufferImageG.setColor(255,255,255);
        bufferImageG.fillRect(0, 0, canvasWidth, canvasHeight);

        int mapX = (40 - computePlayerX(player_location[nowPlayerID])) + -6;
        int mapY = (56 - computePlayerY(player_location[nowPlayerID])) + -16;

        // ������Ƥ
        for(int i = 0; i < 50; i++) {
          //�����Ƥ�����仯
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

            //����ʹ�ù�������
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

            // ˥��ʹ�ù�������
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

            // ���1
            if (groundForBuilding[i] < 10) {
              g1.drawImage(player_houseImage[player_sequence[0]][groundForBuilding[
                           i] - 1], computeGroundX(i),
                           computeGroundY(i), 20);
              g2.drawImage(player_houseImage[player_sequence[0]][groundForBuilding[
                           i] - 1], -128 + computeGroundX(i),
                           computeGroundY(i), 20);
              continue;
            }
            //���2
            if (groundForBuilding[i] < 100) {
              g1.drawImage(player_houseImage[player_sequence[1]][groundForBuilding[
                           i] / 10 - 1], computeGroundX(i),
                           computeGroundY(i), 20);
              g2.drawImage(player_houseImage[player_sequence[1]][groundForBuilding[
                           i] / 10 - 1], -128 + computeGroundX(i),
                           computeGroundY(i), 20);
              continue;
            }
            //���3
            g1.drawImage(player_houseImage[player_sequence[2]][groundForBuilding[
                         i] / 100 - 1], computeGroundX(i), computeGroundY(i), 20);
            g2.drawImage(player_houseImage[player_sequence[2]][groundForBuilding[
                         i] / 100 - 1], -128 + computeGroundX(i),computeGroundY(i), 20);
          }

        }
        //���Ƶ�ͼ
        try
        {
          bufferImageG.drawImage(mapImage1, (34 - computePlayerX(player_location[nowPlayerID])), (40 - computePlayerY(player_location[nowPlayerID])), 20);
          bufferImageG.drawImage(mapImage2, (162 - computePlayerX(player_location[nowPlayerID])), (40 - computePlayerY(player_location[nowPlayerID])), 20);
        }
        catch(Exception exception) { }


        // �����ǵ�ǰ��Ϸ�ߵ�ͷ��
        try
        {
          for(int i = 2; i >= 0; i--)
            if(i != nowPlayerID && player_money[i] >= 0)
              bufferImageG.drawImage(player_faceImage[player_sequence[i]], mapX + computePlayerX(player_location[i]), mapY + computePlayerY(player_location[i]), 20);
        }
        catch(Exception exception5) { }

        // ����������
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
           // �����������ϵ�ͷ��
           bufferImageG.drawImage(player_faceImage[player_sequence[nowPlayerID]], 79, 4, 20);
           // ������Ϸ��ͷ��
           if(player_money[nowPlayerID] >= 0)
             bufferImageG.drawImage(player_faceImage[player_sequence[nowPlayerID]], 34, 40, 20);
         }
         catch(Exception exception8) { }
         //����ѡ���
         bufferImageG.setColor(255,255,255);
         bufferImageG.drawRect(78, 23 + choiceIndex * 14, 17, 13);

       }
       //�����һ�ʤ
       if(gameStatusGoOrWinOrFail == 2)
       {
         try
         {
           bufferImageG.drawImage(Image.createImage("/res/image/start00bw.png"), 0, 0, 20);
           bufferImageG.drawImage(Image.createImage("/res/image/win.png"), 20, 22, 20);
         }
         catch(Exception exception1) { }
       }
       //������ʧ��
       if(gameStatusGoOrWinOrFail == 3)
       {
         try
         {
           bufferImageG.drawImage(Image.createImage("/res/image/start00bw.png"), 0, 0, 20);
           bufferImageG.drawImage(Image.createImage("/res/image/lost.png"), 17, 22, 20);
         }
         catch(Exception exception2) { }
       }
       //���������ɫ״̬
       if(status_Park10_Prinson20_Sleep30 == 30)
         drawMessage(bufferImageG, "˯��");
       else
       if(status_Park10_Prinson20_Sleep30 == 20)
         drawMessage(bufferImageG, "����");
       else
       if(status_Park10_Prinson20_Sleep30 == 10)
         drawMessage(bufferImageG, "��԰");
       else
       if(status_Park10_Prinson20_Sleep30 > 0)
       {
         drawMessage(bufferImageG, "+ $200");
         status_Park10_Prinson20_Sleep30 = status_Park10_Prinson20_Sleep30 != 4 ? ++status_Park10_Prinson20_Sleep30 : 0;
       }
       //���������ͼ
       graphics.drawImage(bufferImage,0,0,20);
    }
    /**
     * ���Ƴ������ɫ״̬
     * @param g ���
     * @param s ״̬��Ϣ
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
     * ��������
     * @param keyInt ��ֵ
     */

    public void keyPressed(int keyInt)
    {
      // ������ʧ�ܻ��ǳɹ�
      if (gameStatusGoOrWinOrFail > 1)
        KMRichMan.exitGame();
      // ���������ҽ�����Ϸ
      if (nowPlayerID != 0 || ManControlStatus_CanNotPlay)
        return;
      int pressKey = getGameAction(keyInt);

      switch (pressKey) {
        default:
          break;

        case 1: // ��
        case 2:
          choiceIndex--;
          if (choiceIndex == -1)
            choiceIndex = 3;
          repaint();
          serviceRepaints();
          break;

        case 5: // ��
        case 6:
          choiceIndex++;
          if (choiceIndex == 4)
            choiceIndex = 0;
          repaint();
          serviceRepaints();
          break;

        case 8: // ȷ��
          switch (choiceIndex) {
            default:
              break;

            case 0: // Ͷɫ��
              richMan.setDisplayToDiceCanvas();
              ManControlStatus_CanNotPlay = true;
              break;

            case 1: // ��Ƭ
              if (player_cards[nowPlayerID][0] == 16)
                richMan.showNoCard();
              else
                richMan.showCards(player_cards[nowPlayerID]);
              break;

            case 2: // ��ʾ
              this.richMan.setDisplayToFucntionList();
              break;

            case 3: // ѡ��
              this.richMan.setDisplayToOptionList();
              break;
          }
          break;
      }
    }
    /**
     * ����¼���������ɫ�ߵ�������
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
            s = "����(����ͣ��)2��";
            break;

        case 1: // '\001'
            player_money[nowPlayerID] += 300;
            s = "�齱Ӯ�� + $300";
            break;

        case 2: // '\002'
            s = "��òƸ��� 10%\n" + " + $".concat(String.valueOf(player_money[nowPlayerID] / 10));
            player_money[nowPlayerID] = (player_money[nowPlayerID] * 110) / 100;
            break;

        case 3: // '\003'
            s = "����Ͷ��ʧ��!\n��ʧ�Ʋ�10%\n" + " + $".concat(String.valueOf(String.valueOf((player_money[nowPlayerID] * 90) / 100)));
            player_money[nowPlayerID] = (player_money[nowPlayerID] * 90) / 100;
            break;

        case 4: // '\004'
            s = "����˰ 5%\n" + " - $".concat(String.valueOf(String.valueOf(player_money[nowPlayerID] / 20)));
            player_money[nowPlayerID] = (player_money[nowPlayerID] * 95) / 100;
            break;

        case 5: // '\005'
            s = "�Ʋ�˰ 5%\n" + " - $".concat(String.valueOf(String.valueOf(fetchBuildingTax() / 20)));
            player_money[nowPlayerID] -= fetchBuildingTax() / 20;
            break;

        case 6: // '\006'
            tricleBroadCast(15);
            s = "����";
            break;

        case 7: // '\007'
            tricleBroadCast(25);
            s = "�����";
            break;

        case 8: // '\b'
            fireGround();
            s = "��";
            break;

        case 9: // '\t'
            player_money[nowPlayerID] += 200;
            s = "�ز�Ͷ�ʻ���\n+ $200";
            break;
        }
        if(random != 6 && random != 7 && random != 8)
            richMan.showChance(s);
    }
    /**
     * �������
     */

    private void prise()
    {
        player_prinson_canNotMoveNum[nowPlayerID] += 3;
        richMan.showInPrisonMessage();
    }
    /**
     * ��ʾ�ɵ���һ���ɻ�����Ϣ
     */

    private void showFlyToNextStation()
    {
        richMan.showBroadCast("�ɵ���һ������.", null, true, "����");
    }
    /**
     * �ɵ��·ɻ���
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
     * ʹ�ÿ�Ƭ
     * @param cardID ��Ƭ��ID
     */

    void useCard(int cardID)
    {
      switch(cardID)
      {
        case 4: // ���￨
        default:
          break;

        case 0: // ͵����
          int cardNum = stealCard();
          if(cardNum == -1)
            richMan.showCardUsedMessage("û�п�Ƭ��͵.");
          else {
            deleteCard(nowPlayerID, cardID);
            addCard(cardNum);
          }
          break;

        case 1: // ������
          deleteCard(nowPlayerID, cardID);
          int money = 0;
          for(int i = 0; i < 3; i++)
            if(i != nowPlayerID)
            {
              player_money[i] /= 2;
              money += player_money[i];
            }

          player_money[nowPlayerID] += money;
          richMan.showCardUsedMessage("��� + $".concat(String.valueOf(money)));
          break;

        case 2: // ǿռ��
          if(!isNowPlayerIDGround()) {
            occupyGround();
            deleteCard(nowPlayerID, cardID);
          }
          else {
            richMan.showCardUsedMessage("�㲻�����������ſ�Ƭ.");
          }
          break;

        case 3: // ˯�߿�
          deleteCard(nowPlayerID, cardID);
          int playerID;
          for(playerID = fetchRandom(2); playerID == nowPlayerID; playerID = fetchRandom(2));
          player_sleep_canNotMoveNum[playerID] += 3;
          player_prinson_canNotMoveNum[playerID] = 0;
          richMan.showCardUsedMessage("��Ҳ����ƶ�����.");
          break;

        case 5: // ���޿�
          deleteCard(nowPlayerID, cardID);
          tricleBroadCast(5);
          break;

        case 6: // ��ʹ��
          deleteCard(nowPlayerID, cardID);
          tricleBroadCast(11);
          break;

        case 7: // �ֽ�
          deleteCard(nowPlayerID, cardID);
          richMan.setDisplayToDiceForm(player_money, nowPlayerID);
          break;

        case 8: // ����
          if(isEmptyGround(player_location[nowPlayerID]))
          {
            groundForBuilding[player_location[nowPlayerID]] = -1;
            deleteCard(nowPlayerID, cardID);
            nextPlay(1000);
          } else
          {
            richMan.showCardUsedMessage("�㲻������ʹ��.");
          }
          break;

        case 9: // ˥��
          if(isEmptyGround(player_location[nowPlayerID]))
          {
            groundForBuilding[player_location[nowPlayerID]] = -2;
            deleteCard(nowPlayerID, cardID);
            nextPlay(1000);
          } else
          {
            richMan.showCardUsedMessage("�㲻������ʹ��.");
          }
          break;
      }
    }
    /**
     * �ж��Ƿ��ǿյ�
     * @param groundLocation �յ�λ��
     */

    private boolean isEmptyGround(int groundLocation)
    {
      return groundLocation != 2 && groundLocation != 6 && groundLocation != 11 && groundLocation != 16 && groundLocation != 20 && groundLocation != 25 && groundLocation != 28 && groundLocation != 32 && groundLocation != 37 && groundLocation != 42 && groundLocation != 45 && groundLocation != 49 && groundForBuilding[groundLocation] == 0;
    }
    /**
     * ǿռ����
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
            richMan.showCardUsedMessage("���������������.");
            break;
        }
    }
    /**
     * ɾ����Ƭ
     * @param playID ��ɫID
     * @param cardID ��ƬID
     */

    private boolean deleteCard(int playID, int cardID)
    {
      boolean flag = false;
      //�ж���û�����ſ�
      for(int i = 0; i < player_cards[playID].length; i++)
        if(player_cards[playID][i] == cardID)
          flag = true;

      if(!flag)
        return false;

      for(int i = 0; i < player_cards[playID].length; i++)
        if(player_cards[playID][i] == cardID)
        {
          player_cards[playID][i] = 16;
          i = 5; // ����ѭ��������ֵ����á����Ǻ�
        }
      // �������п�Ƭ
      for(int i = 0; i < 4; i++)
        if(player_cards[playID][i] == 16)
        {
          player_cards[playID][i] = player_cards[playID][i + 1];
          player_cards[playID][i + 1] = 16;
        }
      return true;
    }
    /**
     * ͵��Ƭ
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
     * ���ӿ�Ƭ
     * @param ��ƬID
     */

    private void addCard(int cardID)
    {
      if(cardID == 10) // ������ӿ�Ƭ
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
     * ǿ�����ӿ�Ƭ��5�ſ�Ƭ������ʱ��
     * @param ��ƬID
     */

    private void forceAddCard(int cardNum)
    {
      for(int i = 0; i < 4; i++)
        player_cards[nowPlayerID][i] = player_cards[nowPlayerID][i + 1];
      player_cards[nowPlayerID][4] = cardNum;
      richMan.showCardMessage(cardNum);
    }
    /**
     * ���һ���������>=0 and <= number)
     * @param number �������������
     */

    private int fetchRandom(int number)
    {
        return (random.nextInt() % (number + 1) + (number + 1)) % (number + 1);
    }
    /**
     * ���һ�����������ȥһ����
     * @param maxNum �����
     * @param excludeNum �ų���
     */

    private int fetchRandomIn0ToMaxNumExceptExcludeNum(int maxNum, int excludeNum)
    {
        int id;
        for(id = fetchRandom(maxNum); id == excludeNum; id = fetchRandom(maxNum));
        return id;
    }
    /**
     * ����
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
        richMan.showBroadCast("��Щ���򱻻��ִݻ���:", groundIDs, nowPlayerID != 0, "��");
    }
    /**
     * �ƻ���Ƥ����Ϊ���֣�
     * @param beginIndex ��Ƥ��ʼ���
     * @param endIndex ��Ƥ�������
     * @param j1 �Ҷ���֪����ʲô�õġ����Ǻǡ���j1������û���ã�
     */

    private void firedetroyGround(int beginIndex, int endIndex, int j1)
    {
        for(int k1 = beginIndex; k1 < endIndex + 1; k1++)
            if(groundForBuilding[k1] != 0)
                groundForBuilding[k1] = groundForBuilding[k1] >= 10 ? groundForBuilding[k1] >= 100 ? 100 : 10 : 1;

    }
    /**
     * �Զ���ȡ�������Ƥ������ȥ�����ɻ����ȣ�
     */

    private int autoFetchNotSpecialGround()
    {
        int l = fetchRandom(47);
        if(l == 2 || l == 5 || l == 6 || l == 10 || l == 11 || l == 14 || l == 15 || l == 16 || l == 20 || l == 25 || l == 27 || l == 28 || l == 32 || l == 36 || l == 37 || l == 41 || l == 42 || l == 45)
            l = autoFetchNotSpecialGround();
        return l;
    }
    /**
     * ��ȡ�����ӵ�����
     * @param groundLocation ��Ƥλ��
     */

    private int groundNotConnected(int groundLocation)
    {
        return groundLocation >= 2 ? groundLocation >= 5 ? groundLocation >= 10 ? ((int) (groundLocation >= 14 ? ((int) (groundLocation >= 20 ? ((int) (groundLocation >= 25 ? ((int) (groundLocation >= 32 ? ((int) (groundLocation >= 36 ? ((int) (groundLocation >= 41 ? 9 : 8)) : 7)) : 6)) : 5)) : 4)) : 3)) : 2 : 1 : 0;
    }
    /**
     * �����㲥������ȣ�
     * @param broadCastType �㲥����
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
            groundForBuilding[27] = 0; //���ر�Ŷ��
            groundForBuilding[28] = 0;//���ر�Ŷ������CASE��û��
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
            richMan.showBroadCast("��ʹף��:\n", groundIDs, nowPlayerID != 0, "����");
        else
        if(broadCastType == 5)
            richMan.showBroadCast("����Ϯ��\n:", groundIDs, nowPlayerID != 0, "����");
        else
        if(broadCastType == 15)
            richMan.showBroadCast("����ݻ�:\n", groundIDs, nowPlayerID != 0, "����");
        else
        if(broadCastType == 25)
            richMan.showBroadCast("�����ٻ�:\n", groundIDs, nowPlayerID != 0, "����");
    }
    /**
     * �㲥�ı䣨����ȣ�
     * @param beginID ��ʼ��Ƥ���
     * @param endID ������Ƥ���
     * @param broadCastType �㲥����
     */

    private void broadCastChangeGround(int beginID, int endID, int broadCastType)
    {
        switch(broadCastType)
        {
        case 11: // ��ʹף��
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

        case 15: // ����ݻ�
            for(int l1 = beginID; l1 < endID + 1; l1++)
                groundForBuilding[l1] = 0;

            break;

        default: // �����ٻ�,����Ϯ��
            destroyGround(beginID, endID);
            break;
        }
    }
    /**
      * ������ƻ�����
      * @param beginID ��ʼ��Ƥ���
      * @param endID ������Ƥ���
      */


    private void destroyGround(int beginID, int endID)
    {
        for(int j1 = beginID; j1 < endID + 1; j1++)
            if(groundForBuilding[j1] != 0)
                groundForBuilding[j1] = groundForBuilding[j1] >= 10 ? groundForBuilding[j1] >= 100 ? 100 : 10 : 1;

    }
    /**
     * �������أ�������Ƚ���
     * @param groundLocation ����λ��
     */

    private void adjustGroundOverBuilding(int groundLocation)
    {
        groundForBuilding[groundLocation] = groundForBuilding[groundLocation] != 5 ? groundForBuilding[groundLocation] != 50 ? groundForBuilding[groundLocation] != 500 ? groundForBuilding[groundLocation] : 400 : 40 : 4;
    }
    /**
     * ��òƲ�˰
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
     * �ж��Ƿ��ǵ�ǰ��ɫ�ĵ�Ƥ
     */

    private boolean isNowPlayerIDGround()
    {
        if(groundForBuilding[player_location[nowPlayerID]] == 0)
            return true;
        else
            return nowPlayerID == (groundForBuilding[player_location[nowPlayerID]] >= 10 ? groundForBuilding[player_location[nowPlayerID]] >= 100 ? 2 : 1 : 0);
    }
    /**
     * ��ػ��ǼӸǷ���
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
     * ��Ǯ���ߵ����˵ĵ�Ƥ�ϣ�
     * @param groundBelongtoWho ˭������
     * @param price  ��·��
     */

    private void decreaseMoney(int groundBelongtoWho, int price)
    {
        player_money[nowPlayerID] -= price;
        player_money[groundBelongtoWho] += price;
        // �����ɫǮ < 0 �������е��������
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
     * �жϵ�ǰ�ĵ�ǰ��ɫ������Ƥ��˭�ġ�
     */

    private int fetchNowPlayerLocationGroundBelongtoWho()
    {
        if(groundForBuilding[player_location[nowPlayerID]] < 10)
            return 0;
        else
            return groundForBuilding[player_location[nowPlayerID]] >= 100 ? 2 : 1;
    }
    /**
     * ��õ�Ƥ�ļ۸�
     * @param groundID ��ƤID
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
     * ��������ĵ�Ƥ�۸�
     * @param id ��ƤID
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
     * ��õ�Ƥ�۸�
     * @param groundBuilding ��Ƥ�ļ���
     * @param groudID ��ƤID
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
     * ��õ�Ƥ�۸�
     * @param ��ƤID
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
     * �����Ƥ������
     * @param groundValue ��Ƥֵ
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
     * �����ɫ�ڵ�ͼ�ϵ�����X
     * @param id ��ƤID
     */

    private int computePlayerX(int id)
    {
        return id <= 44 && id >= 3 ? id >= 7 ? id >= 12 ? id >= 16 ? id >= 21 ? id >= 25 ? id >= 33 ? id >= 45 ? 0 : 208 - 16 * (id - 33) : 224 : 160 + 16 * (id - 21) : 144 : 96 + 16 * (id - 12) : 80 : 32 + 16 * (id - 3) : 16;
    }
    /**
     * �����ɫ�ڵ�ͼ�ϵ�����Y
     * @param id ��ƤID
     */

    private int computePlayerY(int id)
    {
        return id >= 3 ? id >= 7 ? id >= 11 ? id >= 16 ? id >= 20 ? id >= 26 ? id >= 32 ? id >= 46 ? id >= 50 ? 0 : 32 + 16 * (id - 46) : 16 : 112 - 16 * (id - 26) : 128 : 64 + 16 * (id - 16) : 48 : 112 - 16 * (id - 7) : 128 : 96 + 16 * id;
    }
    /**
     * �����Ƥ�ڵ�ͼ�ϵ�����X
     * @param id ��ƤID
     */

    private int computeGroundX(int id)
    {
        return id >= 2 ? id >= 5 ? id >= 10 ? id >= 14 ? id >= 20 ? id >= 25 ? id >= 32 ? id >= 45 ? ((int) (id >= 48 ? -200 : 0)) : 208 - 16 * (id - 33) : 240 : 160 + 16 * (id - 21) : 128 : 96 + 16 * (id - 12) : 64 : 32 + 16 * (id - 3) : 0;
    }
    /**
     * �����ɫ�ڵ�ͼ�ϵ�����Y
     * @param id ��ƤID
     */

    private int computeGroundY(int id)
    {
        return id >= 2 ? id >= 5 ? id >= 10 ? id >= 14 ? id >= 20 ? id >= 25 ? id >= 32 ? id >= 45 ? id >= 48 ? 0 : 32 + 16 * (id - 46) : 0 : 112 - 16 * (id - 26) : 144 : 80 + 16 * (id - 17) : 64 : 112 - 16 * (id - 7) : 144 : 96 + 16 * id;
    }
    /**
     * ��ʼ����Ϸ
     */

    public void initialGame()
    {
      //��ɫ
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
      //��Ʊ
      this.stock_name = new String[4];
      stock_name[0] = "�ཿع�";
      stock_name[1] = "����ҽ��";
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
      //����
      choiceIndex = 0;
      gameStatusGoOrWinOrFail = 0;
      turnCount = 0;
      nowPlayerID = 0;
      ManControlStatus_CanNotPlay = false;
    }
    /**
     * ���ƹ�Ʊ����
     */

    private void changeStock() {
      for (int i = 0; i < 4; i++ ){
        stock_amplitude[i] += (7 - this.fetchRandom(14));
        if (stock_amplitude[i] > 10)
          stock_amplitude[i] = 10; //�Ƿ����ܴ���10%
        if (stock_amplitude[i] < -10)
          stock_amplitude[i] = -10; //�Ƿ�����С��-10%
        int price = stock_price[i] * 10000 + stock_price_fraction[i];
        price = price * (100 + stock_amplitude[i]) / 100;
        stock_price[i] = price / 10000;
        stock_price_fraction[i] = price - stock_price[i] * 10000;
        if (stock_price[i] < 2) { //�۸���С�� 2��Ǯ
          stock_price[i] = 2;
        }
      }
    }
    /**
     * ��Ϸ����
     * @param status �������
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
     * �Զ������ļ�
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
     * �����Ϸ��¼
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
     * ������Ϸ
     */

    public byte[] saveGame()
    {
        byte bytes[] = null;
        try
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
            // �������
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
            // ��Ƥ����
            for(int i = 0; i < groundForBuilding.length; i++)
              dataoutputstream.writeInt(groundForBuilding[i]);
              // ��Ʊ����
            for(int i = 0; i < 4; i++) {
              dataoutputstream.writeInt(this.stock_price[i]);
              dataoutputstream.writeInt(this.stock_amplitude[i]);
              dataoutputstream.writeInt(this.stock_price_fraction[i]);
            }
            // ��������
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
     * �ؽ���Ϸ��load game)
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
     * ��������
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

        case -2: //˥�������
            player_money[nowPlayerID] -= fetchGroundPrice(player_location[nowPlayerID]);
            break;
        case -1: //���������
          break;
        }
        if(groundForBuilding[player_location[nowPlayerID]] == -2)
            groundForBuilding[player_location[nowPlayerID]] = 0; // �ָ�˥�������
        else
            groundForBuilding[player_location[nowPlayerID]] += nowPlayerID != 0 ? nowPlayerID != 1 ? 100 : 10 : 1;
        adjustGroundOverBuilding(player_location[nowPlayerID]);
    }
    /**
     * ��������������Ϸ
     */

    void dealWithComputerActorPlay()
    {
      ManControlStatus_CanNotPlay = true;
      int cardIndex = -1;
      //�Զ���ÿ�Ƭ
      if(player_cards[nowPlayerID][0] != 16)
        cardIndex = autoFetchCardIndex();
      if(cardIndex != -1 &&
         this.fetchRandom(5) > 3) // ������2/6����ʹ�ÿ�Ƭ
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
     * �Զ���ÿ�Ƭ
     */

    private int autoFetchCardIndex()
    {
      int cardCount = 0;
      //���㿨Ƭ����Ŀ
      for(int i1 = 0; i1 < 5; i1++)
        if(player_cards[nowPlayerID][i1] != 16)
          cardCount++;

      for(int index = 0; index < cardCount; index++)
        switch(player_cards[nowPlayerID][index])
        {
          case 4: // ���￨
          default:
            break;

          case 2: // ǿռ��
            if(!isNowPlayerIDGround())
              return index;
            break;

          case 8: // ����
          case 9: // ˥��
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

          case 0: //������Ƭ
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
     * ���ñ�����
     * @param isLightOn �Ƿ񿪵�
     */

  public void setIsLightOn(boolean isLightOn) {
    this.isLightOn = isLightOn;
    if (this.isLightOn)
      com.siemens.mp.game.Light.setLightOn();
    else
      com.siemens.mp.game.Light.setLightOff();
  }
  /**
   * ��������
   * @param isMusicOn �Ƿ������
   */

  public void setIsMusicOn(boolean isMusicOn) {
    this.isMusicOn = isMusicOn;
    if (this.isMusicOn)
      this.composer.getMelody().play();
    else
      this.composer.getMelody().stop();
  }
  /**
   * ��ȡ������״̬
   */

  public boolean isIsLightOn() {
    return isLightOn;
  }
  /**
   * ��ȡ��������״̬
   */

  public boolean isIsMusicOn() {
    return isMusicOn;
  }
  /**
   * ��ȡ��Ϸ�ٶ�
   */

  public int getGameSpeed() {
    return gameSpeed;
  }
  /**
   * ������Ϸ�ٶ�
   */

  public void setGameSpeed(int gameSpeed) {
    this.gameSpeed = gameSpeed;
  }
  /**
   * ��ʼ������
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
   * ��ȡ�������
   */

  public int getMusic() {
    return music;
  }
  /**
   * �����������
   * @param music �������
   */

  public void setMusic(int music) {
    this.music = music;
    this.initialMusic();
  }
}