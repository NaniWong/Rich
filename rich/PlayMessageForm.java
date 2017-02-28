import java.util.Timer;
import javax.microedition.lcdui.*;

/**
  * ��Ϸ��Ϣ���ڡ�
  * @author SoftStar,����
  * @version 1.0
  */

public class PlayMessageForm
    extends Form
    implements CommandListener {
  /**
   *	richmanʵ��
   */

  KMRichMan richMan;
  /**
   * ��ʱ��
   */

  private static Timer timer = null;
  /**
   * ��ƬID
   */

  private int cardIDs[];
  /**
   * ѡ��Ŀ�ƬID
   */

  private int selectedIDs;
  /**
   * ��Ϣ����
   */

  int messageType;
  /**
   * ��Ϣ����ѡ��״̬
   */

  private static byte choiceStatus = 0; //0 -- �Զ� ��1--�ֶ���3--���
  /**
   *	����һ������
   * @param kmrichman ��������
   * @param title ����
   * @param message ��Ϣ
   * @param type ��Ϣ����
   * @param isAutoSelect �Ƿ���Ϣ�������Զ�ѡ��ġ�
   */

  public PlayMessageForm(KMRichMan kmrichman, String title, String message,
                         int type, boolean isAutoSelect) {
    super(title);
    cardIDs = null;
    selectedIDs = 0;
    richMan = kmrichman;
    messageType = type;
    append(message);

    if (isAutoSelect) { //������Զ�ѡ��
      if (timer == null)
        timer = new Timer();
      timer.schedule(new Controlor(this), 2000L);
      choiceStatus = 0;
    }
    else {
      switch (type) {
        case 0: // �Ƿ������أ����췿�ݣ������ù�
          addCommand(new Command("��", 8, 0));
          addCommand(new Command("��", 8, 1));
          break;

        case 1: // ֧��·��
        case 2: // ����
        case 3: // ����
        case 4: // ����
        case 6: // ������𣬿�Ƭ��Ч
          addCommand(new Command("ȷ��", 8, 0));
          break;

        case 5: // ��Ƭ����
        case 7: // û�п�Ƭ
          addCommand(new Command("����", 8, 0));
          break;
    }
    choiceStatus = 1;
    }
    setCommandListener(this);
  }
  /**
   * ��ʼ����Ƭ
   */

  void intialCardIDs(int cardIDs[], int selectedID) {
    this.cardIDs = cardIDs;
    selectedIDs = selectedID;
  }
  /**
 *	������
 */

  public void commandAction(Command command, Displayable displayable) {
    if (choiceStatus == 0)
      return;

    if (command.getLabel() == "��") {
      choiceStatus = 3;
      richMan.buyGroundorBuilding();
    }
    else
    if (command.getLabel() == "��") {
      choiceStatus = 3;
      richMan.confirmMessage(); // �������ػ�ӸǷ���
    }
    else
    if (command.getLabel() == "ȷ��") {
      choiceStatus = 3;
      richMan.confirmMessage(); //֧��·�� ���� ���� ���� ������𣨿�Ƭ��Ч��
    }
    else
    if (command.getLabel() == "����" && getTitle() == "��Ƭ����") {
      choiceStatus = 3;
      richMan.useCard_hunman(cardIDs, selectedIDs);
    }
    else
    if (command.getLabel() == "����") {
      choiceStatus = 3;
      richMan.setDisplayToPlayCanvas1();
    }
  }
  /**
 *	�����Զ�ѡ��
 */

  void autoSelect() {
    if (choiceStatus == 3)
      return;

    switch (messageType) {
      case 0: // ѡ��
        richMan.buyGroundorBuilding(); //������ִ�е������ˡ���Ϊ���Բ��ó�������Ի���
        break;
      case 1: // ȷ��
      case 4:
      case 3:
      case 6:
        richMan.confirmMessage(); //֧��·�� ���� ���� ������𣨿�Ƭ��Ч��
        break;
      case 2: // ����
        richMan.confirmNews();
        break;
    }
  }

}