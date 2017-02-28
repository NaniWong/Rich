
import javax.microedition.lcdui.*;
/**
  * 股票窗口。
  * @author 嘟嘟熊
  * @version 1.0
  */

public class StockForm extends Form implements CommandListener {

  StringItem priceAndAmplitude;
  StringItem playMoney;
  StringItem buyPriceAndAmount;
  StringItem explain;

  private KMRichMan richManObject;
  private PlayCanvas playCanvas;
  /**
 *	股票的索引
 */


  int index;
  /**
 *	这个东东是什么呢？标尺？？就算吧：）
 */

  Gauge gauge;
  /**
 *	构造一个对象
 *  @param kmrichman 别问我，自己知道
 *  @param playCanvas 游戏画板
 *  @param index 股票的索引（第几个股票，不是有四个股票吗？0，1，2，3）
 */

  public StockForm(KMRichMan kmrichman,PlayCanvas playCanvas,int index) {
    super("");
    this.richManObject = kmrichman;
    this.playCanvas = playCanvas;
    this.index = index;

    this.setTitle(this.playCanvas.stock_name[index]);

    priceAndAmplitude = new StringItem("","价格:$" + playCanvas.stock_price[index] +
                                       " [" + playCanvas.stock_amplitude[index] + "%]");
    buyPriceAndAmount = new StringItem("", "持有价:$" +
                              playCanvas.player_stock[playCanvas.nowPlayerID][index][1] +
                              "[" + playCanvas.player_stock[playCanvas.nowPlayerID][index][0] + "]");
    playMoney = new StringItem("", "现金:$" +
                            playCanvas.player_money[playCanvas.nowPlayerID]);
    gauge = new Gauge("", true, 10, 0);
    explain = new StringItem("", "说明:\n购入时,百分比指用现金的百分之多少购股票;" +
                             "卖出时,指卖出购入股票的百分之多少.");


    setCommandListener(this);

    addCommand(new Command("返回", Command.OK, 0));
    addCommand(new Command("购入", Command.OK, 1));
    addCommand(new Command("卖出", Command.OK, 2));
    this.append(priceAndAmplitude);
    this.append(buyPriceAndAmount);
    this.append(playMoney);
    this.append(gauge);
    this.append(explain);

  }
  /**
   * 处理按键
   */

  public void commandAction(Command command, Displayable displayable) {
    if (command.getLabel() == "返回") {
      richManObject.setDisplayToStockList();
    }
    if (command.getLabel() == "购入") { //购入哦
      int money = playCanvas.player_money[playCanvas.nowPlayerID]; // 角色的钱
      int stock_money = money * this.gauge.getValue() / 10; //用于购买股票的钱
      int stock_price = playCanvas.stock_price[index]; // 股票价格
      int amount = 0;
      if (stock_money >=  stock_price) {
        amount = stock_money / stock_price;
        playCanvas.player_money[playCanvas.nowPlayerID] -= stock_price * amount;
        playCanvas.player_stock[playCanvas.nowPlayerID][index][1] = //计算购入价格
            (playCanvas.player_stock[playCanvas.nowPlayerID][index][1] *
            playCanvas.player_stock[playCanvas.nowPlayerID][index][0] +
            stock_price * amount) / (playCanvas.player_stock[playCanvas.nowPlayerID][index][0]
                               + amount);
        playCanvas.player_stock[playCanvas.nowPlayerID][index][0] += amount;
        this.buyPriceAndAmount.setText("持有价:$" +
                              playCanvas.player_stock[playCanvas.nowPlayerID][index][1] +
                              "[" + playCanvas.player_stock[playCanvas.nowPlayerID][index][0] + "]");
        this.playMoney.setText("现金:$" +
                            playCanvas.player_money[playCanvas.nowPlayerID]);
      }
    }
    if (command.getLabel() == "卖出") { //卖出股票了，写的不好，请大家包涵。。。。
      int totalAmount = playCanvas.player_stock[playCanvas.nowPlayerID][index][0];
      int amount = totalAmount * this.gauge.getValue() / 10;
      int stock_price = playCanvas.stock_price[index];
      if (amount > 0) {
        playCanvas.player_money[playCanvas.nowPlayerID] += stock_price * amount;
        playCanvas.player_stock[playCanvas.nowPlayerID][index][0] -= amount;
        if (playCanvas.player_stock[playCanvas.nowPlayerID][index][0] == 0)
          playCanvas.player_stock[playCanvas.nowPlayerID][index][1] = 0;

        this.buyPriceAndAmount.setText("持有价:$" +
                              playCanvas.player_stock[playCanvas.nowPlayerID][index][1] +
                              "[" + playCanvas.player_stock[playCanvas.nowPlayerID][index][0] + "]");
        this.playMoney.setText("现金:$" +
                            playCanvas.player_money[playCanvas.nowPlayerID]);
      }

    }

  }
}
