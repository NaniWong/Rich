
import javax.microedition.lcdui.*;
/**
  * ��Ʊ���ڡ�
  * @author ����
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
 *	��Ʊ������
 */


  int index;
  /**
 *	���������ʲô�أ���ߣ�������ɣ���
 */

  Gauge gauge;
  /**
 *	����һ������
 *  @param kmrichman �����ң��Լ�֪��
 *  @param playCanvas ��Ϸ����
 *  @param index ��Ʊ���������ڼ�����Ʊ���������ĸ���Ʊ��0��1��2��3��
 */

  public StockForm(KMRichMan kmrichman,PlayCanvas playCanvas,int index) {
    super("");
    this.richManObject = kmrichman;
    this.playCanvas = playCanvas;
    this.index = index;

    this.setTitle(this.playCanvas.stock_name[index]);

    priceAndAmplitude = new StringItem("","�۸�:$" + playCanvas.stock_price[index] +
                                       " [" + playCanvas.stock_amplitude[index] + "%]");
    buyPriceAndAmount = new StringItem("", "���м�:$" +
                              playCanvas.player_stock[playCanvas.nowPlayerID][index][1] +
                              "[" + playCanvas.player_stock[playCanvas.nowPlayerID][index][0] + "]");
    playMoney = new StringItem("", "�ֽ�:$" +
                            playCanvas.player_money[playCanvas.nowPlayerID]);
    gauge = new Gauge("", true, 10, 0);
    explain = new StringItem("", "˵��:\n����ʱ,�ٷֱ�ָ���ֽ�İٷ�֮���ٹ���Ʊ;" +
                             "����ʱ,ָ���������Ʊ�İٷ�֮����.");


    setCommandListener(this);

    addCommand(new Command("����", Command.OK, 0));
    addCommand(new Command("����", Command.OK, 1));
    addCommand(new Command("����", Command.OK, 2));
    this.append(priceAndAmplitude);
    this.append(buyPriceAndAmount);
    this.append(playMoney);
    this.append(gauge);
    this.append(explain);

  }
  /**
   * ������
   */

  public void commandAction(Command command, Displayable displayable) {
    if (command.getLabel() == "����") {
      richManObject.setDisplayToStockList();
    }
    if (command.getLabel() == "����") { //����Ŷ
      int money = playCanvas.player_money[playCanvas.nowPlayerID]; // ��ɫ��Ǯ
      int stock_money = money * this.gauge.getValue() / 10; //���ڹ����Ʊ��Ǯ
      int stock_price = playCanvas.stock_price[index]; // ��Ʊ�۸�
      int amount = 0;
      if (stock_money >=  stock_price) {
        amount = stock_money / stock_price;
        playCanvas.player_money[playCanvas.nowPlayerID] -= stock_price * amount;
        playCanvas.player_stock[playCanvas.nowPlayerID][index][1] = //���㹺��۸�
            (playCanvas.player_stock[playCanvas.nowPlayerID][index][1] *
            playCanvas.player_stock[playCanvas.nowPlayerID][index][0] +
            stock_price * amount) / (playCanvas.player_stock[playCanvas.nowPlayerID][index][0]
                               + amount);
        playCanvas.player_stock[playCanvas.nowPlayerID][index][0] += amount;
        this.buyPriceAndAmount.setText("���м�:$" +
                              playCanvas.player_stock[playCanvas.nowPlayerID][index][1] +
                              "[" + playCanvas.player_stock[playCanvas.nowPlayerID][index][0] + "]");
        this.playMoney.setText("�ֽ�:$" +
                            playCanvas.player_money[playCanvas.nowPlayerID]);
      }
    }
    if (command.getLabel() == "����") { //������Ʊ�ˣ�д�Ĳ��ã����Ұ�����������
      int totalAmount = playCanvas.player_stock[playCanvas.nowPlayerID][index][0];
      int amount = totalAmount * this.gauge.getValue() / 10;
      int stock_price = playCanvas.stock_price[index];
      if (amount > 0) {
        playCanvas.player_money[playCanvas.nowPlayerID] += stock_price * amount;
        playCanvas.player_stock[playCanvas.nowPlayerID][index][0] -= amount;
        if (playCanvas.player_stock[playCanvas.nowPlayerID][index][0] == 0)
          playCanvas.player_stock[playCanvas.nowPlayerID][index][1] = 0;

        this.buyPriceAndAmount.setText("���м�:$" +
                              playCanvas.player_stock[playCanvas.nowPlayerID][index][1] +
                              "[" + playCanvas.player_stock[playCanvas.nowPlayerID][index][0] + "]");
        this.playMoney.setText("�ֽ�:$" +
                            playCanvas.player_money[playCanvas.nowPlayerID]);
      }

    }

  }
}
