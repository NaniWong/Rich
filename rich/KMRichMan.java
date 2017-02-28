import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.*;

/**
 * ���������������������������б����ڵĵ���ת��
 * ��һЩҪ��ϵ�ܶ���Ĺ��ܵ��á�
 * @author SoftStar,����
 * @version 1.0
 */

public class KMRichMan extends MIDlet
{
    /**
     * richManʵ��
     */
    static KMRichMan richMan = null;
    /**
     * ��Ϸ����
     */
    PlayCanvas playCanvas;
    /**
     * ����һ������
     */
    public KMRichMan()
    {
        playCanvas = null;
        richMan = this;
    }

    /**
     * ������Ϸ����
     */
    void createPlayCanvas() {
      playCanvas = new PlayCanvas(this);
    }
    /**
     * ת������Ϸ���岢������һ����Ϸ��ɫ����������岻׼ȷ��
     */
    void confirmMessage()
    {
        Display.getDisplay(this).setCurrent(playCanvas);
        playCanvas.nextPlay(1000);
    }
    /**
     * ת������Ϸ���塣
     * @param flag ����������������壿������֪�������ܲ��ܿ�����˼���Ǻǡ����ǲ����������ַ������İ�
     */
    void setDisplayToPlayCanvas4(boolean flag)
    {
        Display.getDisplay(this).setCurrent(playCanvas);
        playCanvas.fly();
    }
    /**
     * ת������Ϸ���壬�������,�ɻ����йأ���ͼ�ϵģ���
     */
    void confirmNews()
    {
        Display.getDisplay(this).setCurrent(playCanvas);
        playCanvas.fly();
    }
    /**
     * ��ʾ���������Ϣ��
     */
    void showInPrisonMessage()
    {
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "����", "������ڼ���ͣ������", 4, true));
    }
    /**
     * ת������Ϸ�˵���
     */
    void setDisplayToGameMenu()
    {
        Display.getDisplay(this).setCurrent(new GameMenu(this));
    }
    /**
     * ת����ѡ��˵���
     */
    void setDisplayToOptionList()
    {
        Display.getDisplay(this).setCurrent(new SystemList(this,this.playCanvas));
    }
    /**
     * ת�������ܲ˵���
     */
    void setDisplayToFucntionList()
    {
        Display.getDisplay(this).setCurrent(new OptionList(this,this.playCanvas));
    }
    /**
     * ת������Ʊ���ڡ�
     * @para index ��Ʊ���
     */
    void setDisplayToStockForm(int index)
    {
        Display.getDisplay(this).setCurrent(new StockForm(this,this.playCanvas,index));
    }
    /**
     * ת�����������ڡ�
     */
    void setDisplayToHelpForm()
    {
        Display.getDisplay(this).setCurrent(new HelpForm(this));
    }
    /**
     * ��ʾ��Ϸ��ɫ��״̬��
     * @param playMoney1 ���1��Ǯ
     * @param stock1 ���1�Ĺ�Ʊ��ֵ
     * @param ground1 ���1�ĵ�Ƥ��Ŀ
     * @param house1 ���1�ķ�����Ŀ
     * @param hotel1 ���1���ù���Ŀ
     * @param playMoney2 ���2��Ǯ
     * @param stock2 ���2�Ĺ�Ʊ��ֵ
     * @param ground2 ���2�ĵ�Ƥ��Ŀ
     * @param house2 ���2�ķ�����Ŀ
     * @param hotel2 ���2���ù���Ŀ
     * @param playMoney3 ���3��Ǯ
     * @param stock3 ���3�Ĺ�Ʊ��ֵ
     * @param ground3 ���3�ĵ�Ƥ��Ŀ
     * @param house3 ���3�ķ�����Ŀ
     * @param hotel3 ���3���ù���Ŀ

     * @param playSequence ��Ϸ����
     */
    void showPlayerStatus(int playerMoney1, int playerMoney2, int playerMoney3,
                          int stock1, int stock2, int stock3,
                          int ground1, int ground2, int ground3,
                          int house1,  int house2, int house3,
                          int hotel1, int hotel2, int hotel3, int playerSequence[])
    {
        String playerNameArray[] = {
            "��С��", "������", "Ǯ����"
        };
        String playerMessage1 = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(
        playerNameArray[playerSequence[0]])))).append(":\n").
            append(playerMoney1 + stock1).append(" ��Ԫ�ʲ�\n ").
            append(playerMoney1).append(" ��Ԫ�ֽ�\n ").
            append(stock1).append(" ��Ԫ��Ʊ\n ").
            append(ground1).append(" ").
            append(fetchStatusMessages(0, ground1)).append("\n ").
            append(house1).append(" ").append(fetchStatusMessages(1, house1)).append("\n ").
            append(hotel1).append(" ").append(fetchStatusMessages(2, hotel1)).append("\n ")));
        String playerMessage2 = String.valueOf(String.valueOf((new StringBuffer("\n\n")).
            append(playerNameArray[playerSequence[1]]).append(":\n").
            append(playerMoney2 + stock2).append(" ��Ԫ�ʲ�\n ").
            append(playerMoney2).append(" ��Ԫ�ֽ�\n ").
            append(stock2).append(" ��Ԫ��Ʊ\n ").
            append(ground2).append(" ").
            append(fetchStatusMessages(0, ground2)).append("\n ").
            append(house2).append(" ").append(fetchStatusMessages(1, house2)).append("\n ").
            append(hotel2).append(" ").append(fetchStatusMessages(2, hotel2)).append("\n ")));
        String playerMessage3 = String.valueOf(String.valueOf((new StringBuffer("\n\n")).
            append(playerNameArray[playerSequence[2]]).append(":\n").
            append(playerMoney3 + stock3).append(" ��Ԫ�ʲ�\n ").
            append(playerMoney3).append(" ��Ԫ�ֽ�\n ").
            append(stock3).append(" ��Ԫ��Ʊ\n ").
            append(ground3).append(" ").
            append(fetchStatusMessages(0, ground3)).append("\n ").
            append(house3).append(" ").append(fetchStatusMessages(1, house3)).append("\n ").
            append(hotel3).append(" ").append(fetchStatusMessages(2, hotel3)).append("\n ")));
        if(playerMoney1 < 0)
            playerMessage1 = "���һ: �Ʋ�";
        if(playerMoney2 < 0)
            playerMessage2 = "\n��Ҷ�: �Ʋ�";
        if(playerMoney3 < 0)
            playerMessage3 = "\n�����: �Ʋ�";
        Display.getDisplay(this).setCurrent(new PlayerStatusForm(this, String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(playerMessage1)))).append(playerMessage2).append(playerMessage3)))));
    }
    /**
    * �õ��ʲ��ĵ�λ������������Կ��������Ϸ��ǰ��Ӣ�ĵģ�
    * @param type ����
    * @param number ����
    */

    String fetchStatusMessages(int type, int number)
    {
        switch(type)
        {
        case 0: // '\0'
            return number <= 1 ? "��յ�" : "��յ�";

        case 1: // '\001'
            return number <= 1 ? "������" : "������";

        case 2: // '\002'
            return number <= 1 ? "������" : "������";
        }
        return "";
    }
    /**
     * ����������Ϸ��
     * @param selectedPlayerID ѡ�����Ϸ��ɫ ��0-2����Ϸ��ɫ��3�����ϴ���Ϸ��
     */

    void continuePlay(int selectedPlayerID)
    {
        if(playCanvas == null)
            playCanvas = new PlayCanvas(this);
        if(selectedPlayerID < 3)
        // ��ʾ������Ϸ���� selectedPlayerID == 3ʱ����ʾ�����ϴε���Ϸ
        {
          playCanvas.composer.getMelody().play();
          playCanvas.arragePlayerSecquence(selectedPlayerID);
          Display.getDisplay(this).setCurrent(playCanvas);
        } else
        if(!playCanvas.fetchRecords())
        {
            Alert alert = new Alert("û�м�¼");
            alert.setType(AlertType.ERROR);
            alert.setString("û�м�¼���洢��.");
            alert.setTimeout(2000);
            Display.getDisplay(this).setCurrent(alert);
        } else
        {
          Display.getDisplay(this).setCurrent(playCanvas);
        }
    }
    /**
     * ת������Ϸ����
     */
    void setDisplayToPlayCanvas1()
    {
        Display.getDisplay(this).setCurrent(playCanvas);
    }
    /**
     * Ͷ��ɫ�Ӻ�ת������Ϸ����
     * @param diceNumber ɫ�ӵ���
     */
    void diceAndSetDisplayToPlayForm(int diceNumber)
    {
        Display.getDisplay(this).setCurrent(playCanvas);
        playCanvas.moveActor(diceNumber);
    }
    /**
     * Ͷ��ɫ�Ӻ�ת������Ϸ����
     * @param diceNumber ɫ�ӵ���
     * @param isGoOrAddMoney ɫ�ӵ����ǽ�ɫ�ƶ����Ǽ�Ǯ���ֽ𿨣�
     */
    void addDiceMoney(int diceNumber, boolean isGoOrAddMoney)
    {
        PlayCanvas.player_money[PlayCanvas.nowPlayerID] += diceNumber * 100;
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "��Ƭ��Ч",String.valueOf((new StringBuffer("���� $").append(diceNumber * 100).append("."))), 6, true));
    }
    /**
     * ��ػ��ڵ��Ͻ��췿��
     * @param groundLocation ��Ƥλ��
     * @param groundPrice ��Ƥ�۸�
     * @param isComputerActor �Ƿ��ǵ��Խ�ɫ
     * @param buildingLevel ����ļ����ǵ�Ƥ��һ�㷿�ݣ����Ǵ���)
     */

    void buildorBuyGround(int groundLocation, int groundPrice, boolean isComputerActor, int buildingLevel)
    {
      // ������˿��ƣ���ô��ʾ������Ϣ������ֱ�ӹ���
      if (this.playCanvas.nowPlayerID == 0) {

        if (buildingLevel == 0)
          Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "����",
              String.valueOf(new StringBuffer("   *").append(fetchGroundName(
              groundLocation)).append("*\n        [$ ").append(groundPrice).
                             append("]\n    ���빺����?")), 0, isComputerActor));
        else
        if (buildingLevel == 3)
          Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "����",
              "���뽨��һ��������?\n" +
              "���� $".concat(String.valueOf(String.valueOf(groundPrice))), 0,
                                                 isComputerActor));
        else
          Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "����",
              "���뽨��һ��������?\n" +
              "���� $".concat(String.valueOf(String.valueOf(groundPrice))), 0,
                                                 isComputerActor));
      }
      else {
        playCanvas.buyLand();
        playCanvas.nextPlay(1000);
      }

    }
    /**
     * ��ʾ��·����Ϣ
     * @param price ·��
     * @param isAuto �Ƿ����Զ����Ʒ���
     */

    void showPaymentExpense(int price, boolean isAuto)
    {
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "֧��", "��·�� $ ".concat(String.valueOf(String.valueOf(price))), 1, isAuto));
    }
    /**
     * ��ʾ��Ƭ����ϸ����
     * @param cardIDs ��Ƭ
     * @param selectedCardIndex ѡ��Ŀ�Ƭ���
     */

    void showCardDetail(int cardIDs[], int selectedCardIndex)
    {
        String s = "";
        switch(cardIDs[selectedCardIndex])
        {
        case 0: // '\0'
            s = "͵����:\n\n����:�Ӷ�����͵һ�ſ�Ƭ.\n\nʹ�ö���:����";
            break;

        case 1: // '\001'
            s = "������:\n\n����:������ÿ�������ƽ���ֽ�.\n\nʹ�ö���:����";
            break;

        case 2: // '\002'
            s = "ǿռ��:\n\n����:ǿ��ռ�ö��ֵ�����,��������ķ���.\n\nʹ��ʱ��:ͣ���ڶ��ֵ���һ������!";
            break;

        case 3: // '\003'
            s = "˯�߿�:\n\n����:ʹ����ÿ�������ԭ��˯������,˯�߹�����û���չ�·�ѵ�Ȩ��.\n\nʹ��ʱ��:�κ�ʱ��";
            break;

        case 4: // '\004'
            s = "���￨:\n\n����:���������ʱ,��ҿ��ô˿�����.\n\nʹ��ʱ��:���������ʱ.";
            break;

        case 5: // '\005'
            s = "���޿�:\n\n����:��ȫ�ݻ������ϵķ��ݺͽ���.\n\nʹ������:����ѡ��";
            break;

        case 6: // '\006'
            s = "��ʹ��:\n\n����:��һ�������Ͻ���һ������.\n\nʹ������:����";
            break;

        case 7: // '\007'
            s = "�ֽ�:\n\n����:�����������ӵ���һ�ٱ����ֽ�.\n\nʹ��ʱ��:�κ�ʱ��";
            break;

        case 8: // '\b'
            s = "����:\n\n����:��ҿ�����ѹ�������.\n\nʹ��ʱ��:����ڿյ���.";
            break;

        case 9: // '\t'
            s = "˥��:\n\n����:��Ҳ��ܹ�������.\n\nʹ��ʱ��:����ڿյ���.";
            break;
        }
        PlayMessageForm playMessageForm = new PlayMessageForm(this, "��Ƭ����", s, 5, false);
        playMessageForm.intialCardIDs(cardIDs, selectedCardIndex);
        Display.getDisplay(this).setCurrent(playMessageForm);
    }
    /**
     * ��ʾ��Ƭ����Ϣ
     * @param cardIndex ��Ƭ���
     */

    void showCardMessage(int cardIndex)
    {
        Display.getDisplay(this).setCurrent(new CardCanvas(this, null, cardIndex, true));
    }
    /**
     * ��ʾ��Ƭʹ�ù�Ч��
     * @param msg Ч��˵��
     */

    void showCardUsedMessage(String msg)
    {
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "��Ƭ��Ч", msg, 6, true));
    }
    /**
     * ʹ�ÿ�Ƭ
     * @param cardID ��Ƭ����
     */

    void useCard(int cardID)
    {
      if(cardID != 16)
        Display.getDisplay(this).setCurrent(playCanvas);
      playCanvas.useCard(cardID);
    }
    /**
     * ��ʾû�п�Ƭ��Ϣ
     */

    void showNoCard()
    {
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "û�п�Ƭ", "��û���κο�Ƭ.", 7, false));
    }
    /**
     * ��ʾ��Ƭ��Ϣ
     * @param cards ��Ƭ
     */

    void showCards(int cards[])
    {
        Display.getDisplay(this).setCurrent(new CardCanvas(this, cards, 0, false));
    }
    /**
     * ʹ�ÿ�Ƭ���ֶ����أ�
     * @param cardIDs ��Ƭ
     * @param cardIndex ��Ƭ���
     */
    void useCard_hunman(int cardIDs[], int cardIndex)
    {
        Display.getDisplay(this).setCurrent(new CardCanvas(this, cardIDs, cardIndex, false));
    }
    /**
     * ʹ�ÿ�Ƭ���Զ����أ�
     * @param cardIDs ��Ƭ
     * @param cardIndex ��Ƭ���
     */
    void useCard_ComputerActor(int cardIDs[], int cardIndex)
    {
        Display.getDisplay(this).setCurrent(new CardCanvas(this, cardIDs, cardIndex, true));
    }
    /**
     * ��ʾ�㲥��Ϣ������磬����ȣ�
     * @param message ��Ϣ
     * @param grounds ��Ƥ
     * @param isAuto �Ƿ��Զ�����
     * @param title ���ڱ���
     */
    void showBroadCast(String message, int grounds[], boolean isAuto, String title)
    {
        if(grounds != null)
        {
            for(int i = 0; i < grounds.length; i++)
            {
                message = message + " ".concat(fetchGroundName(grounds[i]).trim());
                if(i != grounds.length - 1)
                    message = message.concat(",");
            }

        }
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, title, message, 2, isAuto));
    }
    /**
     * ��ȡ��Ƥ����
     * @param location ��Ƥλ��
     */
    private String fetchGroundName(int location)
    {
        switch(location)
        {
        case 0: // '\0'
            return "  ����˹��  ";

        case 1: // '\001'
            return "  ��������  ";

        case 3: // '\003'
            return "  �������  ";

        case 4: // '\004'
            return " ��˹����԰ ";

        case 7: // '\007'
            return "   ������   ";

        case 8: // '\b'
            return "   �ߺ���   ";

        case 9: // '\t'
            return " �����溣̲ ";

        case 12: // '\f'
            return "  �¿���˹  ";

        case 13: // '\r'
            return "   ����˹   ";

        case 17: // '\021'
            return "   �ڻ���   ";

        case 18: // '\022'
            return " ��˹Τ��˹ ";

        case 19: // '\023'
            return "   ������   ";

        case 21: // '\025'
            return "    ŦԼ    ";

        case 22: // '\026'
            return "   ������   ";

        case 23: // '\027'
            return "  ��³����  ";

        case 24: // '\030'
            return "   ����˹   ";

        case 26: // '\032'
            return "   ��ʢ��   ";

        case 29: // '\035'
            return "   ����ͼ   ";

        case 30: // '\036'
            return " ��ʢ������ ";

        case 31: // '\037'
            return "    �׹�    ";

        case 33: // '!'
            return "  ������˹  ";

        case 34: // '"'
            return "   ֥�Ӹ�   ";

        case 35: // '#'
            return " ³˹Ī���� ";

        case 38: // '&'
            return " ���������� ";

        case 39: // '\''
            return "   ��ɼ�   ";

        case 40: // '('
            return "   ������   ";

        case 43: // '+'
            return "  ������ɽ  ";

        case 44: // ','
            return "   �ɽ�ɽ   ";

        case 46: // '.'
            return "�Ѱ�˹����ͷ";

        case 47: // '/'
            return "   39��ͷ   ";

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
            return "����";
        }
    }
    /**
     * ת������Ʊ�б�
     */

    void setDisplayToStockList()
    {
        Display.getDisplay(this).setCurrent(new StockList(this, this.playCanvas));
    }
    /**
     * ��ʾ�����ã���ͼ�ϵģ�������Ϣ
     * @param msg ��Ϣ
     */

    void showChance(String msg)
    {
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "����", msg, 3, true));
    }
    /**
     * ת����ɫ�Ӵ���
     */

    void setDisplayToDiceCanvas()
    {
        Display.getDisplay(this).setCurrent(new DiceCanvas(this, true));
    }
    /**
     * ת����ɫ�Ӵ���(�ֽ𿨼�Ǯ�ã�
     * @param playerMoney ��ɫ�Ľ�Ǯ (���������ò�����
     * @param playerID ��ɫ��index �����������ò�����
     */

    void setDisplayToDiceForm(int playerMoney[], int playerID)
    {
        Display.getDisplay(this).setCurrent(new DiceCanvas(this, false));
    }
    /**
     * ��ʾ��߷�
     */

    void showHighScore()
    {
      String playerNames[] = {
          "��С��", "������", "Ǯ����"
      };
      String playerData[][] = new String[3][2];
      // �õ���������߷�
      try {
        RecordStore recordstore = RecordStore.openRecordStore("RichManScore", false);
        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(
            recordstore.getRecord(1));
        DataInputStream datainputstream = new DataInputStream(
            bytearrayinputstream);
        for (int l = 0; l < 3; l++) {
          playerData[l][0] = playerNames[datainputstream.readInt()];
          playerData[l][1] = "".concat(String.valueOf(datainputstream.readInt()));
        }
        datainputstream.close();
        bytearrayinputstream.close();
        recordstore.closeRecordStore();
      }
      catch (Exception exception) {
        // Ҫ�ǳ����쳣����Ĭ��ֵ
        playerData[0][0] = "��С��";
        playerData[0][1] = "105";
        playerData[1][0] = "������";
        playerData[1][1] = "155";
        playerData[2][0] = "Ǯ����";
        playerData[2][1] = "166";
      }
      Display.getDisplay(this).setCurrent(new HighScoreCanvas(this, playerData));
    }
    /**
     * ������߷�
     * @param playerName �������
     * @param turnCount ��Ϸ���е�������3����������һ��Ϊһ�֣�
     */

    void freshHighScore(int playerName, int turnCount)
    {
        RecordStore recordstore = null;
        int score[][] = new int[3][2];
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 2; j++)
                score[i][j] = -1;

        }

        try
        {
            recordstore = RecordStore.openRecordStore("RichManScore", false);
        }
        catch(Exception exception)
        {
            score[0][0] = 0;
            score[0][1] = 105;
            score[1][0] = 1;
            score[1][1] = 155;
            score[2][0] = 2;
            score[2][1] = 166;
        }
        try
        {
            if(recordstore != null)
            {
                ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(recordstore.getRecord(1));
                DataInputStream datainputstream = new DataInputStream(bytearrayinputstream);
                for(int i = 0; i < 3; i++)
                {
                    score[i][0] = datainputstream.readInt();
                    score[i][1] = datainputstream.readInt();
                }
                datainputstream.close();
                bytearrayinputstream.close();
                recordstore.closeRecordStore();
            }
        }
        catch(Exception exception1) { }
        // �������
        if(turnCount < score[0][1])
        {
            score[2][0] = score[1][0];
            score[2][1] = score[1][1];
            score[1][0] = score[0][0];
            score[1][1] = score[0][1];
            score[0][0] = playerName;
            score[0][1] = turnCount;
        } else
        if(turnCount < score[1][1])
        {
            score[2][0] = score[1][0];
            score[2][0] = score[1][0];
            score[1][0] = playerName;
            score[1][1] = turnCount;
        } else
        if(turnCount < score[2][1])
        {
            score[2][0] = playerName;
            score[2][0] = turnCount;
        }
        byte bytes[] = null;
        // ����д���ļ�
        try
        {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            DataOutputStream dataoutputstream = new DataOutputStream(bytearrayoutputstream);
            for(int i2 = 0; i2 < 3; i2++)
            {
                for(int j2 = 0; j2 < 2; j2++)
                    dataoutputstream.writeInt(score[i2][j2]);

            }

            bytes = bytearrayoutputstream.toByteArray();
            dataoutputstream.close();
            bytearrayoutputstream.close();
        }
        catch(Exception exception2) { }
        try {
          RecordStore.deleteRecordStore("RichManScore");
        }
        catch (RecordStoreException ex) {
          System.out.println("Can't find Score!");
        }
        try
        {
          RecordStore recordstore1 = RecordStore.openRecordStore("RichManScore", true);
          recordstore1.addRecord(bytes, 0, bytes.length);
          recordstore1.closeRecordStore();
        }
        catch(Exception exception3) { }
    }
    /**
     * ��ػ���ǼӸǷ���
     */

    public void buyGroundorBuilding()
    {
        playCanvas.buyLand();
        confirmMessage(); // �����ط���
    }
    /**
     * ����ʼ��ת������ʼ����
     */

    public void startApp()
    {
        Display.getDisplay(this).setCurrent(new OpenCanvas(this));
    }
    /**
     * ��ͣ����
     */

    public void pauseApp()
    {
    }
    /**
     * �ƻ�����
     */

    public void destroyApp(boolean flag)
    {
    }
    /**
     * �˳���Ϸ
     */

    public static void exitGame()
    {
        richMan.destroyApp(true);
        richMan.notifyDestroyed();
        richMan = null;
    }

}
