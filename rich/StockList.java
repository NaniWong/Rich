import javax.microedition.lcdui.*;
/**
  * 股票列表。
  * @author 嘟嘟熊
  * @version 1.0
  */

public class StockList
    extends List
    implements CommandListener {

  private KMRichMan richManObject;
  private PlayCanvas playCanvas;
  /**
    * 构造股票列表。
    */

  public StockList(KMRichMan kmrichman,PlayCanvas playCanvas) {
    super("股票信息", 3);
    this.richManObject = kmrichman;
    this.playCanvas = playCanvas;
    for (int i = 0; i < playCanvas.stock_name.length; i++) {
      this.append(playCanvas.stock_name[i] +
                  "(" + playCanvas.stock_price[i] + ")" +
                  "[" + playCanvas.stock_amplitude[i] + "%]",null);
    }
    this.addCommand(new Command("返回",8,0));
    setCommandListener(this);
  }
  /**
   * 处理按键
  */

  public void commandAction(Command command, Displayable displayable) {
    if (command.getLabel() == "返回") {
      richManObject.setDisplayToPlayCanvas1();
    }else
      richManObject.setDisplayToStockForm(getSelectedIndex());
  }
}
