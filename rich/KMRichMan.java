import java.io.*;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.*;

/**
 * 大富翁主程序。这个程序控制着所有列表，窗口的的跳转，
 * 和一些要联系很多类的功能调用。
 * @author SoftStar,嘟嘟熊
 * @version 1.0
 */

public class KMRichMan extends MIDlet
{
    /**
     * richMan实例
     */
    static KMRichMan richMan = null;
    /**
     * 游戏画板
     */
    PlayCanvas playCanvas;
    /**
     * 构造一个对象
     */
    public KMRichMan()
    {
        playCanvas = null;
        richMan = this;
    }

    /**
     * 创建游戏画板
     */
    void createPlayCanvas() {
      playCanvas = new PlayCanvas(this);
    }
    /**
     * 转换到游戏画板并换到下一个游戏角色。（这个定义不准确）
     */
    void confirmMessage()
    {
        Display.getDisplay(this).setCurrent(playCanvas);
        playCanvas.nextPlay(1000);
    }
    /**
     * 转换到游戏画板。
     * @param flag 这个参数好像无意义？？，不知道你们能不能看出意思？呵呵。。是不是用来区分方法名的啊
     */
    void setDisplayToPlayCanvas4(boolean flag)
    {
        Display.getDisplay(this).setCurrent(playCanvas);
        playCanvas.fly();
    }
    /**
     * 转换到游戏画板，与机会获得,飞机场有关（地图上的？）
     */
    void confirmNews()
    {
        Display.getDisplay(this).setCurrent(playCanvas);
        playCanvas.fly();
    }
    /**
     * 显示进入监狱消息。
     */
    void showInPrisonMessage()
    {
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "监狱", "你必须在监狱停留三天", 4, true));
    }
    /**
     * 转换到游戏菜单。
     */
    void setDisplayToGameMenu()
    {
        Display.getDisplay(this).setCurrent(new GameMenu(this));
    }
    /**
     * 转换到选项菜单。
     */
    void setDisplayToOptionList()
    {
        Display.getDisplay(this).setCurrent(new SystemList(this,this.playCanvas));
    }
    /**
     * 转换到功能菜单。
     */
    void setDisplayToFucntionList()
    {
        Display.getDisplay(this).setCurrent(new OptionList(this,this.playCanvas));
    }
    /**
     * 转换到股票窗口。
     * @para index 股票序号
     */
    void setDisplayToStockForm(int index)
    {
        Display.getDisplay(this).setCurrent(new StockForm(this,this.playCanvas,index));
    }
    /**
     * 转换到帮助窗口。
     */
    void setDisplayToHelpForm()
    {
        Display.getDisplay(this).setCurrent(new HelpForm(this));
    }
    /**
     * 显示游戏角色的状态。
     * @param playMoney1 玩家1的钱
     * @param stock1 玩家1的股票价值
     * @param ground1 玩家1的地皮数目
     * @param house1 玩家1的房子数目
     * @param hotel1 玩家1的旅馆数目
     * @param playMoney2 玩家2的钱
     * @param stock2 玩家2的股票价值
     * @param ground2 玩家2的地皮数目
     * @param house2 玩家2的房子数目
     * @param hotel2 玩家2的旅馆数目
     * @param playMoney3 玩家3的钱
     * @param stock3 玩家3的股票价值
     * @param ground3 玩家3的地皮数目
     * @param house3 玩家3的房子数目
     * @param hotel3 玩家3的旅馆数目

     * @param playSequence 游戏次序
     */
    void showPlayerStatus(int playerMoney1, int playerMoney2, int playerMoney3,
                          int stock1, int stock2, int stock3,
                          int ground1, int ground2, int ground3,
                          int house1,  int house2, int house3,
                          int hotel1, int hotel2, int hotel3, int playerSequence[])
    {
        String playerNameArray[] = {
            "孙小美", "阿土伯", "钱夫人"
        };
        String playerMessage1 = String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(
        playerNameArray[playerSequence[0]])))).append(":\n").
            append(playerMoney1 + stock1).append(" 美元资产\n ").
            append(playerMoney1).append(" 美元现金\n ").
            append(stock1).append(" 美元股票\n ").
            append(ground1).append(" ").
            append(fetchStatusMessages(0, ground1)).append("\n ").
            append(house1).append(" ").append(fetchStatusMessages(1, house1)).append("\n ").
            append(hotel1).append(" ").append(fetchStatusMessages(2, hotel1)).append("\n ")));
        String playerMessage2 = String.valueOf(String.valueOf((new StringBuffer("\n\n")).
            append(playerNameArray[playerSequence[1]]).append(":\n").
            append(playerMoney2 + stock2).append(" 美元资产\n ").
            append(playerMoney2).append(" 美元现金\n ").
            append(stock2).append(" 美元股票\n ").
            append(ground2).append(" ").
            append(fetchStatusMessages(0, ground2)).append("\n ").
            append(house2).append(" ").append(fetchStatusMessages(1, house2)).append("\n ").
            append(hotel2).append(" ").append(fetchStatusMessages(2, hotel2)).append("\n ")));
        String playerMessage3 = String.valueOf(String.valueOf((new StringBuffer("\n\n")).
            append(playerNameArray[playerSequence[2]]).append(":\n").
            append(playerMoney3 + stock3).append(" 美元资产\n ").
            append(playerMoney3).append(" 美元现金\n ").
            append(stock3).append(" 美元股票\n ").
            append(ground3).append(" ").
            append(fetchStatusMessages(0, ground3)).append("\n ").
            append(house3).append(" ").append(fetchStatusMessages(1, house3)).append("\n ").
            append(hotel3).append(" ").append(fetchStatusMessages(2, hotel3)).append("\n ")));
        if(playerMoney1 < 0)
            playerMessage1 = "玩家一: 破产";
        if(playerMoney2 < 0)
            playerMessage2 = "\n玩家二: 破产";
        if(playerMoney3 < 0)
            playerMessage3 = "\n玩家三: 破产";
        Display.getDisplay(this).setCurrent(new PlayerStatusForm(this, String.valueOf(String.valueOf((new StringBuffer(String.valueOf(String.valueOf(playerMessage1)))).append(playerMessage2).append(playerMessage3)))));
    }
    /**
    * 得到资产的单位。（从这里可以看出这个游戏以前是英文的）
    * @param type 类型
    * @param number 数量
    */

    String fetchStatusMessages(int type, int number)
    {
        switch(type)
        {
        case 0: // '\0'
            return number <= 1 ? "块空地" : "块空地";

        case 1: // '\001'
            return number <= 1 ? "座房屋" : "座房屋";

        case 2: // '\002'
            return number <= 1 ? "座大厦" : "座大厦";
        }
        return "";
    }
    /**
     * 继续进行游戏。
     * @param selectedPlayerID 选择的游戏角色 （0-2新游戏角色，3继续上次游戏）
     */

    void continuePlay(int selectedPlayerID)
    {
        if(playCanvas == null)
            playCanvas = new PlayCanvas(this);
        if(selectedPlayerID < 3)
        // 表示是新游戏，当 selectedPlayerID == 3时，表示接着上次的游戏
        {
          playCanvas.composer.getMelody().play();
          playCanvas.arragePlayerSecquence(selectedPlayerID);
          Display.getDisplay(this).setCurrent(playCanvas);
        } else
        if(!playCanvas.fetchRecords())
        {
            Alert alert = new Alert("没有纪录");
            alert.setType(AlertType.ERROR);
            alert.setString("没有纪录被存储过.");
            alert.setTimeout(2000);
            Display.getDisplay(this).setCurrent(alert);
        } else
        {
          Display.getDisplay(this).setCurrent(playCanvas);
        }
    }
    /**
     * 转换到游戏画板
     */
    void setDisplayToPlayCanvas1()
    {
        Display.getDisplay(this).setCurrent(playCanvas);
    }
    /**
     * 投完色子后转换到游戏画板
     * @param diceNumber 色子点数
     */
    void diceAndSetDisplayToPlayForm(int diceNumber)
    {
        Display.getDisplay(this).setCurrent(playCanvas);
        playCanvas.moveActor(diceNumber);
    }
    /**
     * 投完色子后转换到游戏画板
     * @param diceNumber 色子点数
     * @param isGoOrAddMoney 色子点数是角色移动还是加钱（现金卡）
     */
    void addDiceMoney(int diceNumber, boolean isGoOrAddMoney)
    {
        PlayCanvas.player_money[PlayCanvas.nowPlayerID] += diceNumber * 100;
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "卡片功效",String.valueOf((new StringBuffer("你获得 $").append(diceNumber * 100).append("."))), 6, true));
    }
    /**
     * 买地或在地上建造房子
     * @param groundLocation 地皮位置
     * @param groundPrice 地皮价格
     * @param isComputerActor 是否是电脑角色
     * @param buildingLevel 建造的级别（是地皮，一般房屋，还是大厦)
     */

    void buildorBuyGround(int groundLocation, int groundPrice, boolean isComputerActor, int buildingLevel)
    {
      // 如果是人控制，那么显示购买信息。否则直接购买
      if (this.playCanvas.nowPlayerID == 0) {

        if (buildingLevel == 0)
          Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "土地",
              String.valueOf(new StringBuffer("   *").append(fetchGroundName(
              groundLocation)).append("*\n        [$ ").append(groundPrice).
                             append("]\n    你想购买吗?")), 0, isComputerActor));
        else
        if (buildingLevel == 3)
          Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "土地",
              "你想建造一座大厦吗?\n" +
              "费用 $".concat(String.valueOf(String.valueOf(groundPrice))), 0,
                                                 isComputerActor));
        else
          Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "土地",
              "你想建造一座房屋吗?\n" +
              "费用 $".concat(String.valueOf(String.valueOf(groundPrice))), 0,
                                                 isComputerActor));
      }
      else {
        playCanvas.buyLand();
        playCanvas.nextPlay(1000);
      }

    }
    /**
     * 显示过路费信息
     * @param price 路费
     * @param isAuto 是否是自动控制返回
     */

    void showPaymentExpense(int price, boolean isAuto)
    {
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "支付", "过路费 $ ".concat(String.valueOf(String.valueOf(price))), 1, isAuto));
    }
    /**
     * 显示卡片的详细介绍
     * @param cardIDs 卡片
     * @param selectedCardIndex 选择的卡片序号
     */

    void showCardDetail(int cardIDs[], int selectedCardIndex)
    {
        String s = "";
        switch(cardIDs[selectedCardIndex])
        {
        case 0: // '\0'
            s = "偷盗卡:\n\n功能:从对手那偷一张卡片.\n\n使用对象:对手";
            break;

        case 1: // '\001'
            s = "均富卡:\n\n功能:从其他每个玩家那平分现金.\n\n使用对象:对手";
            break;

        case 2: // '\002'
            s = "强占卡:\n\n功能:强制占用对手的土地,包括上面的房屋.\n\n使用时机:停留在对手的任一土地上!";
            break;

        case 3: // '\003'
            s = "睡眠卡:\n\n功能:使其他每个玩家在原地睡眠三天,睡眠过程中没有收过路费的权力.\n\n使用时机:任何时间";
            break;

        case 4: // '\004'
            s = "免罪卡:\n\n功能:当进入监狱时,玩家可用此卡出狱.\n\n使用时机:被关入监狱时.";
            break;

        case 5: // '\005'
            s = "怪兽卡:\n\n功能:完全摧毁土地上的房屋和建筑.\n\n使用区域:任意选择";
            break;

        case 6: // '\006'
            s = "天使卡:\n\n功能:在一块土地上建造一座建筑.\n\n使用区域:任意";
            break;

        case 7: // '\007'
            s = "现金卡:\n\n功能:增加所掷骰子点数一百倍的现金.\n\n使用时机:任何时间";
            break;

        case 8: // '\b'
            s = "财神卡:\n\n功能:玩家可以免费购买土地.\n\n使用时机:玩家在空地上.";
            break;

        case 9: // '\t'
            s = "衰神卡:\n\n功能:玩家不能购买土地.\n\n使用时机:玩家在空地上.";
            break;
        }
        PlayMessageForm playMessageForm = new PlayMessageForm(this, "卡片详情", s, 5, false);
        playMessageForm.intialCardIDs(cardIDs, selectedCardIndex);
        Display.getDisplay(this).setCurrent(playMessageForm);
    }
    /**
     * 显示卡片的信息
     * @param cardIndex 卡片序号
     */

    void showCardMessage(int cardIndex)
    {
        Display.getDisplay(this).setCurrent(new CardCanvas(this, null, cardIndex, true));
    }
    /**
     * 显示卡片使用过效果
     * @param msg 效果说明
     */

    void showCardUsedMessage(String msg)
    {
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "卡片功效", msg, 6, true));
    }
    /**
     * 使用卡片
     * @param cardID 卡片号码
     */

    void useCard(int cardID)
    {
      if(cardID != 16)
        Display.getDisplay(this).setCurrent(playCanvas);
      playCanvas.useCard(cardID);
    }
    /**
     * 显示没有卡片信息
     */

    void showNoCard()
    {
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "没有卡片", "你没有任何卡片.", 7, false));
    }
    /**
     * 显示卡片信息
     * @param cards 卡片
     */

    void showCards(int cards[])
    {
        Display.getDisplay(this).setCurrent(new CardCanvas(this, cards, 0, false));
    }
    /**
     * 使用卡片（手动返回）
     * @param cardIDs 卡片
     * @param cardIndex 卡片序号
     */
    void useCard_hunman(int cardIDs[], int cardIndex)
    {
        Display.getDisplay(this).setCurrent(new CardCanvas(this, cardIDs, cardIndex, false));
    }
    /**
     * 使用卡片（自动返回）
     * @param cardIDs 卡片
     * @param cardIndex 卡片序号
     */
    void useCard_ComputerActor(int cardIDs[], int cardIndex)
    {
        Display.getDisplay(this).setCurrent(new CardCanvas(this, cardIDs, cardIndex, true));
    }
    /**
     * 显示广播信息（龙卷风，地震等）
     * @param message 消息
     * @param grounds 地皮
     * @param isAuto 是否自动返回
     * @param title 窗口标题
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
     * 获取地皮名字
     * @param location 地皮位置
     */
    private String fetchGroundName(int location)
    {
        switch(location)
        {
        case 0: // '\0'
            return "  阿拉斯加  ";

        case 1: // '\001'
            return "  安克雷奇  ";

        case 3: // '\003'
            return "  佛罗里达  ";

        case 4: // '\004'
            return " 迪斯尼乐园 ";

        case 7: // '\007'
            return "   夏威夷   ";

        case 8: // '\b'
            return "   瓦胡岛   ";

        case 9: // '\t'
            return " 哈奇奇海滩 ";

        case 12: // '\f'
            return "  德克萨斯  ";

        case 13: // '\r'
            return "   达拉斯   ";

        case 17: // '\021'
            return "   内华达   ";

        case 18: // '\022'
            return " 拉斯韦加斯 ";

        case 19: // '\023'
            return "   塔霍湖   ";

        case 21: // '\025'
            return "    纽约    ";

        case 22: // '\026'
            return "   曼哈顿   ";

        case 23: // '\027'
            return "  布鲁克林  ";

        case 24: // '\030'
            return "   奎恩斯   ";

        case 26: // '\032'
            return "   华盛顿   ";

        case 29: // '\035'
            return "   西雅图   ";

        case 30: // '\036'
            return " 华盛顿特区 ";

        case 31: // '\037'
            return "    白宫    ";

        case 33: // '!'
            return "  伊洛尼斯  ";

        case 34: // '"'
            return "   芝加哥   ";

        case 35: // '#'
            return " 鲁斯莫尔峰 ";

        case 38: // '&'
            return " 加利福尼亚 ";

        case 39: // '\''
            return "   洛杉矶   ";

        case 40: // '('
            return "   好莱坞   ";

        case 43: // '+'
            return "  贝弗莉山  ";

        case 44: // ','
            return "   旧金山   ";

        case 46: // '.'
            return "费阿斯门码头";

        case 47: // '/'
            return "   39码头   ";

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
            return "土地";
        }
    }
    /**
     * 转换到股票列表
     */

    void setDisplayToStockList()
    {
        Display.getDisplay(this).setCurrent(new StockList(this, this.playCanvas));
    }
    /**
     * 显示机会获得（地图上的？）的信息
     * @param msg 信息
     */

    void showChance(String msg)
    {
        Display.getDisplay(this).setCurrent(new PlayMessageForm(this, "机会", msg, 3, true));
    }
    /**
     * 转换到色子窗口
     */

    void setDisplayToDiceCanvas()
    {
        Display.getDisplay(this).setCurrent(new DiceCanvas(this, true));
    }
    /**
     * 转换到色子窗口(现金卡加钱用）
     * @param playerMoney 角色的金钱 (好象这里用不到）
     * @param playerID 角色的index （好象这里用不到）
     */

    void setDisplayToDiceForm(int playerMoney[], int playerID)
    {
        Display.getDisplay(this).setCurrent(new DiceCanvas(this, false));
    }
    /**
     * 显示最高分
     */

    void showHighScore()
    {
      String playerNames[] = {
          "孙小美", "阿土伯", "钱夫人"
      };
      String playerData[][] = new String[3][2];
      // 得到存贮的最高分
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
        // 要是出现异常，用默认值
        playerData[0][0] = "孙小美";
        playerData[0][1] = "105";
        playerData[1][0] = "阿土伯";
        playerData[1][1] = "155";
        playerData[2][0] = "钱夫人";
        playerData[2][1] = "166";
      }
      Display.getDisplay(this).setCurrent(new HighScoreCanvas(this, playerData));
    }
    /**
     * 更新最高分
     * @param playerName 玩家名字
     * @param turnCount 游戏进行的轮数（3个人依次玩一次为一轮）
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
        // 插入分数
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
        // 从新写入文件
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
     * 买地或或是加盖房子
     */

    public void buyGroundorBuilding()
    {
        playCanvas.buyLand();
        confirmMessage(); // 买土地返回
    }
    /**
     * 程序开始，转换到开始画面
     */

    public void startApp()
    {
        Display.getDisplay(this).setCurrent(new OpenCanvas(this));
    }
    /**
     * 暂停程序
     */

    public void pauseApp()
    {
    }
    /**
     * 破坏程序
     */

    public void destroyApp(boolean flag)
    {
    }
    /**
     * 退出游戏
     */

    public static void exitGame()
    {
        richMan.destroyApp(true);
        richMan.notifyDestroyed();
        richMan = null;
    }

}
