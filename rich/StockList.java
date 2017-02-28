import javax.microedition.lcdui.*;
/**
  * ��Ʊ�б�
  * @author ����
  * @version 1.0
  */

public class StockList
    extends List
    implements CommandListener {

  private KMRichMan richManObject;
  private PlayCanvas playCanvas;
  /**
    * �����Ʊ�б�
    */

  public StockList(KMRichMan kmrichman,PlayCanvas playCanvas) {
    super("��Ʊ��Ϣ", 3);
    this.richManObject = kmrichman;
    this.playCanvas = playCanvas;
    for (int i = 0; i < playCanvas.stock_name.length; i++) {
      this.append(playCanvas.stock_name[i] +
                  "(" + playCanvas.stock_price[i] + ")" +
                  "[" + playCanvas.stock_amplitude[i] + "%]",null);
    }
    this.addCommand(new Command("����",8,0));
    setCommandListener(this);
  }
  /**
   * ������
  */

  public void commandAction(Command command, Displayable displayable) {
    if (command.getLabel() == "����") {
      richManObject.setDisplayToPlayCanvas1();
    }else
      richManObject.setDisplayToStockForm(getSelectedIndex());
  }
}
