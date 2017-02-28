import javax.microedition.lcdui.*;
/**
  * ��Ϸ�˵���
  * @author SoftStar,����
  * @version 1.0
  */

public class GameMenu
    extends List
    implements CommandListener {
  /**
   * richmanʵ��
   */

  private KMRichMan richManObject;
  /**
   * ��ɫ1��FACE
   */

  private Image playerFaceImage1;
  /**
   * ��ɫ2��FACE
   */

  private Image playerFaceImage2;
  /**
   * ��ɫ3��FACE
   */

  private Image playerFaceImage3;
  /**
   * ����һ������
   */

  public GameMenu(KMRichMan kmrichman) {
    super("�˵�[����1.2]", 3);
    richManObject = null;
    playerFaceImage1 = null;
    playerFaceImage2 = null;
    playerFaceImage3 = null;
    richManObject = kmrichman;
    try {
      playerFaceImage1 = Image.createImage("/res/image/ph0.png");
      playerFaceImage2 = Image.createImage("/res/image/ph1.png");
      playerFaceImage3 = Image.createImage("/res/image/ph2.png");
    }
    catch (Exception exception) {}
    append("�����ϴ���Ϸ", null);
    append("����Ϸ", null);
    append("�߷ְ�", null);
    append("����", null);
    append("�˳�", null);
    setCommandListener(this);
  }
  /**
   * ������
   */

  public void commandAction(Command command, Displayable displayable) {
    if (size() == 5)
      switch (getSelectedIndex()) {
        case 0:
          richManObject.continuePlay(3); // �����ϴ���Ϸ��
          break;

        case 1:
          selectPlayer();
          break;

        case 2:
          richManObject.showHighScore();
          break;

        case 3:
          richManObject.setDisplayToHelpForm();
          break;

        case 4:
          KMRichMan.exitGame();
          break;
      }
    else // ѡ���ɫ��ʼ����Ϸ
      richManObject.continuePlay(getSelectedIndex());
  }
  /**
   * ����ѡ���ɫ�˵�
   */

  private void selectPlayer() {
    for (int i = 0; i < 5; i++)
      delete(0);

    setTitle("ѡ��һ����ɫ");
    append("��С��", playerFaceImage1);
    append("������", playerFaceImage2);
    append("Ǯ����", playerFaceImage3);
  }
}