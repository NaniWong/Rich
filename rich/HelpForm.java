import javax.microedition.lcdui.*;
/**
  * �����˵���
  * @author SoftStar,����
  * @version 1.0
  */

public class HelpForm
    extends Form
    implements CommandListener {

  KMRichMan richManObject;
  /**
   * ����һ������
   */

  public HelpForm(KMRichMan kmrichman) {
    super("����");
    richManObject = kmrichman;
    append("       ����\n" +
           "Email:david_nj@hotmail.com\n" +
           "QQ:122173984\n" +
           "˵��:����Ϸ��SoftStar��˾��Ʒ.Siemens����zha��ֲ.�˰汾��'����'��zha��Simens�汾���޸�.\n" +
           "�ǵ��ϳ��е�ʱ��,�Ϳ�ʼ��SoftStar����." +
           "���ڻ���������!");
    setCommandListener(this);
    addCommand(new Command("����", 8, 0));
  }
  /**
   * ������
   */

  public void commandAction(Command command, Displayable displayable) {
    if (command.getLabel() == "����") {
      richManObject.setDisplayToGameMenu();
    }
  }
}
