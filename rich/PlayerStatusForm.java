import javax.microedition.lcdui.*;
/**
  * ��ɫ�ʲ�״̬���ڡ�
  * @author SoftStar,����
  * @version 1.0
  */

public class PlayerStatusForm
    extends Form
    implements CommandListener {

  KMRichMan richManObject;
  /**
   * ����һ������
   * @param kmrichman richmanʵ��
   * @param s ��ɫ״̬�ַ���
  */

  public PlayerStatusForm(KMRichMan kmrichman, String s) {
    super("��Ϸ״̬");
    richManObject = null;
    richManObject = kmrichman;
    append(s);
    setCommandListener(this);
    addCommand(new Command("����", 8, 0));
  }
  /**
   * ������
  */

  public void commandAction(Command command, Displayable displayable) {
    if (command.getLabel() == "����")
      richManObject.setDisplayToPlayCanvas1();
  }
}