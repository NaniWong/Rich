import java.util.Timer;
import javax.microedition.lcdui.*;

/**
  * 游戏信息窗口。
  * @author SoftStar,嘟嘟熊
  * @version 1.0
  */

public class PlayMessageForm
    extends Form
    implements CommandListener {
  /**
   *	richman实例
   */

  KMRichMan richMan;
  /**
   * 定时器
   */

  private static Timer timer = null;
  /**
   * 卡片ID
   */

  private int cardIDs[];
  /**
   * 选择的卡片ID
   */

  private int selectedIDs;
  /**
   * 信息类型
   */

  int messageType;
  /**
   * 信息窗口选择状态
   */

  private static byte choiceStatus = 0; //0 -- 自动 ，1--手动，3--完毕
  /**
   *	构造一个对象
   * @param kmrichman 不解释了
   * @param title 标题
   * @param message 信息
   * @param type 信息类型
   * @param isAutoSelect 是否信息窗口是自动选择的。
   */

  public PlayMessageForm(KMRichMan kmrichman, String title, String message,
                         int type, boolean isAutoSelect) {
    super(title);
    cardIDs = null;
    selectedIDs = 0;
    richMan = kmrichman;
    messageType = type;
    append(message);

    if (isAutoSelect) { //如果是自动选择
      if (timer == null)
        timer = new Timer();
      timer.schedule(new Controlor(this), 2000L);
      choiceStatus = 0;
    }
    else {
      switch (type) {
        case 0: // 是否购买土地，建造房屋，建造旅馆
          addCommand(new Command("是", 8, 0));
          addCommand(new Command("否", 8, 1));
          break;

        case 1: // 支付路费
        case 2: // 新闻
        case 3: // 机会
        case 4: // 监狱
        case 6: // 获得美金，卡片功效
          addCommand(new Command("确认", 8, 0));
          break;

        case 5: // 卡片详情
        case 7: // 没有卡片
          addCommand(new Command("返回", 8, 0));
          break;
    }
    choiceStatus = 1;
    }
    setCommandListener(this);
  }
  /**
   * 初始化卡片
   */

  void intialCardIDs(int cardIDs[], int selectedID) {
    this.cardIDs = cardIDs;
    selectedIDs = selectedID;
  }
  /**
 *	处理按键
 */

  public void commandAction(Command command, Displayable displayable) {
    if (choiceStatus == 0)
      return;

    if (command.getLabel() == "是") {
      choiceStatus = 3;
      richMan.buyGroundorBuilding();
    }
    else
    if (command.getLabel() == "否") {
      choiceStatus = 3;
      richMan.confirmMessage(); // 不买土地或加盖房子
    }
    else
    if (command.getLabel() == "确认") {
      choiceStatus = 3;
      richMan.confirmMessage(); //支付路费 新闻 机会 监狱 获得美金（卡片功效）
    }
    else
    if (command.getLabel() == "返回" && getTitle() == "卡片详情") {
      choiceStatus = 3;
      richMan.useCard_hunman(cardIDs, selectedIDs);
    }
    else
    if (command.getLabel() == "返回") {
      choiceStatus = 3;
      richMan.setDisplayToPlayCanvas1();
    }
  }
  /**
 *	处理自动选择
 */

  void autoSelect() {
    if (choiceStatus == 3)
      return;

    switch (messageType) {
      case 0: // 选择
        richMan.buyGroundorBuilding(); //好象不能执行到这里了。因为电脑不用出现这个对话框
        break;
      case 1: // 确认
      case 4:
      case 3:
      case 6:
        richMan.confirmMessage(); //支付路费 机会 监狱 获得美金（卡片功效）
        break;
      case 2: // 新闻
        richMan.confirmNews();
        break;
    }
  }

}